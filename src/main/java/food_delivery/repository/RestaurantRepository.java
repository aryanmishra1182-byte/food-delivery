package food_delivery.repository;

import food_delivery.entity.Restaurant;
import food_delivery.entity.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long>{
    List<Restaurant> findByStatus(RestaurantStatus status);
    Page<Restaurant> findByNameContainingIgnoreCaseAndStatus(String name,RestaurantStatus status,Pageable pageable);
    Page<Restaurant> findByStatus(RestaurantStatus status, Pageable pageable);
}
