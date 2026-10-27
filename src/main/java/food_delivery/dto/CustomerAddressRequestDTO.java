package food_delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressRequestDTO {
    @NotBlank
    @Size(max = 50)
    private String label;
    @NotBlank
    @Size(max = 100)
    private String addressLine;
    @NotBlank
    @Size(max = 20)
    private String postalCode;
    @Size(max = 100)
    private String landmark;
    @NotBlank
    @Size(max = 100)
    private String state;
    @NotBlank
    @Size(max = 100)
    private String city;
    private boolean isDefault;

}
