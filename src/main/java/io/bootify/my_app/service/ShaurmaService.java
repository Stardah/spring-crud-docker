package io.bootify.my_app.service;

import io.bootify.my_app.domain.Ingredient;
import io.bootify.my_app.domain.Shaurma;
import io.bootify.my_app.domain.ShaurmaOrder;
import io.bootify.my_app.model.ShaurmaDTO;
import io.bootify.my_app.repos.IngredientRepository;
import io.bootify.my_app.repos.ShaurmaOrderRepository;
import io.bootify.my_app.repos.ShaurmaRepository;
import io.bootify.my_app.util.NotFoundException;
import io.bootify.my_app.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Transactional
@Service
public class ShaurmaService {

    private final ShaurmaRepository shaurmaRepository;
    private final IngredientRepository ingredientRepository;
    private final ShaurmaOrderRepository shaurmaOrderRepository;

    public ShaurmaService(final ShaurmaRepository shaurmaRepository,
            final IngredientRepository ingredientRepository,
            final ShaurmaOrderRepository shaurmaOrderRepository) {
        this.shaurmaRepository = shaurmaRepository;
        this.ingredientRepository = ingredientRepository;
        this.shaurmaOrderRepository = shaurmaOrderRepository;
    }

    public List<ShaurmaDTO> findAll() {
        final List<Shaurma> shaurmas = shaurmaRepository.findAll(Sort.by("id"));
        return shaurmas.stream()
                .map((shaurma) -> mapToDTO(shaurma, new ShaurmaDTO()))
                .toList();
    }

    public ShaurmaDTO get(final Long id) {
        return shaurmaRepository.findById(id)
                .map(shaurma -> mapToDTO(shaurma, new ShaurmaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ShaurmaDTO shaurmaDTO) {
        final Shaurma shaurma = new Shaurma();
        mapToEntity(shaurmaDTO, shaurma);
        return shaurmaRepository.save(shaurma).getId();
    }

    public void update(final Long id, final ShaurmaDTO shaurmaDTO) {
        final Shaurma shaurma = shaurmaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(shaurmaDTO, shaurma);
        shaurmaRepository.save(shaurma);
    }

    public void delete(final Long id) {
        shaurmaRepository.deleteById(id);
    }

    private ShaurmaDTO mapToDTO(final Shaurma shaurma, final ShaurmaDTO shaurmaDTO) {
        shaurmaDTO.setId(shaurma.getId());
        shaurmaDTO.setComments(shaurma.getComments());
        shaurmaDTO.setIngredientList(shaurma.getIngredientList() == null ? null : shaurma.getIngredientList().stream()
                .map(ingredient -> ingredient.getId())
                .toList());
        return shaurmaDTO;
    }

    private Shaurma mapToEntity(final ShaurmaDTO shaurmaDTO, final Shaurma shaurma) {
        shaurma.setComments(shaurmaDTO.getComments());
        final List<Ingredient> ingredientList = ingredientRepository.findAllById(
                shaurmaDTO.getIngredientList() == null ? Collections.emptyList() : shaurmaDTO.getIngredientList());
        if (ingredientList.size() != (shaurmaDTO.getIngredientList() == null ? 0 : shaurmaDTO.getIngredientList().size())) {
            throw new NotFoundException("one of ingredientList not found");
        }
        shaurma.setIngredientList(ingredientList.stream().collect(Collectors.toSet()));
        return shaurma;
    }

    public String getReferencedWarning(final Long id) {
        final Shaurma shaurma = shaurmaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ShaurmaOrder shaurmaListShaurmaOrder = shaurmaOrderRepository.findFirstByShaurmaList(shaurma);
        if (shaurmaListShaurmaOrder != null) {
            return WebUtils.getMessage("shaurma.shaurmaOrder.shaurmaList.referenced", shaurmaListShaurmaOrder.getId());
        }
        return null;
    }

}
