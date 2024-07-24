package com.funWithStocks.FunWithStocks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "betsDetails")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "bet_id")
    private Long id;

    @Column(name = "bet_number")
    private Long bet_number;

    @Column(name = "selected_numbers")
    private String selected_stocks;

    @Column(name = "bet_amount")
    private double bet_amount;

    @Column(name = "slots")
    private String slot;

    @CreationTimestamp
    @Column(name = "bet_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp bet_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}
