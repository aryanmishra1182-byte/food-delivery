package food_delivery.controller;

import food_delivery.dto.UserRequestDTO;
import food_delivery.dto.UserResponseDTO;
import food_delivery.dto.UserUpdateDTO;
import food_delivery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
@PostMapping("/users")
    public ResponseEntity<UserResponseDTO>createUser(@Valid  @RequestBody UserRequestDTO requestDTO){
UserResponseDTO newUser=userService.saveUser(requestDTO);
return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
}
@GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO>getUserById(@PathVariable Long id){
        UserResponseDTO user=userService.getUserById(id);
return ResponseEntity.ok(user);
}
@GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>>getAllUsers(){
        List<UserResponseDTO> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
}
@PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO>updateUser(@PathVariable Long id,@Valid @RequestBody UserUpdateDTO update){
        UserResponseDTO updatedUser=userService.updateUser(update,id);
        return ResponseEntity.ok(updatedUser);
}
@DeleteMapping("/users/{id}")
    public ResponseEntity<String>deactivateUser(@PathVariable Long id){
        userService.deactivateUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Deactivated");
}
}
