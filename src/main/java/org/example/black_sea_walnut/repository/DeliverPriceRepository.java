package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.DeliveryPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliverPriceRepository  extends JpaRepository<DeliveryPrice, Long> {
}
