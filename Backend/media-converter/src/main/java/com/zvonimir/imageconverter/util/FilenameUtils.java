package com.zvonimir.imageconverter.util;

import com.zvonimir.imageconverter.models.ImageFormat;

public class FilenameUtils {

    private FilenameUtils() {
    }

    public static String generateConvertedFilename(String originalFilename, String format) {

        String baseName = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(0, originalFilename.lastIndexOf('.'))
                : "image";

        baseName = baseName.replaceAll("[^a-zA-Z0-9-_]", "_");

        return baseName + "-converted." + format.toLowerCase();
    }
}
