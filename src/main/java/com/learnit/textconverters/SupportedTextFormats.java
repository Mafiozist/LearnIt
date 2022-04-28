package com.learnit.textconverters;

import java.util.Arrays;

//Needs to write in lower case
public enum SupportedTextFormats {
    DOC, DOCX, TXT;

    public static String[] getSupportedFormats() {
        return Arrays.toString(SupportedTextFormats.values()).replaceAll("^.|.$", "").split(", ");
    }

}
