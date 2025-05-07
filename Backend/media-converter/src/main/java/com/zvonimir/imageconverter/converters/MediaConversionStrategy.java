package com.zvonimir.imageconverter.converters;

import com.zvonimir.imageconverter.models.FormatType;

import java.io.File;
import java.io.IOException;

public interface MediaConversionStrategy<T extends FormatType> {
    byte[] convert(File inputFile, T format, String jobId) throws IOException, InterruptedException;
}
