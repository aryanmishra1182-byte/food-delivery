package food_delivery.dto;

import food_delivery.entity.RestaurantStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class RestaurantResponseDTO {
   private Long id ;
      private String      name;
   private String description;
      private String      phone;
   private RestaurantStatus status;
   private Long ownerId;
   private String rejectionReason;
}
