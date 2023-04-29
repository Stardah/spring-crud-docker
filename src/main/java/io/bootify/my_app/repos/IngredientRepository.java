package io.bootify.my_app.repos;

import io.bootify.my_app.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
