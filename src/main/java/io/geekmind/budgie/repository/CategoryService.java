package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.dto.NewCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.validation.UniquenessValidationService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements UniquenessValidationService {

    private final CategoryRepository categoryRepository;
    private final MapperFacade mapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository,
                           MapperFacade mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public List<ExistingCategory> loadAll() {
        return this.categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(category -> this.mapper.map(category, ExistingCategory.class))
            .sorted(Comparator.comparing(ExistingCategory::getName))
            .collect(Collectors.toList());
    }

    public Optional<ExistingCategory> remove(Integer id) {
        return this.categoryRepository.findById(id)
            .map(category -> {
                this.categoryRepository.delete(category);
                return this.mapper.map(category, ExistingCategory.class);
            });
    }

    public ExistingCategory create(NewCategory newCategory) {
        Category category = this.mapper.map(newCategory, Category.class);
        return this.mapper.map(
            this.categoryRepository.save(category),
            ExistingCategory.class
        );
    }

    public Optional<ExistingCategory> loadById(Integer id) {
        return this.categoryRepository.findById(id)
            .map(category -> this.mapper.map(category, ExistingCategory.class));
    }

    @Override
    public Boolean canValidate(Class<?> type) {
        return type.equals(NewCategory.class) || type.equals(ExistingCategory.class);
    }

    @Override
    public Boolean isValid(Object entity) {
        if (entity instanceof NewCategory) {
            return this.isValidToCreate((NewCategory) entity);
        } else if (entity instanceof ExistingCategory) {
            return this.isValidToUpdate((ExistingCategory) entity);
        } else {
            return false;
        }
    }

    protected Boolean isValidToCreate(NewCategory entity) {
         return this.categoryRepository.findAll()
            .stream()
            .noneMatch(nCategory -> nCategory.getName().equals(entity.getName()));
    }

    protected Boolean isValidToUpdate(ExistingCategory entity) {
          return this.categoryRepository.findAll()
            .stream()
            .filter(nCategory -> !nCategory.getId().equals(entity.getId()))
            .noneMatch(nCategory -> nCategory.getName().equals(entity.getName()));
    }

    public ExistingCategory update(ExistingCategory existingCategory) {
        Optional<Category> categoryToUpdate = this.categoryRepository.findById(existingCategory.getId());
        if (categoryToUpdate.isPresent()) {
            Category category = categoryToUpdate.get();
            this.mapper.map(existingCategory, category);
            this.categoryRepository.save(category);
        }
        return existingCategory;
    }
}
