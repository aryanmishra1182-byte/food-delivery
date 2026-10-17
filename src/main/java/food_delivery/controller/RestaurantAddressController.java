package food_delivery.controller;

import food_delivery.dto.RestaurantAddressRequestDTO;
import food_delivery.dto.RestaurantAddressResponseDTO;
import food_delivery.service.RestaurantAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestaurantAddressController {
    private final RestaurantAddressService restaurantAddressService;

    public RestaurantAddressController(RestaurantAddressService restaurantAddressService) {
        this.restaurantAddressService = restaurantAddressService;
    }
    @PostMapping("/restaurants/{restaurantId}/address")
    public ResponseEntity<RestaurantAddressResponseDTO> addAddress(@PathVariable Long restaurantId, @Valid @RequestBody RestaurantAddressRequestDTO request){
        RestaurantAddressResponseDTO address=restaurantAddressService.addAddress(restaurantId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }
    @GetMapping("/restaurants/{restaurantId}/address")
    public ResponseEntity<RestaurantAddressResponseDTO> getAddressByRestaurant(@PathVariable Long restaurantId){
        RestaurantAddressResponseDTO address=restaurantAddressService.getAddressByRestaurant(restaurantId);
        return ResponseEntity.ok(address);
    }
    @PutMapping("/restaurants/{restaurantId}/address")
    public ResponseEntity<RestaurantAddressResponseDTO> updateAddress(@PathVariable Long restaurantId,@Valid @RequestBody RestaurantAddressRequestDTO requestDTO){
        RestaurantAddressResponseDTO newAddress=restaurantAddressService.updateAddress(restaurantId,requestDTO);
        return ResponseEntity.ok(newAddress);
    }
    @DeleteMapping("/restaurants/{restaurantId}/address")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long restaurantId){
        restaurantAddressService.deleteAddress(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
