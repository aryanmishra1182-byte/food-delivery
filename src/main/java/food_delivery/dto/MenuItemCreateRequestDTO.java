package food_delivery.dto;
import food_delivery.entity.MenuCategory;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemCreateRequestDTO {
    @NotBlank
    @Size(max=150)
    private String name;
    @Size(max = 1000)
    private String description;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    private String imageUrl;
    @NotNull
    private Long categoryId;
}
