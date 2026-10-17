package food_delivery.controller;

import food_delivery.dto.RestaurantRejectionRequestDTO;
import food_delivery.dto.RestaurantResponseDTO;
import food_delivery.entity.RestaurantStatus;
import food_delivery.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final RestaurantService restaurantService;

    public AdminController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @GetMapping("/restaurants/pending")
    public ResponseEntity<List<RestaurantResponseDTO>>findByStatus(){
        List<RestaurantResponseDTO> restaurants=restaurantService.findByStatus(RestaurantStatus.PENDING_APPROVAL);
        return ResponseEntity.ok(restaurants);
    }
    @PatchMapping("/restaurants/{id}/approve")
    public ResponseEntity<RestaurantResponseDTO> approveRestaurant(@PathVariable Long id){
        RestaurantResponseDTO restaurant=restaurantService.approveRestaurant(id);
        return ResponseEntity.ok(restaurant);
    }
    @PatchMapping("/restaurants/{id}/reject")
    public ResponseEntity<RestaurantResponseDTO> rejectRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRejectionRequestDTO request){
        RestaurantResponseDTO restaurant=restaurantService.rejectRestaurant(id,request);
        return ResponseEntity.ok(restaurant);
    }
}
