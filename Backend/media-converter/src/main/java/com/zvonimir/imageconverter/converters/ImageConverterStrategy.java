package com.zvonimir.imageconverter.converters;

import com.zvonimir.imageconverter.models.ImageFormat;
import com.zvonimir.imageconverter.services.JobTrackingService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ImageConverterStrategy implements MediaConversionStrategy<ImageFormat> {

    private final JobTrackingService jobTrackingService;

    public ImageConverterStrategy(JobTrackingService jobTrackingService) {
        this.jobTrackingService = jobTrackingService;
    }
    @Override
    public byte[] convert(File inputFile, ImageFormat format, String jobId) throws IOException, InterruptedException {
        for (int i = 1; i <= 9; i++) {
            Thread.sleep(1000);
            jobTrackingService.updateProgress(jobId, i * 10);
        }

        try (InputStream input = new FileInputStream(inputFile);
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            Thumbnails.of(input)
                    .scale(1.0)
                    .outputFormat(format.getExtension())
                    .toOutputStream(output);

            jobTrackingService.updateProgress(jobId, 100);
            return output.toByteArray();
        }
    }
}
