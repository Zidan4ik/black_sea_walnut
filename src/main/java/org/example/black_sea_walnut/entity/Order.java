package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.Country;
import org.example.black_sea_walnut.enums.DeliveryStatus;
import org.example.black_sea_walnut.enums.OrderStatus;
import org.example.black_sea_walnut.enums.Region;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long personalId;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private boolean isPay;
    private String fio;
    private String email;
    private String phone;
    private LocalDateTime dateOrdering;
    private int countProducts;
    private String companyDelivery;
    private String contactPersonNameDelivery;
    private String emailDelivery;
    private String phoneDelivery;
    private Country countryDelivery;
    private Region regionDelivery;
    private String addressDelivery;
    private int summaWithoutDiscount;
    private int summaWithDiscount;
}
