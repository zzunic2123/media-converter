package com.zvonimir.imageconverter.converters;

import com.zvonimir.imageconverter.models.FormatType;
import com.zvonimir.imageconverter.models.ImageFormat;
import com.zvonimir.imageconverter.models.VideoFormat;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MediaConversionContext {

    private final ImageConverterStrategy imageStrategy;
    private final VideoConverterStrategy videoStrategy;

    public MediaConversionContext(ImageConverterStrategy imageStrategy,
                                  VideoConverterStrategy videoStrategy) {
        this.imageStrategy = imageStrategy;
        this.videoStrategy = videoStrategy;
    }

    public byte[] convert(File inputFile, FormatType format, String jobId) throws IOException, InterruptedException {
        if (format instanceof ImageFormat) {
            return imageStrategy.convert(inputFile, (ImageFormat) format, jobId);
        } else if (format instanceof VideoFormat) {
            return videoStrategy.convert(inputFile, (VideoFormat) format,jobId);
        } else {
            throw new IllegalArgumentException("Unsupported format type: " + format);
        }
    }
}