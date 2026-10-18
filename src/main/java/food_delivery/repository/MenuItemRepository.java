package food_delivery.repository;

import food_delivery.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {
    List<MenuItem> findByMenuCategory_Id(Long categoryId);
    boolean existsByNameIgnoreCaseAndMenuCategory_Id(String name,Long categoryId);
    Optional<MenuItem> findByIdAndMenuCategory_Id(Long itemId,Long categoryId);
    List<MenuItem> findByMenuCategory_IdAndIsAvailableTrue(Long categoryId);
    boolean existsByNameIgnoreCaseAndMenuCategory_IdAndIdNot(
            String name, Long categoryId, Long itemId);
}
