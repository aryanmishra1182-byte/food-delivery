package food_delivery.service;

import food_delivery.dto.UserRequestDTO;
import food_delivery.dto.UserResponseDTO;
import food_delivery.dto.UserUpdateDTO;
import food_delivery.entity.Role;
import food_delivery.entity.Status;
import food_delivery.entity.User;
import food_delivery.exception.UserAlreadyExistsException;
import food_delivery.exception.UserNotFoundException;
import food_delivery.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO){
        String email=userRequestDTO.getEmail();
        User user=new User();
        if(userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException("User Already Exists");
        user.setEmail(userRequestDTO.getEmail());
        user.setName(userRequestDTO.getName());
        String encodedPassword= passwordEncoder.encode(userRequestDTO.getPassword());
        user.setPassword(encodedPassword);
        user.setPhone(userRequestDTO.getPhone());
        user.setRole(Role.CUSTOMER);
        user.setStatus(Status.ACTIVE);
        User savedUser=userRepository.save(user);
        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole(),
                savedUser.getStatus()
        );

    }
    public UserResponseDTO getUserById(Long id){
        User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus()
        );
    }
    public List<UserResponseDTO> getAllUsers(){
List<User> users=userRepository.findAll();
return users.stream().map(user->new UserResponseDTO(
user.getId(),
        user.getName(),
        user.getEmail(),
        user.getPhone(),
        user.getRole(),
        user.getStatus()
)).toList();
    }

    public UserResponseDTO updateUser(UserUpdateDTO update,Long id) {
        User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));
        user.setName(update.getName());
        user.setPhone(update.getPhone());
        User updatedUser=userRepository.save(user);
        return new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getRole(),
                updatedUser.getStatus()

        );
    }

    public void deactivateUser(Long id) {
        User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
    }
}
