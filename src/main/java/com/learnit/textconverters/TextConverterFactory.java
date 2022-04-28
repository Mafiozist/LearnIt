package com.learnit.textconverters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import static com.learnit.textconverters.SupportedTextFormats.*;

//Fabric method for text file converter
//not multi-thread safety
public class TextConverterFactory {
   private static TextConverterFactory textConverterFactory;

   private TextConverterFactory(){}

   public static TextConverterFactory getInstance(){
      if(textConverterFactory == null) textConverterFactory = new TextConverterFactory();
      return textConverterFactory;
   }

   public TextConverter createTextConverter(String filePath) throws IOException {
      String[] rawFilePath = filePath.split("\\.");
      String fileType = rawFilePath[rawFilePath.length - 1];

      TextConverter textConverter = null;

      switch (SupportedTextFormats.valueOf(fileType.toUpperCase(Locale.ROOT))){
         case TXT:
            textConverter = new TxtToHtmlConverter(filePath);
            break;

         /*case  PDF:
            textConverter = new PdfToHtmlConverter(filePath);
            break;*/

         case DOC:
         case DOCX:
            textConverter = new DocToHtmlConverter(filePath);
            break;

         default:
            throw new IllegalStateException("Unexpected value: " + fileType);
      }

      return textConverter;
   }
}
