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
public class IngredientDiv {
    private String title;
    @JacksonXmlProperty(localName = "ing")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<IngredientXml> ingredients;
}
