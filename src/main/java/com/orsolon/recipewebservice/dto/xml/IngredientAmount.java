package com.orsolon.recipewebservice.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientAmount {
    @JacksonXmlProperty(localName = "qty")
    private String quantity;

    private String unit;
}
