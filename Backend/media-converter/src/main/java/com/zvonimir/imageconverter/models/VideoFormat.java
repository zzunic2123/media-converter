package com.zvonimir.imageconverter.models;

public enum VideoFormat implements FormatType {
    MP4("mp4"),
    AVI("avi"),
    WEBM("webm"),
    MKV("mkv");

    private final String extension;

    VideoFormat(String extension) {
        this.extension = extension;
    }

    @Override
    public String getExtension() {
        return extension;
    }
}
