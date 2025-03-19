package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.DeliveryStatus;
import org.example.black_sea_walnut.enums.DeliveryType;
import org.example.black_sea_walnut.enums.OrderStatus;
import org.example.black_sea_walnut.enums.PaymentType;

import java.time.LocalDate;
import java.util.List;

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
    @Column(length = 50)
    private String fio;
    private String email;
    private String phone;
    private int countProducts;
    private int totalPrice;
    private String companyDelivery;
    private String personNameDelivery;
    private String emailDelivery;
    private String phoneDelivery;
    private String addressDelivery;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private DeliveryType deliveryType;
    private PaymentType paymentType;
    private boolean isPayed;
    private LocalDate dateOfOrdering;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderDetail> orderDetails;
    @ManyToMany
    private List<DeliveryPrice> deliveryPrices;
    @ManyToOne
    private User user;

    public Order(LocalDate dateOfOrdering, boolean isPayed, int totalPrice, int countProducts) {
        this.dateOfOrdering = dateOfOrdering;
        this.isPayed = isPayed;
        this.totalPrice = totalPrice;
        this.countProducts = countProducts;
    }
}
