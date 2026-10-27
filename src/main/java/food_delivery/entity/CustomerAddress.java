package food_delivery.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "customer_address")
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;
    @NotBlank
    private String label;
    @NotBlank
    @Column(nullable = false, length = 100)
    private String addressLine;
    private String landmark;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String postalCode;
    private boolean isDefault=false;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
    }
}
