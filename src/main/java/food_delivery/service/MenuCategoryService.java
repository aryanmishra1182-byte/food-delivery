package food_delivery.service;

import food_delivery.dto.MenuCategoryCreateRequestDTO;
import food_delivery.dto.MenuCategoryResponseDTO;
import food_delivery.entity.MenuCategory;
import food_delivery.entity.Restaurant;
import food_delivery.entity.RestaurantStatus;
import food_delivery.entity.User;
import food_delivery.exception.CategoryNotFoundException;
import food_delivery.exception.MenuCategoryAlreadyExistsException;
import food_delivery.exception.RestaurantNotFoundException;
import food_delivery.exception.UserNotFoundException;
import food_delivery.repository.MenuCategoryRepository;
import food_delivery.repository.RestaurantRepository;
import food_delivery.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuCategoryService {
private final MenuCategoryRepository menuCategoryRepository;
private final RestaurantRepository restaurantRepository;
private final UserRepository userRepository;

    public MenuCategoryService(MenuCategoryRepository menuCategoryRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }
    private MenuCategoryResponseDTO toResponseDTO(MenuCategory category){
        return new MenuCategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getRestaurant().getId()
        );
    }

public MenuCategoryResponseDTO createCategory(Long restaurantId, MenuCategoryCreateRequestDTO request){
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
                "You cannot manage categories for this restaurant");
    }
    if(restaurant.getStatus()!= RestaurantStatus.ACTIVE)
        throw new IllegalStateException("Restaurant Inactive");
    MenuCategory menuCategory=new MenuCategory();
    String categoryName = request.getName().trim();

    boolean exists =
            menuCategoryRepository
                    .existsByNameIgnoreCaseAndRestaurant_Id(
                            categoryName, restaurantId
                    );

    if (exists) {
        throw new MenuCategoryAlreadyExistsException(
                "Category already exists"
        );
    }
    menuCategory.setDescription(request.getDescription());
    menuCategory.setName(categoryName);
    menuCategory.setRestaurant(restaurant);
   MenuCategory savedCategory= menuCategoryRepository.save(menuCategory);
    return toResponseDTO(savedCategory);
}
public List<MenuCategoryResponseDTO> getCategoriesByRestaurant(Long restaurantId){
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() ->
                    new RestaurantNotFoundException("Restaurant Not Found"));
    if(restaurant.getStatus()!= RestaurantStatus.ACTIVE)
        throw new RestaurantNotFoundException("Restaurant Not Found");
    List<MenuCategory> menuCategories=menuCategoryRepository.findByRestaurant_Id(restaurantId);
    return menuCategories.stream().map(this::toResponseDTO).toList();
}
public MenuCategoryResponseDTO updateCategory(Long restaurantId,Long categoryId,MenuCategoryCreateRequestDTO request){
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
                "You cannot manage categories for this restaurant");
    }
    if(restaurant.getStatus()!= RestaurantStatus.ACTIVE)
        throw new IllegalStateException("Restaurant Inactive");
    MenuCategory category=menuCategoryRepository.findById(categoryId).orElseThrow(()->new CategoryNotFoundException("Category Not Found"));
    if(!category.getRestaurant().getId().equals(restaurantId)){
        throw new CategoryNotFoundException("This Category Does Not Exists In This Restaurant");
    }
    String categoryName = request.getName().trim();

    if (menuCategoryRepository
            .existsByNameIgnoreCaseAndRestaurant_IdAndIdNot(
                    categoryName, restaurantId, categoryId)) {
        throw new MenuCategoryAlreadyExistsException(
                "Category already exists"
        );
    }

    category.setName(categoryName);
    category.setDescription(request.getDescription());
    MenuCategory updatedCategory= menuCategoryRepository.save(category);
    return toResponseDTO(updatedCategory);
}
public void deleteCategory(Long restaurantId,Long categoryId){
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
                "You cannot manage categories for this restaurant");
    }
    if(restaurant.getStatus()!= RestaurantStatus.ACTIVE)
        throw new IllegalStateException("Restaurant Inactive");
    MenuCategory category=menuCategoryRepository.findById(categoryId).orElseThrow(()->new CategoryNotFoundException("Category Not Found"));
    if(!category.getRestaurant().getId().equals(restaurantId)){
        throw new CategoryNotFoundException("This Category Does Not Exists In This Restaurant");
    }
    menuCategoryRepository.delete(category);
}
}
