package com.orsolon.recipewebservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCategoryDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String name;
}
