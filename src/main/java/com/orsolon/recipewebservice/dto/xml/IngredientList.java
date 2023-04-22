package com.orsolon.recipewebservice.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class IngredientList {

    @JacksonXmlProperty(localName = "ing-div")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<IngredientDiv> ingredientDivs;

    public List<IngredientDiv> getIngredientDivs() {
        return ingredientDivs;
    }

    public void setIngredientDivs(List<IngredientDiv> ingredientDivs) {
        this.ingredientDivs = ingredientDivs;
    }

    /*
        This is used for cases when IngredientDivs are not present in the origin XML.
        It reads the ingredients and creates an IngredientDiv instance.
        The class will only expose get methods for the ingredientDiv property, providing a single and standard access point to the ingredients list.
     */
    @JacksonXmlProperty(localName = "ing")
    @JacksonXmlElementWrapper(useWrapping = false)
    public void setIngredients(List<IngredientXml> ingredients) {
        IngredientDiv ingredientDiv = new IngredientDiv();
        ingredientDiv.setTitle("");
        ingredientDiv.setIngredients(ingredients);
        if (this.ingredientDivs == null) {
            this.ingredientDivs = new ArrayList<>();
        }
        this.ingredientDivs.add(ingredientDiv);
    }
}
