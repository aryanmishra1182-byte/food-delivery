package food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuCategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long restaurantId;
}
