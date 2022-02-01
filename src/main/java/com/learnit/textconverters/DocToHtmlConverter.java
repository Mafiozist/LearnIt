package com.learnit.textconverters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class DocToHtmlConverter extends TextConverter{

    DocToHtmlConverter(String filePath){
        super(filePath);
    }

    @Override
    public String convert() throws IOException {
        DocumentConverter converter = new DocumentConverter();
        Result<String> result = converter.convertToHtml(new File(filePath));
        String html = result.getValue(); // The generated HTML
        Set<String> warnings = result.getWarnings(); // Any warnings during conversion
        //System.out.println("Result+" + '\n' + html);
        //System.out.println("Warnings+" + '\n' + warnings);

        Document doc = Jsoup.parse(htmlBase); //Использование Jsoup для парсинга текста в html
        doc.body().append(html);
        System.out.println(doc.html());

        return doc.html();
    }
}
