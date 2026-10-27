package food_delivery.controller;

import food_delivery.dto.CustomerAddressRequestDTO;
import food_delivery.dto.CustomerAddressResponseDTO;
import food_delivery.service.CustomerAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/addresses")
public class CustomerAddressController {
    private final CustomerAddressService customerAddressService;

    public CustomerAddressController(CustomerAddressService customerAddressService) {
        this.customerAddressService = customerAddressService;
    }
    @PostMapping
    public ResponseEntity<CustomerAddressResponseDTO> addAddress(@RequestBody @Valid CustomerAddressRequestDTO request){
        CustomerAddressResponseDTO address=customerAddressService.addAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }
    @GetMapping
    public ResponseEntity<List<CustomerAddressResponseDTO>> getMyAddresses(){
        List<CustomerAddressResponseDTO> addresses=customerAddressService.getMyAddresses();
        return ResponseEntity.ok(addresses);
    }
    @GetMapping("/{addressId}")
    public ResponseEntity<CustomerAddressResponseDTO> getAddressById(@PathVariable Long addressId){
        CustomerAddressResponseDTO address=customerAddressService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }
    @PutMapping("/{addressId}")
    public ResponseEntity<CustomerAddressResponseDTO> updateAddress(@PathVariable Long addressId,@RequestBody @Valid CustomerAddressRequestDTO request){
        CustomerAddressResponseDTO address=customerAddressService.updateAddress(addressId,request);
        return ResponseEntity.ok(address);
    }
    @DeleteMapping("/{addressId}")
    public  ResponseEntity<Void> deleteAddress(@PathVariable Long addressId){
        customerAddressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{addressId}/default")
    public ResponseEntity<CustomerAddressResponseDTO> setDefaultAddress(@PathVariable Long addressId){
        CustomerAddressResponseDTO address=customerAddressService.setDefaultAddress(addressId);
        return ResponseEntity.ok(address);
    }
}
