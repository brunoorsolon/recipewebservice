package com.orsolon.recipewebservice.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class RecipeXmlParser {
    public RecipeMl parseXml(InputStream inputStream) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(inputStream, RecipeMl.class);
    }
}
