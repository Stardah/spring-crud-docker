package io.bootify.my_app.controller;

import io.bootify.my_app.model.IngredientDTO;
import io.bootify.my_app.model.IngredientType;
import io.bootify.my_app.service.IngredientService;
import io.bootify.my_app.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(final IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("typeValues", IngredientType.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("ingredients", ingredientService.findAll());
        return "ingredient/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("ingredient") final IngredientDTO ingredientDTO) {
        return "ingredient/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("ingredient") @Valid final IngredientDTO ingredientDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ingredient/add";
        }
        ingredientService.create(ingredientDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ingredient.create.success"));
        return "redirect:/ingredients";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("ingredient", ingredientService.get(id));
        return "ingredient/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("ingredient") @Valid final IngredientDTO ingredientDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ingredient/edit";
        }
        ingredientService.update(id, ingredientDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ingredient.update.success"));
        return "redirect:/ingredients";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = ingredientService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            ingredientService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("ingredient.delete.success"));
        }
        return "redirect:/ingredients";
    }

}
