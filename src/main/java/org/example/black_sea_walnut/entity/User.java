package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.*;
import org.hibernate.bytecode.enhance.spi.interceptor.AbstractLazyLoadInterceptor;

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
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private Country country;
    private Region region;
    private City city;
    private String address;
    private String password;
    private String pathToImage;
    private String okpo;
    private Country countryLawful;
    private Region regionLawful;
    private City cityLawful;
    private String addressLawful;
    private LocalDate dateRegistered;
    private RegisterType registerType;
    private boolean isFop;
    private Role role;
    private int department;
    @ManyToOne
    private Manager manager;
    @OneToMany
    private List<Order> orders;
}
