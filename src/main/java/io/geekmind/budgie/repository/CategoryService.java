package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.mapper.CategoryToExistingCategoryMapper;
import io.geekmind.budgie.model.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final Mapper<Category, ExistingCategory> categoryToCategoryMapper;
    private final CategoryRepository categoryRespository;

    @Autowired
    public CategoryService(@Qualifier(CategoryToExistingCategoryMapper.QUALIFIER) Mapper<Category, ExistingCategory> categoryToExistingCategoryMapper,
                           CategoryRepository categoryRepository) {
        this.categoryToCategoryMapper = categoryToExistingCategoryMapper;
        this.categoryRespository = categoryRepository;
    }

    public List<ExistingCategory> loadAll() {
        return this.categoryRespository.findAll(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(this.categoryToCategoryMapper::mapTo)
            .collect(Collectors.toList());
    }

}
