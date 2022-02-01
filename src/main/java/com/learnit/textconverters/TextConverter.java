package com.learnit.textconverters;

import java.io.IOException;

abstract public class TextConverter {
    protected final String htmlBase = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>";
    protected String filePath;

    protected TextConverter(String filePath){
        this.filePath = filePath;
    }

    public abstract String convert() throws IOException;
}
