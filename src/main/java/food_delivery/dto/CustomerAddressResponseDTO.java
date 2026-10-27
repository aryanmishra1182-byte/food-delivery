package food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressResponseDTO {
    private Long id;
    private String label;
    private String addressLine;
    private String postalCode;
    private String landmark;
    private String state;
    private String city;
    private boolean isDefault;
    private LocalDateTime createdAt;
}
