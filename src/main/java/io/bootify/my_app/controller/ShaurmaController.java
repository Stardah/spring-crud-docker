package io.bootify.my_app.controller;

import io.bootify.my_app.domain.Ingredient;
import io.bootify.my_app.model.ShaurmaDTO;
import io.bootify.my_app.repos.IngredientRepository;
import io.bootify.my_app.service.ShaurmaService;
import io.bootify.my_app.util.CustomCollectors;
import io.bootify.my_app.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/shaurmas")
public class ShaurmaController {

    private final ShaurmaService shaurmaService;
    private final IngredientRepository ingredientRepository;

    public ShaurmaController(final ShaurmaService shaurmaService,
            final IngredientRepository ingredientRepository) {
        this.shaurmaService = shaurmaService;
        this.ingredientRepository = ingredientRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("ingredientListValues", ingredientRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Ingredient::getId, Ingredient::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("shaurmas", shaurmaService.findAll());
        return "shaurma/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("shaurma") final ShaurmaDTO shaurmaDTO) {
        return "shaurma/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("shaurma") @Valid final ShaurmaDTO shaurmaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "shaurma/add";
        }
        shaurmaService.create(shaurmaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("shaurma.create.success"));
        return "redirect:/shaurmas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("shaurma", shaurmaService.get(id));
        return "shaurma/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("shaurma") @Valid final ShaurmaDTO shaurmaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "shaurma/edit";
        }
        shaurmaService.update(id, shaurmaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("shaurma.update.success"));
        return "redirect:/shaurmas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = shaurmaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            shaurmaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("shaurma.delete.success"));
        }
        return "redirect:/shaurmas";
    }

}
