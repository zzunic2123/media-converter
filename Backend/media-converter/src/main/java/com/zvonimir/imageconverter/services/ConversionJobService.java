package com.zvonimir.imageconverter.services;

import com.zvonimir.imageconverter.converters.MediaConversionContext;
import com.zvonimir.imageconverter.models.FormatType;
import com.zvonimir.imageconverter.models.ImageFormat;
import com.zvonimir.imageconverter.models.ConversionResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversionJobService {

    private final MediaConversionContext mediaConversionContext;
    private final JobTrackingService jobTrackingService;


    public ConversionJobService(
            MediaConversionContext mediaConversionContext,
            JobTrackingService jobTrackingService) {

        this.mediaConversionContext = mediaConversionContext;
        this.jobTrackingService = jobTrackingService;
    }

    public String startJob(MultipartFile file, FormatType format) {
        String jobId = UUID.randomUUID().toString();
        jobTrackingService.updateProgress(jobId, 0);

        // Save uploaded file to /uploads in the project root
        File uploadDir = new File(System.getProperty("user.dir"), "uploads");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String savedFileName = jobId + "_" + file.getOriginalFilename();
        File savedFile = new File(uploadDir, savedFileName);

        try {
            file.transferTo(savedFile);
        } catch (IOException e) {
            jobTrackingService.markFailed(jobId);
            System.err.println("Failed to save uploaded file for jobId: " + jobId);
            e.printStackTrace();
            return jobId;
        }

        new Thread(() -> {
            try {

                byte[] result = mediaConversionContext.convert(savedFile, format, jobId);
                jobTrackingService.storeResult(jobId, new ConversionResult(result, format, file.getOriginalFilename()));
                jobTrackingService.updateProgress(jobId, 100);

            } catch (Exception e) {
                jobTrackingService.markFailed(jobId);
                System.err.println("Error during conversion for jobId: " + jobId);
                e.printStackTrace();

            } finally {
                // Clean up the saved file
                if (savedFile.exists()) {
                    savedFile.delete();
                }
            }
        }).start();

        return jobId;
    }
}
