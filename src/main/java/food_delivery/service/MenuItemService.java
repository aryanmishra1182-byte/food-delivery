package food_delivery.service;

import food_delivery.dto.MenuItemCreateRequestDTO;
import food_delivery.dto.MenuItemResponseDTO;
import food_delivery.entity.*;
import food_delivery.exception.*;
import food_delivery.repository.MenuCategoryRepository;
import food_delivery.repository.MenuItemRepository;
import food_delivery.repository.RestaurantRepository;
import food_delivery.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuItemService {
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    public MenuItemService(MenuCategoryRepository menuCategoryRepository, MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }
    public MenuItemResponseDTO createMenuItem(MenuItemCreateRequestDTO request){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Authentication required");
        }
        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Owner Not Found"));

        MenuCategory category = menuCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category Not Found"));
        Restaurant restaurant=restaurantRepository.findById(category.getRestaurant().getId())
                .orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException(
                    "You cannot manage menu items for this restaurant");
        }
        if(restaurant.getStatus()!= RestaurantStatus.ACTIVE)
            throw new IllegalStateException("Restaurant Inactive");
        String itemName = request.getName().trim();
        if(menuItemRepository.existsByNameIgnoreCaseAndMenuCategory_Id(itemName,request.getCategoryId())){
            throw new MenuItemAlreadyExistsException("Menu Item Already Exists");
        }
        MenuItem item=new MenuItem();
        item.setDescription(request.getDescription());
        item.setImageUrl(request.getImageUrl());
        item.setMenuCategory(category);
        item.setName(itemName);
        item.setPrice(request.getPrice());
        item.setAvailable(true);
        MenuItem savedItem=menuItemRepository.save(item);
        return new MenuItemResponseDTO(
                savedItem.getId(),
                savedItem.getName(),
                savedItem.getDescription(),
                savedItem.getPrice(),
                savedItem.getImageUrl(),
                savedItem.isAvailable(),
                savedItem.getMenuCategory().getId(),
                savedItem.getCreatedAt(),
                savedItem.getUpdatedAt()
        );
    }
public List<MenuItemResponseDTO> getMenuItemsByCategory(Long categoryId){
    MenuCategory category = menuCategoryRepository.findById(categoryId)
            .orElseThrow(() ->
                    new CategoryNotFoundException("Category Not Found"));
    Restaurant restaurant=restaurantRepository.findById(category.getRestaurant().getId())
            .orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
    if(restaurant.getStatus()!= RestaurantStatus.ACTIVE)
        throw new IllegalStateException("Restaurant Inactive");
    List<MenuItem> items=menuItemRepository.findByMenuCategory_IdAndIsAvailableTrue(categoryId);
    return items.stream().map(
            item->new MenuItemResponseDTO(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getImageUrl(),
                    item.isAvailable(),
                    item.getMenuCategory().getId(),
                    item.getCreatedAt(),
                    item.getUpdatedAt()
            )
    ).toList();
}
    public List<MenuItemResponseDTO> getOwnerMenuItemsByCategory(Long categoryId){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Authentication required");
        }
        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Owner Not Found"));
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category Not Found"));
        Restaurant restaurant=restaurantRepository.findById(category.getRestaurant().getId())
                .orElseThrow(()->new RestaurantNotFoundException("Restaurant Not Found"));
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException(
                    "You cannot manage menu items for this restaurant");
        }
        List<MenuItem> items=menuItemRepository.findByMenuCategory_Id(categoryId);
        return items.stream().map(
                item->new MenuItemResponseDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getImageUrl(),
                        item.isAvailable(),
                        item.getMenuCategory().getId(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()
                )
        ).toList();
    }

    public MenuItemResponseDTO updateMenuItem(
            Long itemId,
            Long currentCategoryId,
            MenuItemCreateRequestDTO request) {

        // 1. Authenticate the user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Authentication required");
        }

        // 2. Find the authenticated owner
        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Owner Not Found"));

        // 3. Find the existing item in its current category
        MenuItem item = menuItemRepository
                .findByIdAndMenuCategory_Id(itemId, currentCategoryId)
                .orElseThrow(() ->
                        new MenuItemNotFoundException("Menu Item Not Found"));

        // 4. Find the existing restaurant
        Restaurant restaurant = item.getMenuCategory().getRestaurant();

        // 5. Verify ownership
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException(
                    "You cannot manage menu items for this restaurant");
        }

        // 6. Ensure the restaurant is active
        if (restaurant.getStatus() != RestaurantStatus.ACTIVE) {
            throw new IllegalStateException("Restaurant Inactive");
        }

        // 7. Find the target category
        MenuCategory targetCategory = menuCategoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Target Category Not Found"));

        // 8. Ensure the target category belongs to the same restaurant
        if (!targetCategory.getRestaurant().getId()
                .equals(restaurant.getId())) {
            throw new AccessDeniedException(
                    "Cannot move an item to another restaurant's category");
        }

        // 9. Normalize the name and check for duplicates
        String itemName = request.getName().trim();

        boolean duplicateExists =
                menuItemRepository
                        .existsByNameIgnoreCaseAndMenuCategory_IdAndIdNot(
                                itemName,
                                targetCategory.getId(),
                                itemId
                        );

        if (duplicateExists) {
            throw new MenuItemAlreadyExistsException(
                    "Menu Item Already Exists");
        }

        // 10. Update the existing entity
        item.setName(itemName);
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        item.setMenuCategory(targetCategory);

        // 11. Save the updated entity
        MenuItem savedItem = menuItemRepository.save(item);

        // 12. Return the response DTO
        return new MenuItemResponseDTO(
                savedItem.getId(),
                savedItem.getName(),
                savedItem.getDescription(),
                savedItem.getPrice(),
                savedItem.getImageUrl(),
                savedItem.isAvailable(),
                savedItem.getMenuCategory().getId(),
                savedItem.getCreatedAt(),
                savedItem.getUpdatedAt()
        );
    }

    public void deleteItem(Long itemId, Long categoryId) {

        // 1. Authenticate the user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Authentication required");
        }

        // 2. Find the authenticated owner
        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Owner Not Found"));

        // 3. Find the item in the specified category
        MenuItem item = menuItemRepository
                .findByIdAndMenuCategory_Id(itemId, categoryId)
                .orElseThrow(() ->
                        new MenuItemNotFoundException("Menu Item Not Found"));

        // 4. Get the restaurant associated with the item
        Restaurant restaurant = item.getMenuCategory().getRestaurant();

        // 5. Verify restaurant ownership
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException(
                    "You cannot manage menu items for this restaurant");
        }

        // 6. Ensure the restaurant is active
        if (restaurant.getStatus() != RestaurantStatus.ACTIVE) {
            throw new IllegalStateException("Restaurant Inactive");
        }

        // 7. Delete the item
        menuItemRepository.delete(item);
    }
}
