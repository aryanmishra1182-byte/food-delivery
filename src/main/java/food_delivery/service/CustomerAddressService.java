package food_delivery.service;

import food_delivery.dto.CustomerAddressRequestDTO;
import food_delivery.dto.CustomerAddressResponseDTO;
import food_delivery.entity.CustomerAddress;
import food_delivery.entity.User;
import food_delivery.exception.CustomerAddressNotFoundException;
import food_delivery.exception.UserNotFoundException;
import food_delivery.repository.CustomerAddressRepository;
import food_delivery.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerAddressService {
    private final UserRepository userRepository;
    private final CustomerAddressRepository customerAddressRepository;

    public CustomerAddressService(UserRepository userRepository, CustomerAddressRepository customerAddressRepository) {
        this.userRepository = userRepository;
        this.customerAddressRepository = customerAddressRepository;
    }
    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("Customer must be authenticated");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }
    private CustomerAddressResponseDTO toResponseDTO(CustomerAddress address) {
        return new CustomerAddressResponseDTO(
                address.getId(),
                address.getLabel(),
                address.getAddressLine(),
                address.getPostalCode(),
                address.getLandmark(),
                address.getState(),
                address.getCity(),
                address.isDefault(),
                address.getCreatedAt()
        );
    }
    @Transactional
    public CustomerAddressResponseDTO addAddress(CustomerAddressRequestDTO request) {
        User owner = getAuthenticatedUser();

        if (request.isDefault()) {
            List<CustomerAddress> existingAddresses =
                    customerAddressRepository.findByUser_Id(owner.getId());

            for (CustomerAddress existing : existingAddresses) {
                existing.setDefault(false);
            }

            customerAddressRepository.saveAll(existingAddresses);
        }

        CustomerAddress address = new CustomerAddress();
        address.setUser(owner);
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setDefault(request.isDefault());
        address.setLabel(request.getLabel());
        address.setLandmark(request.getLandmark());
        address.setPostalCode(request.getPostalCode());
        address.setState(request.getState());

        CustomerAddress savedAddress = customerAddressRepository.save(address);

        return toResponseDTO(savedAddress);
    }
    public List<CustomerAddressResponseDTO> getMyAddresses(){
        User owner = getAuthenticatedUser();
        List<CustomerAddress> addresses=customerAddressRepository.findByUser_Id(owner.getId());
        return addresses.stream().map(this::toResponseDTO).toList();
    }
    public CustomerAddressResponseDTO getAddressById(Long addressId){
        User owner=getAuthenticatedUser();
        CustomerAddress address=customerAddressRepository.findByIdAndUser_Id(addressId,owner.getId()).orElseThrow(()->new CustomerAddressNotFoundException("Address Not Found!"));
        return toResponseDTO(address);
    }
    public CustomerAddressResponseDTO updateAddress(Long addressId,CustomerAddressRequestDTO request){
        User owner=getAuthenticatedUser();
        CustomerAddress address=customerAddressRepository.findByIdAndUser_Id(addressId,owner.getId()).orElseThrow(()->new CustomerAddressNotFoundException("Address Not Found!"));
        address.setLabel(request.getLabel());
        address.setLandmark(request.getLandmark());
        address.setPostalCode(request.getPostalCode());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setAddressLine(request.getAddressLine());
        CustomerAddress savedAddress=customerAddressRepository.save(address);
        return toResponseDTO(savedAddress);
    }
    public  void deleteAddress(Long addressId){
        User owner=getAuthenticatedUser();
    CustomerAddress address=customerAddressRepository.findByIdAndUser_Id(addressId,owner.getId()).orElseThrow(()->new CustomerAddressNotFoundException("Address Not Found!"));
    customerAddressRepository.delete(address);
    }
    @Transactional
    public CustomerAddressResponseDTO setDefaultAddress(Long addressId){
    User owner=getAuthenticatedUser();
    CustomerAddress selectedAddress=customerAddressRepository.findByIdAndUser_Id(addressId,owner.getId()).orElseThrow(()->new CustomerAddressNotFoundException("Address Not Found!"));
    List<CustomerAddress> addresses=customerAddressRepository.findByUser_Id(owner.getId());
    for(CustomerAddress address:addresses){
        address.setDefault(false);
    }

    selectedAddress.setDefault(true);
    customerAddressRepository.saveAll(addresses);
    return toResponseDTO(selectedAddress);
    }
}