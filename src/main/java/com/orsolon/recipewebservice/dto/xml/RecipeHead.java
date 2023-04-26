package com.orsolon.recipewebservice.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeHead {
    private String title;

    @JacksonXmlElementWrapper(localName = "categories")
    @JacksonXmlProperty(localName = "cat")
    private List<String> categories;

    @JacksonXmlProperty(localName = "yield")
    private int yield;
}
