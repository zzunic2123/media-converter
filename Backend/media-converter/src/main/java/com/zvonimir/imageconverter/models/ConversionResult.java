package com.zvonimir.imageconverter.models;

public class ConversionResult {
    private final byte[] data;
    private final FormatType format;
    private final String OldFilename;

    public ConversionResult(byte[] data, FormatType format, String oldFilename) {
        this.data = data;
        this.format = format;
        OldFilename = oldFilename;
    }

    public byte[] getData() {
        return data;
    }

    public String getFormat() {
        return format.getExtension();
    }

    public String getOldFilename() {
        return OldFilename;
    }
}
