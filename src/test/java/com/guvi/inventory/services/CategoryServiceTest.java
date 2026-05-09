package com.guvi.inventory.services;

import com.guvi.inventory.DTO.CategoryRequest;
import com.guvi.inventory.DTO.CategoryResponse;
import com.guvi.inventory.model.Category;
import com.guvi.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    private Category cat;

    @BeforeEach
    void setUp() {
        cat = new Category();
        cat.setId(1L);
        cat.setName("Electronics");
        cat.setDescription("desc");
        cat.setActive(true);
    }

    @Test
    void createCategory_shouldCreateWhenNameIsUnique() {
        when(categoryRepository.existsByName("Electronics")).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(cat);
        CategoryResponse r = categoryService.createCategory(new CategoryRequest("Electronics", "desc"));
        assertEquals(1L, r.getId());
        assertTrue(r.getActive());
        verify(categoryRepository).save(any());
    }

    @Test
    void createCategory_shouldThrowForDuplicateName() {
        when(categoryRepository.existsByName("Electronics")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory(new CategoryRequest("Electronics", "d")));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_shouldUpdateFields() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(categoryRepository.existsByName("Gadgets")).thenReturn(false);
        when(categoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        CategoryResponse r = categoryService.updateCategory(1L, new CategoryRequest("Gadgets", "n"));
        assertEquals("Gadgets", r.getName());
    }

    @Test
    void updateCategory_shouldAllowSameName() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(categoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        categoryService.updateCategory(1L, new CategoryRequest("Electronics", "u"));
        verify(categoryRepository, never()).existsByName(any());
    }

    @Test
    void updateCategory_shouldThrowWhenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.updateCategory(99L, new CategoryRequest("X", "Y")));
    }

    @Test
    void updateCategory_shouldThrowWhenNameConflicts() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(categoryRepository.existsByName("Gadgets")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.updateCategory(1L, new CategoryRequest("Gadgets", "d")));
    }

    @Test
    void deleteCategory_shouldSoftDelete() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(categoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        categoryService.deleteCategory(1L);
        verify(categoryRepository).save(argThat(c -> !c.getActive()));
    }

    @Test
    void deleteCategory_shouldThrowWhenNotFound() {
        when(categoryRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(5L));
    }

    @Test
    void getCategoryById_shouldReturn() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        assertEquals(1L, categoryService.getCategoryById(1L).getId());
    }

    @Test
    void getCategoryById_shouldThrow() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void getAllCategories_shouldReturnActiveOnly() {
        when(categoryRepository.findAllByActive(true)).thenReturn(List.of(cat));
        assertEquals(1, categoryService.getAllCategories().size());
    }

    @Test
    void getAllCategories_shouldReturnEmptyList() {
        when(categoryRepository.findAllByActive(true)).thenReturn(List.of());
        assertTrue(categoryService.getAllCategories().isEmpty());
    }
}
