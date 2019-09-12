package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.dto.NewCategory;
import io.geekmind.budgie.repository.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryControllerImpl {

    private final CategoryService categoryService;

    @Autowired
    public CategoryControllerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView loadCategories(ModelAndView requestContext) {
        requestContext.addObject("availableCategories", this.categoryService.loadAll());
        requestContext.setViewName("categories/index");
        return requestContext;
    }

    @GetMapping("/new")
    public ModelAndView showNewCategoryForm(ModelAndView requestContext) {
        requestContext.addObject("newCategory", new NewCategory());
        requestContext.setViewName("categories/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewCategory(@Valid NewCategory newCategory,
                                          BindingResult bindingResult,
                                          ModelAndView requestContext,
                                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("newCategory", newCategory);
            requestContext.setViewName("categories/new.form");
        } else {
            this.categoryService.create(newCategory);
            redirectAttributes.addFlashAttribute("message", "Category successfully created.");
            requestContext.setViewName("redirect:/categories/new");
        }
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeCategory(@PathVariable("id")Integer id,
                                       ModelAndView requestContext,
                                       RedirectAttributes redirectAttributes) {
        this.categoryService.remove(id);
        redirectAttributes.addFlashAttribute("message", "Category successfully removed.");
        requestContext.setViewName("redirect:/categories");
        return requestContext;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Integer id,
                                      ModelAndView requestContext,
                                      RedirectAttributes redirectAttributes) {
        Optional<ExistingCategory> existingCategory = this.categoryService.loadById(id);
        if (existingCategory.isPresent()) {
            requestContext.addObject("existingCategory", existingCategory.get());
            requestContext.setViewName("categories/edit.form");
        } else {
            redirectAttributes.addFlashAttribute("error", "Category not found.");
            requestContext.setViewName("redirect:/categories");
        }
        return requestContext;
    }

    @PostMapping("/update")
    public ModelAndView updateCategory(@Valid ExistingCategory existingCategory,
                                       BindingResult bindingResult,
                                       ModelAndView requestContext,
                                       RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            this.categoryService.update(existingCategory);
            redirectAttributes.addFlashAttribute("message", "Category successfully updated.");
            requestContext.setViewName("redirect:/categories");
        } else {
            requestContext.addObject("existingCategory", existingCategory);
            requestContext.setViewName("categories/edit.form");
        }
        return requestContext;
    }

}
