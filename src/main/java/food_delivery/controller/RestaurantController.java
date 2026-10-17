package food_delivery.controller;

import food_delivery.dto.*;
import food_delivery.service.RestaurantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@Valid @RequestBody RestaurantCreateRequestDTO request){
        RestaurantResponseDTO restaurant=restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantPublicResponseDTO> getRestaurantById(@PathVariable Long restaurantId){
        RestaurantPublicResponseDTO restaurant=restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.status(HttpStatus.OK).body(restaurant);
    }
    @PutMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(@PathVariable Long restaurantId,@Valid @RequestBody RestaurantUpdateRequestDTO request){
        RestaurantResponseDTO restaurant=restaurantService.updateRestaurant(restaurantId,request);
        return ResponseEntity.ok(restaurant);
    }
    @GetMapping("/restaurants/{restaurantId}/review")
    public ResponseEntity<RestaurantResponseDTO> getMyRestaurantReview(@PathVariable Long restaurantId){
        RestaurantResponseDTO restaurant=restaurantService.getMyRestaurantReview(restaurantId);
        return ResponseEntity.ok(restaurant);
    }
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantPublicResponseDTO>> getActiveRestaurants(){
        List<RestaurantPublicResponseDTO> responseDTOList=restaurantService.getActiveRestaurants();
        return ResponseEntity.ok(responseDTOList);
    }
    @GetMapping("/restaurants/city")
    public ResponseEntity<Page<RestaurantPublicResponseDTO>> getActiveRestaurantsByCity(@RequestParam String city,@RequestParam(defaultValue = "0") @Min(0)int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<RestaurantPublicResponseDTO> restaurants=restaurantService.getActiveRestaurantsByCity(city,page,size);
        return ResponseEntity.ok(restaurants);
    }
    @GetMapping("/restaurants/search")
    public ResponseEntity<Page<RestaurantPublicResponseDTO>> searchActiveRestaurantsPaginated(@RequestParam @NotBlank String name, @RequestParam(defaultValue = "0") @Min(0)int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
       Page<RestaurantPublicResponseDTO>restaurants= restaurantService.searchActiveRestaurantsPaginated(name,page,size);
        return ResponseEntity.ok(restaurants);
    }
    @GetMapping("/restaurants/paginated")
    public ResponseEntity<Page<RestaurantPublicResponseDTO>> getActiveRestaurantsPaginated(@RequestParam(defaultValue = "0") @Min(0)int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        Page<RestaurantPublicResponseDTO>restaurants=restaurantService.getActiveRestaurantsPaginated(page,size);
        return ResponseEntity.ok(restaurants);
    }
}
