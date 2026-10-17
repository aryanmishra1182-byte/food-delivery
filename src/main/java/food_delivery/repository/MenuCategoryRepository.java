package food_delivery.repository;

import food_delivery.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory,Long> {
    List<MenuCategory> findByRestaurant_Id(Long restaurantId);
//    boolean existsByNameAndRestaurant_Id(String name,Long restaurantId);
//    boolean existsByNameAndRestaurant_IdAndIdNot(
//            String name,
//            Long restaurantId,
//            Long categoryId
//    );
    boolean existsByNameIgnoreCaseAndRestaurant_Id(
            String name,
            Long restaurantId
    );

    boolean existsByNameIgnoreCaseAndRestaurant_IdAndIdNot(
            String name,
            Long restaurantId,
            Long categoryId
    );
}
