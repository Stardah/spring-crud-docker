package io.bootify.my_app.model;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ShaurmaDTO {

    private Long id;

    @Size(max = 255)
    private String comments;

    private List<Long> ingredientList;

}
