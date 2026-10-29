package food_delivery.controller;

import food_delivery.dto.CustomerProfileResponseDTO;
import food_delivery.dto.CustomerProfileUpdateRequestDTO;
import food_delivery.service.CustomerProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/profile")
public class CustomerProfileController {
    private final CustomerProfileService customerProfileService;

    public CustomerProfileController(CustomerProfileService customerProfileService) {
        this.customerProfileService = customerProfileService;
    }
    @GetMapping
    public ResponseEntity<CustomerProfileResponseDTO> getMyProfile(){
        CustomerProfileResponseDTO profile=customerProfileService.getMyProfile();
        return ResponseEntity.ok(profile);
    }
    @PutMapping
    public ResponseEntity<CustomerProfileResponseDTO> updateMyProfile(@Valid @RequestBody CustomerProfileUpdateRequestDTO request){
        CustomerProfileResponseDTO newProfile=customerProfileService.updateMyProfile(request);
        return ResponseEntity.ok(newProfile);
    }
}
