package food_delivery.service;

import food_delivery.dto.*;
import food_delivery.entity.Restaurant;
import food_delivery.entity.RestaurantAddress;
import food_delivery.entity.RestaurantStatus;
import food_delivery.entity.User;
import food_delivery.exception.RestaurantNotFoundException;
import food_delivery.exception.UserNotFoundException;
import food_delivery.repository.RestaurantAddressRepository;
import food_delivery.repository.RestaurantRepository;
import food_delivery.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantAddressRepository restaurantAddressRepository;
    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, RestaurantAddressRepository restaurantAddressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantAddressRepository = restaurantAddressRepository;
    }
    public RestaurantResponseDTO createRestaurant(RestaurantCreateRequestDTO createRequest){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User owner=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Owner Not Found"));
        Restaurant restaurant=new Restaurant();
        restaurant.setName(createRequest.getName());
        restaurant.setPhone(createRequest.getPhone());
        restaurant.setDescription(createRequest.getDescription());
        restaurant.setStatus(RestaurantStatus.PENDING_APPROVAL);
        restaurant.setOwner(owner);
        Restaurant savedRestaurant=restaurantRepository.save(restaurant);
        return new RestaurantResponseDTO(
                savedRestaurant.getId(),
                savedRestaurant.getName(),
                savedRestaurant.getDescription(),
                savedRestaurant.getPhone(),
                savedRestaurant.getStatus(),
                savedRestaurant.getOwner().getId(),
                savedRestaurant.getRejectionReason()
        );
    }
    public RestaurantPublicResponseDTO getRestaurantById(Long id){
    Restaurant restaurant=restaurantRepository.findById(id)
            .orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
    if(restaurant.getStatus()!=RestaurantStatus.ACTIVE)
        throw new RestaurantNotFoundException("Restaurant Not Found");
    return new RestaurantPublicResponseDTO(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getDescription(),
            restaurant.getPhone(),
            restaurant.getStatus()

    );
    }
    public RestaurantResponseDTO updateRestaurant(Long restaurantId,RestaurantUpdateRequestDTO request){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User owner=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Owner Not Found"));
        Restaurant restaurant=restaurantRepository.findById(restaurantId).orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("You cannot update this restaurant");
        }
            restaurant.setName(request.getName());
            restaurant.setDescription(request.getDescription());
            restaurant.setPhone(request.getPhone());
        Restaurant savedRestaurant=restaurantRepository.save(restaurant);
        return new RestaurantResponseDTO(
                savedRestaurant.getId(),
                savedRestaurant.getName(),
                savedRestaurant.getDescription(),
                savedRestaurant.getPhone(),
                savedRestaurant.getStatus(),
                savedRestaurant.getOwner().getId(),
                savedRestaurant.getRejectionReason()
        );
    }
    public List<RestaurantResponseDTO> findByStatus(RestaurantStatus status){
    List<Restaurant> restaurants=restaurantRepository.findByStatus(status);
    return restaurants.stream().map(restaurant -> new RestaurantResponseDTO(
         restaurant.getId(),
         restaurant.getName(),
            restaurant.getDescription(),
            restaurant.getPhone(),
            restaurant.getStatus(),
            restaurant.getOwner().getId(),
            restaurant.getRejectionReason()
    )).toList();
    }
    public RestaurantResponseDTO approveRestaurant(Long id){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User admin=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Admin Not Found"));
        Restaurant currestaurant=restaurantRepository.findById(id).orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
        if (currestaurant.getStatus() != RestaurantStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Restaurant is not pending approval");
        }
        currestaurant.setReviewedBy(admin);
        currestaurant.setReviewedAt(LocalDateTime.now());
        currestaurant.setStatus(RestaurantStatus.ACTIVE);
        Restaurant restaurant=restaurantRepository.save(currestaurant);
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getStatus(),
                restaurant.getOwner().getId(),
                restaurant.getRejectionReason()
        );
    }
    public RestaurantResponseDTO rejectRestaurant(Long id, RestaurantRejectionRequestDTO request){
        Restaurant currestaurant=restaurantRepository.findById(id).orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User admin=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Admin Not Found"));
        if (currestaurant.getStatus() != RestaurantStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Restaurant is not pending approval");
        }

        if(request.getRejectionReason()==null||request.getRejectionReason().isBlank()){
            throw new IllegalArgumentException("Rejection Reason Not Clear");
        }
        if (request.getRejectionReason().length() > 500) {
            throw new IllegalArgumentException(
                    "Rejection reason cannot exceed 500 characters"
            );
        }
        currestaurant.setReviewedBy(admin);
        currestaurant.setReviewedAt(LocalDateTime.now());
            currestaurant.setRejectionReason(request.getRejectionReason());
        currestaurant.setStatus(RestaurantStatus.REJECTED);
        Restaurant restaurant=restaurantRepository.save(currestaurant);
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getStatus(),
                restaurant.getOwner().getId(),
                restaurant.getRejectionReason()
        );
    }
    public RestaurantResponseDTO getMyRestaurantReview(Long restaurantId){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User owner=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Owner Not Found"));
        Restaurant restaurant=restaurantRepository.findById(restaurantId).orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("You cannot view this restaurant's review");
        }
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getStatus(),
                restaurant.getOwner().getId(),
                restaurant.getRejectionReason()
        );
    }
    public List<RestaurantPublicResponseDTO> getActiveRestaurants(){
        List<Restaurant> restaurants=restaurantRepository.findByStatus(RestaurantStatus.ACTIVE);
        return restaurants.stream().map(restaurant->new RestaurantPublicResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getStatus()
        )).toList();
    }
    public Page<RestaurantPublicResponseDTO> getActiveRestaurantsByCity(String city,int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantAddress> addresses=restaurantAddressRepository.findByCityIgnoreCaseAndRestaurant_Status(RestaurantStatus.ACTIVE,city,pageable);
        return addresses.map(address -> new RestaurantPublicResponseDTO(
                       address.getRestaurant().getId(),
                        address.getRestaurant().getName(),
                        address.getRestaurant().getDescription(),
                        address.getRestaurant().getPhone(),
                        address.getRestaurant().getStatus()
                ));
    }
    public Page<RestaurantPublicResponseDTO> searchActiveRestaurantsPaginated(String name,int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant>restaurants=restaurantRepository.findByNameContainingIgnoreCaseAndStatus(name,RestaurantStatus.ACTIVE,pageable);
        return restaurants.map(restaurant->new RestaurantPublicResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getStatus()
        ));
    }
    public Page<RestaurantPublicResponseDTO> getActiveRestaurantsPaginated(int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants=restaurantRepository.findByStatus(RestaurantStatus.ACTIVE,pageable);
        return restaurants.map(restaurant->new RestaurantPublicResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getStatus()
        ));
    }
}

