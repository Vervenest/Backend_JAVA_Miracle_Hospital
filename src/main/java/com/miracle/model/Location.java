package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "locationId", nullable = false, length = 64, unique = true)
    private String locationId;

    @Column(name = "locationName", nullable = false, length = 150)
    private String locationName;

    @Column(name = "locationStatus", length = 5)
    @Builder.Default
    private String locationStatus = "1";

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @PrePersist
    protected void onCreate() {
        createDatetime = LocalDateTime.now();
        updateDatetime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDatetime = LocalDateTime.now();
    }
}