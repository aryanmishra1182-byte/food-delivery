package food_delivery.repository;

import food_delivery.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress,Long> {
    List<CustomerAddress> findByUser_Id(Long id);
    Optional<CustomerAddress> findByIdAndUser_Id(Long addressId,Long userId);
    boolean existsByUser_IdAndIsDefaultTrue(Long userId);
}
