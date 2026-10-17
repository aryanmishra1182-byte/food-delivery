package food_delivery.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAddressRequestDTO {
    @NotBlank
   private String addressLine;
  private 	String   landmark;
  @NotBlank
   private 	String  city;
  @NotBlank
  private  	String  state;
  @Pattern(regexp = "[0-9]{6}$",message = "Postal Code Must Be Of 6 Digits")
  @NotBlank
   private 	String postalCode;
  @DecimalMin(value="-90")
  @DecimalMax(value = "90")
    private BigDecimal latitude;
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    private BigDecimal longitude;
}
