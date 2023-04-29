package io.bootify.my_app.service;

import io.bootify.my_app.domain.Ingredient;
import io.bootify.my_app.domain.Shaurma;
import io.bootify.my_app.model.IngredientDTO;
import io.bootify.my_app.repos.IngredientRepository;
import io.bootify.my_app.repos.ShaurmaRepository;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ShaurmaRepository shaurmaRepository;

    public IngredientService(final IngredientRepository ingredientRepository,
            final ShaurmaRepository shaurmaRepository) {
        this.ingredientRepository = ingredientRepository;
        this.shaurmaRepository = shaurmaRepository;
    }

    public List<IngredientDTO> findAll() {
        final List<Ingredient> ingredients = ingredientRepository.findAll(Sort.by("id"));
        return ingredients.stream()
                .map((ingredient) -> mapToDTO(ingredient, new IngredientDTO()))
                .toList();
    }

    public IngredientDTO get(final Long id) {
        return ingredientRepository.findById(id)
                .map(ingredient -> mapToDTO(ingredient, new IngredientDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final IngredientDTO ingredientDTO) {
        final Ingredient ingredient = new Ingredient();
        mapToEntity(ingredientDTO, ingredient);
        return ingredientRepository.save(ingredient).getId();
    }

    public void update(final Long id, final IngredientDTO ingredientDTO) {
        final Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ingredientDTO, ingredient);
        ingredientRepository.save(ingredient);
    }

    public void delete(final Long id) {
        ingredientRepository.deleteById(id);
    }

    private IngredientDTO mapToDTO(final Ingredient ingredient, final IngredientDTO ingredientDTO) {
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setName(ingredient.getName());
        ingredientDTO.setType(ingredient.getType());
        return ingredientDTO;
    }

    private Ingredient mapToEntity(final IngredientDTO ingredientDTO, final Ingredient ingredient) {
        ingredient.setName(ingredientDTO.getName());
        ingredient.setType(ingredientDTO.getType());
        return ingredient;
    }

    public String getReferencedWarning(final Long id) {
        final Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Shaurma ingredientListShaurma = shaurmaRepository.findFirstByIngredientList(ingredient);
        if (ingredientListShaurma != null) {
            return WebUtils.getMessage("ingredient.shaurma.ingredientList.referenced", ingredientListShaurma.getId());
        }
        return null;
    }

}
