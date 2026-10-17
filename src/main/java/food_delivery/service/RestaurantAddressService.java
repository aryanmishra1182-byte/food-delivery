package food_delivery.service;

import food_delivery.dto.RestaurantAddressRequestDTO;
import food_delivery.dto.RestaurantAddressResponseDTO;
import food_delivery.entity.Restaurant;
import food_delivery.entity.RestaurantAddress;
import food_delivery.entity.User;
import food_delivery.exception.AddressAlreadyExistsException;
import food_delivery.exception.AddressNotFoundException;
import food_delivery.exception.RestaurantNotFoundException;
import food_delivery.exception.UserNotFoundException;
import food_delivery.repository.RestaurantAddressRepository;
import food_delivery.repository.RestaurantRepository;
import food_delivery.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RestaurantAddressService {
    private final RestaurantAddressRepository restaurantAddressRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    public RestaurantAddressService(RestaurantAddressRepository restaurantAddressRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantAddressRepository = restaurantAddressRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }
    public RestaurantAddressResponseDTO addAddress(Long restaurantId,RestaurantAddressRequestDTO request){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User owner=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Owner Not Found"));
        Restaurant restaurant=restaurantRepository.findById(restaurantId).orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("You cannot update this restaurant");
        }
    boolean exists=restaurantAddressRepository.existsByRestaurant_Id(restaurantId);
        if(exists){
            throw new AddressAlreadyExistsException("Address Already exits for the given Restaurant");
        }
        RestaurantAddress address=new RestaurantAddress();
            address.setAddressLine(request.getAddressLine());
            address.setCity(request.getCity());
            address.setLandmark(request.getLandmark());
            address.setLatitude(request.getLatitude());
            address.setLongitude(request.getLongitude());
            address.setPostalCode(request.getPostalCode());
            address.setState(request.getState());
            address.setRestaurant(restaurant);
            RestaurantAddress savedAddress=restaurantAddressRepository.save(address);

        return new RestaurantAddressResponseDTO(
             savedAddress.getId(),
                savedAddress.getRestaurant().getId(),
                savedAddress.getAddressLine(),
                savedAddress.getLandmark(),
                savedAddress.getCity(),
                savedAddress.getState(),
                savedAddress.getPostalCode(),
                savedAddress.getLatitude(),
                savedAddress.getLongitude()
        );
    }
    public RestaurantAddressResponseDTO getAddressByRestaurant(Long restaurantId){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Owner Not Found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantNotFoundException("Restaurant Not Found"));
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException(
                    "You cannot access this restaurant's address");
        }
        RestaurantAddress address=restaurantAddressRepository.findByRestaurant_Id(restaurantId).orElseThrow(()->new AddressNotFoundException("Address Not Found"));
        return new RestaurantAddressResponseDTO(
                address.getId(),
                address.getRestaurant().getId(),
                address.getAddressLine(),
                address.getLandmark(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getLatitude(),
                address.getLongitude()
        );
    }
    public RestaurantAddressResponseDTO updateAddress(Long restaurantId,RestaurantAddressRequestDTO requestDTO){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Owner Not Found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantNotFoundException("Restaurant Not Found"));
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException(
                    "You cannot update this restaurant's address");
        }
        RestaurantAddress address=restaurantAddressRepository.findByRestaurant_Id(restaurantId).orElseThrow(()->new AddressNotFoundException("Address Not Found"));
        address.setAddressLine(requestDTO.getAddressLine());
        address.setLandmark(requestDTO.getLandmark());
        address.setPostalCode(requestDTO.getPostalCode());
        address.setState(requestDTO.getState());
        address.setLongitude(requestDTO.getLongitude());
        address.setLatitude(requestDTO.getLatitude());
        address.setCity(requestDTO.getCity());
        RestaurantAddress savedAddress=restaurantAddressRepository.save(address);
        return new RestaurantAddressResponseDTO(
                savedAddress.getId(),
                savedAddress.getRestaurant().getId(),
                savedAddress.getAddressLine(),
                savedAddress.getLandmark(),
                savedAddress.getCity(),
                savedAddress.getState(),
                savedAddress.getPostalCode(),
                savedAddress.getLatitude(),
                savedAddress.getLongitude()
        );
    }
    public void deleteAddress(Long restaurantId){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Owner Not Found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantNotFoundException("Restaurant Not Found"));
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException(
                    "You cannot delete this restaurant's address");
        }
        RestaurantAddress address=restaurantAddressRepository.findByRestaurant_Id(restaurantId).orElseThrow(()->new AddressNotFoundException("Address Not Found"));
        restaurantAddressRepository.delete(address);
    }

}
