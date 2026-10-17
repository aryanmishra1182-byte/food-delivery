package food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantAddressResponseDTO {
  private  Long id;
  private  Long restaurantId;
  private  String addressLine;
  private  String landmark;
  private  String city;
  private  String state;
  private  String postalCode;
  private  BigDecimal latitude;
   private BigDecimal longitude;
}
