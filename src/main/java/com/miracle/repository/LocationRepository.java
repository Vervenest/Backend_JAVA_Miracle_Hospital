package com.miracle.repository;

import com.miracle.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByLocationId(String locationId);
    Optional<Location> findByLocationNameIgnoreCase(String locationName);
    List<Location> findByLocationStatusOrderByIdAsc(String status);
}