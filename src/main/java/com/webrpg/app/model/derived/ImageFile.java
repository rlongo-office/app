package com.webrpg.app.model.derived;

public class ImageFile {
    private String key;
    private String fileName;

    public ImageFile(String key, String fileName) {
        this.key = key;
        this.fileName = fileName;
    }
    public ImageFile(String [] fileData) {
        this.key = fileData[0];
        this.fileName = fileData[1];
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
