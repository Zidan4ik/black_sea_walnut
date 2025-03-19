package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String pathToImage;
    private String paymentDetails;
    private LocalDate dateRegistered;
    @Enumerated(EnumType.STRING)
    private RegisterType registerType;
    private boolean isFop;
    private boolean isEnable;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int department;
    private String address;
    private String addressAdditional;
    private String indexAdditional;
    private String company;
    @ManyToOne
    private Manager manager;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> orders;
    @OneToOne
    private Basket basket;
    @ManyToOne
    @Enumerated(EnumType.STRING)
    private Country country;
    @ManyToOne
    @Enumerated(EnumType.STRING)
    private Region region;
    @ManyToOne
    @Enumerated(EnumType.STRING)
    private City city;
    @ManyToOne
    @Enumerated(EnumType.STRING)
    private Country countryAdditional;
    @ManyToOne
    @Enumerated(EnumType.STRING)
    private Region regionAdditional;
    @ManyToOne
    @Enumerated(EnumType.STRING)
    private City cityAdditional;
}
