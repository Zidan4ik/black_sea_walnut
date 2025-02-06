package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone1;
    private String phone2;
    private String email;
    private String addressWork;
    private String addressFactory;
    private String coordinates;
    private String telegram;
    private String viber;
    private String whatsapp;
    private String facebook;
    private String instagram;
    private String youtube;
    private LocalDateTime timeUpdated;
}
