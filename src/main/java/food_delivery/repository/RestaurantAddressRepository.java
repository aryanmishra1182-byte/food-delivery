package food_delivery.repository;

import food_delivery.entity.RestaurantAddress;
import food_delivery.entity.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RestaurantAddressRepository extends JpaRepository<RestaurantAddress,Long> {
    boolean existsByRestaurant_Id(Long restaurantId);
    Optional<RestaurantAddress> findByRestaurant_Id(Long restaurantId);
    Page<RestaurantAddress> findByCityIgnoreCaseAndRestaurant_Status(RestaurantStatus status,String city, Pageable pageable);
}
