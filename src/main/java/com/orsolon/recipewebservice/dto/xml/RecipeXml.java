package com.orsolon.recipewebservice.dto.xml;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RecipeXml {

    private RecipeHead head;

    @JacksonXmlProperty(localName = "ingredients")
    private IngredientList ingredientList;

    @JacksonXmlElementWrapper(localName = "directions")
    @JacksonXmlProperty(localName = "step")
    private List<String> steps;
}
