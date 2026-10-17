package food_delivery.dto;

import food_delivery.entity.RestaurantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPublicResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String phone;
    private RestaurantStatus status;
}
