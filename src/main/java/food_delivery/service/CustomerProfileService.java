package food_delivery.service;
import food_delivery.dto.CustomerProfileResponseDTO;
import food_delivery.dto.CustomerProfileUpdateRequestDTO;
import food_delivery.entity.User;
import food_delivery.exception.UserNotFoundException;
import food_delivery.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerProfileService {
    private final UserRepository userRepository;

    public CustomerProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                .orElseThrow(() ->
                        new UserNotFoundException("Customer Not Found!"));
    }
    private CustomerProfileResponseDTO toResponseDTO(User user) {
        return new CustomerProfileResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }
    public CustomerProfileResponseDTO getMyProfile(){
        User user=getAuthenticatedUser();
        return toResponseDTO(user
        );
    }
    public CustomerProfileResponseDTO updateMyProfile(CustomerProfileUpdateRequestDTO request){
        User user=getAuthenticatedUser();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        User savedUser=userRepository.save(user);
        return toResponseDTO(savedUser);
    }
}
