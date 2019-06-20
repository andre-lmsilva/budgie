package io.geekmind.budgie.controller;

import io.geekmind.budgie.repository.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

}
