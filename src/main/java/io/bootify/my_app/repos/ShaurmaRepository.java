package io.bootify.my_app.repos;

import io.bootify.my_app.domain.Ingredient;
import io.bootify.my_app.domain.Shaurma;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShaurmaRepository extends JpaRepository<Shaurma, Long> {

    Shaurma findFirstByIngredientList(Ingredient ingredient);

}
