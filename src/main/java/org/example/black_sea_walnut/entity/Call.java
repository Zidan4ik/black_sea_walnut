package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.CallStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private CallStatus status;
    private LocalDateTime registerCall;
    @ManyToOne
    private User user;
}
