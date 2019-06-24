package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.dto.NewCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.mapper.CategoryToExistingCategoryMapper;
import io.geekmind.budgie.model.mapper.Mapper;
import io.geekmind.budgie.model.mapper.NewCategoryToCategoryMapper;
import io.geekmind.budgie.validation.UniquenessValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements UniquenessValidationService {

    private final Mapper<Category, ExistingCategory> categoryToExistingCategoryMapper;
    private final CategoryRepository categoryRepository;
    private final Mapper<NewCategory, Category> newCategoryMapper;

    @Autowired
    public CategoryService(@Qualifier(CategoryToExistingCategoryMapper.QUALIFIER)Mapper<Category, ExistingCategory> categoryToExistingCategoryMapper,
                           @Qualifier(NewCategoryToCategoryMapper.QUALIFIER)Mapper<NewCategory, Category> newCategoryMapper,
                           CategoryRepository categoryRepository) {
        this.categoryToExistingCategoryMapper = categoryToExistingCategoryMapper;
        this.newCategoryMapper = newCategoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<ExistingCategory> loadAll() {
        return this.categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(this.categoryToExistingCategoryMapper::mapTo)
            .collect(Collectors.toList());
    }

    public Optional<ExistingCategory> remove(Integer id) {
        return this.categoryRepository.findById(id)
            .map(category -> {
                this.categoryRepository.delete(category);
                return this.categoryToExistingCategoryMapper.mapTo(category);
            });
    }

    public ExistingCategory create(NewCategory newCategory) {
        Category category = this.newCategoryMapper.mapTo(newCategory);
        return this.categoryToExistingCategoryMapper.mapTo(
            this.categoryRepository.save(category)
        );
    }

    @Override
    public Boolean canValidate(Class<?> type) {
        return type.equals(NewCategory.class);
    }

    @Override
    public Boolean isValid(Object entity) {
        NewCategory category = (NewCategory)entity;
        return !this.categoryRepository.findByName(category.getName()).isPresent();
    }
}
