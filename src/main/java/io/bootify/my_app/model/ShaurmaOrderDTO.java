package io.bootify.my_app.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ShaurmaOrderDTO {

    private Long id;

    @NotNull
    private Long shaurmaList;

}
