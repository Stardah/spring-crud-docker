package io.bootify.my_app.controller;

import io.bootify.my_app.domain.Shaurma;
import io.bootify.my_app.model.ShaurmaOrderDTO;
import io.bootify.my_app.repos.ShaurmaRepository;
import io.bootify.my_app.service.ShaurmaOrderService;
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
@RequestMapping("/shaurmaOrders")
public class ShaurmaOrderController {

    private final ShaurmaOrderService shaurmaOrderService;
    private final ShaurmaRepository shaurmaRepository;

    public ShaurmaOrderController(final ShaurmaOrderService shaurmaOrderService,
            final ShaurmaRepository shaurmaRepository) {
        this.shaurmaOrderService = shaurmaOrderService;
        this.shaurmaRepository = shaurmaRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("shaurmaListValues", shaurmaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Shaurma::getId, Shaurma::getComments)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("shaurmaOrders", shaurmaOrderService.findAll());
        return "shaurmaOrder/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("shaurmaOrder") final ShaurmaOrderDTO shaurmaOrderDTO) {
        return "shaurmaOrder/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("shaurmaOrder") @Valid final ShaurmaOrderDTO shaurmaOrderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "shaurmaOrder/add";
        }
        shaurmaOrderService.create(shaurmaOrderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("shaurmaOrder.create.success"));
        return "redirect:/shaurmaOrders";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("shaurmaOrder", shaurmaOrderService.get(id));
        return "shaurmaOrder/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("shaurmaOrder") @Valid final ShaurmaOrderDTO shaurmaOrderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "shaurmaOrder/edit";
        }
        shaurmaOrderService.update(id, shaurmaOrderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("shaurmaOrder.update.success"));
        return "redirect:/shaurmaOrders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        shaurmaOrderService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("shaurmaOrder.delete.success"));
        return "redirect:/shaurmaOrders";
    }

}
