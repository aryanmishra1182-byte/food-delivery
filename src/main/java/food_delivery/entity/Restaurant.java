package food_delivery.entity;

import jakarta.persistence.*;
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
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String phone;
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;
    @ManyToOne
    @JoinColumn(name = "owner_id",nullable = false)
    private User owner;
    @Column(length = 500)
    private String rejectionReason;
    @ManyToOne
    @JoinColumn(name="reviewed_by")
    private User reviewedBy;
    private LocalDateTime reviewedAt;
}