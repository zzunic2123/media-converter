package com.zvonimir.imageconverter.converters;

import com.zvonimir.imageconverter.models.VideoFormat;
import com.zvonimir.imageconverter.services.JobTrackingService;
import org.springframework.stereotype.Component;


import java.io.*;
import java.util.UUID;

@Component
public class VideoConverterStrategy implements MediaConversionStrategy<VideoFormat> {

    private final JobTrackingService jobTrackingService;

    public VideoConverterStrategy(JobTrackingService jobTrackingService) {
        this.jobTrackingService = jobTrackingService;
    }
    @Override
    public byte[] convert(File inputFile, VideoFormat format, String jobId) throws IOException {

        File outputFile = createOutputFile(inputFile, format);
        double totalDuration = getSafeVideoDuration(inputFile);

        Process process = startFfmpegConversion(inputFile, outputFile);
        jobTrackingService.updateProgress(jobId, 10);

        trackProgressFromFfmpeg(process, totalDuration, jobId);
        waitForProcess(process);

        byte[] result = readOutputFile(outputFile);
        outputFile.delete();

        jobTrackingService.updateProgress(jobId, 100);
        return result;
    }

    private File createOutputFile(File inputFile, VideoFormat format) {
        String outputFileName = "converted-" + UUID.randomUUID() + "." + format.getExtension();
        return new File(inputFile.getParentFile(), outputFileName);
    }

    private double getSafeVideoDuration(File inputFile) throws IOException {
        try {
            return getVideoDuration(inputFile);
        } catch (Exception e) {
            throw new IOException("Failed to get video duration", e);
        }
    }

    private void trackProgressFromFfmpeg(Process process, double totalDuration, String jobId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            int lastReported = 10;

            while ((line = reader.readLine()) != null) {
                System.out.println("[ffmpeg] " + line);

                double currentTime = extractTimeInSeconds(line);
                if (currentTime > 0 && totalDuration > 0) {
                    int progress = (int) ((currentTime / totalDuration) * 90);
                    if (progress > lastReported) {
                        jobTrackingService.updateProgress(jobId, progress);
                        lastReported = progress;
                    }
                }
            }
        }
    }

    private void waitForProcess(Process process) throws IOException {
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg failed with exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Video conversion interrupted", e);
        }
    }
    private byte[] readOutputFile(File outputFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(outputFile);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }

            return bos.toByteArray();
        }
    }

    private Process startFfmpegConversion(File inputFile, File outputFile) throws IOException {
        File ffmpeg = new File("external/ffmpeg/ffmpeg-2025-05-05-git-f4e72eb5a3-essentials_build/bin/ffmpeg.exe");

        ProcessBuilder processBuilder = new ProcessBuilder(
                ffmpeg.getAbsolutePath(),
                "-y",
                "-i", inputFile.getAbsolutePath(),
                "-c:v", "libx264",
                "-c:a", "aac",
                outputFile.getAbsolutePath()
        );

        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private double getVideoDuration(File inputFile) throws IOException, InterruptedException {
        ProcessBuilder probeBuilder = new ProcessBuilder(
                new File("external/ffmpeg/ffmpeg-2025-05-05-git-f4e72eb5a3-essentials_build/bin/ffprobe.exe").getAbsolutePath(),
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                inputFile.getAbsolutePath()
        );

        Process process = probeBuilder.start();
        String output = new String(process.getInputStream().readAllBytes()).trim();
        process.waitFor();

        return Double.parseDouble(output);
    }

    private double extractTimeInSeconds(String line) {
        if (line.contains("time=")) {
            String[] parts = line.split("time=");
            if (parts.length > 1) {
                String timePart = parts[1].split(" ")[0]; // "00:00:05.46"
                return parseTimestampToSeconds(timePart);
            }
        }
        return -1;
    }

    private double parseTimestampToSeconds(String timestamp) {
        String[] parts = timestamp.split(":");
        if (parts.length != 3) return 0;

        double hours = Double.parseDouble(parts[0]);
        double minutes = Double.parseDouble(parts[1]);
        double seconds = Double.parseDouble(parts[2]);

        return hours * 3600 + minutes * 60 + seconds;
    }

}