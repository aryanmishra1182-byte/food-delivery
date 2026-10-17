package food_delivery.controller;

import food_delivery.dto.MenuCategoryCreateRequestDTO;
import food_delivery.dto.MenuCategoryResponseDTO;
import food_delivery.service.MenuCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/categories")
public class MenuCategoryController {
private final MenuCategoryService menuCategoryService;

    public MenuCategoryController(MenuCategoryService menuCategoryService) {
        this.menuCategoryService = menuCategoryService;
    }
    @PostMapping
    public ResponseEntity<MenuCategoryResponseDTO> createCategory(@PathVariable Long restaurantId, @Valid @RequestBody MenuCategoryCreateRequestDTO request){
        MenuCategoryResponseDTO category=menuCategoryService.createCategory(restaurantId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
    @GetMapping
    public ResponseEntity<List<MenuCategoryResponseDTO>> getCategoriesByRestaurant(@PathVariable Long restaurantId){
        List<MenuCategoryResponseDTO>categories=menuCategoryService.getCategoriesByRestaurant(restaurantId);
        return ResponseEntity.ok(categories);
    }
    @PatchMapping("/{categoryId}")
    public ResponseEntity<MenuCategoryResponseDTO>updateCategory(@PathVariable Long restaurantId,@PathVariable  Long categoryId,@Valid @RequestBody MenuCategoryCreateRequestDTO request){
        MenuCategoryResponseDTO category=menuCategoryService.updateCategory(restaurantId,categoryId,request);
        return ResponseEntity.ok(category);
    }
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void>deleteCategory(@PathVariable Long restaurantId,@PathVariable Long categoryId){
        menuCategoryService.deleteCategory(restaurantId,categoryId);
        return ResponseEntity.noContent().build();
    }
}
