package com.jinternals.demo.controllers.requests;


import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.validators.EnumValidator;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class CreateProductRequest {

    @NotEmpty(message = "Empty or null name not allowed")
    private String name;

    private String description;

    @EnumValidator(
            enumClass = ProductType.class,
            caseSensitive = true,
            message = "'${validatedValue}' is not valid type."
    )
    private String type;

}
