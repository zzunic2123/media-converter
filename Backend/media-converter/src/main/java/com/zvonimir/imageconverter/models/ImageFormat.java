package com.zvonimir.imageconverter.models;

public enum ImageFormat implements FormatType {
    PNG("png"),
    JPEG("jpeg"),
    BMP("bmp"),
    GIF("gif");

    private final String extension;

    ImageFormat(String extension) {
        this.extension = extension;
    }

    @Override
    public String getExtension() {
        return extension;
    }
}
