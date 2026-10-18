package food_delivery.controller;

import food_delivery.dto.MenuItemCreateRequestDTO;
import food_delivery.dto.MenuItemResponseDTO;
import food_delivery.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories/{categoryId}/items")
public class MenuItemController {
    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }
    @PostMapping
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(@PathVariable Long categoryId,@Valid @RequestBody  MenuItemCreateRequestDTO request){
        if (!categoryId.equals(request.getCategoryId())) {
            throw new IllegalArgumentException(
                    "Category ID in URL must match category ID in request body"
            );
        }
        MenuItemResponseDTO responseDTO=menuItemService.createMenuItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    @GetMapping
    public ResponseEntity<List<MenuItemResponseDTO>> getMenuItemsByCategoryId(@PathVariable Long categoryId){
        List<MenuItemResponseDTO>items=menuItemService.getMenuItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }
    @GetMapping("/manage")
    public ResponseEntity<List<MenuItemResponseDTO>> getOwnerMenuItemsByCategoryId(@PathVariable Long categoryId){
        List<MenuItemResponseDTO> items=menuItemService.getOwnerMenuItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }
    @PutMapping("/{itemId}")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(@PathVariable Long itemId,@PathVariable Long categoryId,@Valid @RequestBody MenuItemCreateRequestDTO request){
        MenuItemResponseDTO updatedItem=menuItemService.updateMenuItem(itemId,categoryId,request);
        return ResponseEntity.ok(updatedItem);
    }
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId,@PathVariable Long categoryId){
        menuItemService.deleteItem(itemId,categoryId);
        return ResponseEntity.noContent().build();
    }
}
