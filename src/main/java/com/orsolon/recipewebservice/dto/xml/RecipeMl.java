package com.orsolon.recipewebservice.dto.xml;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "recipeml")
public class RecipeMl {

    @JacksonXmlProperty(isAttribute = true)
    private String version;

    private RecipeXml recipe;
}
