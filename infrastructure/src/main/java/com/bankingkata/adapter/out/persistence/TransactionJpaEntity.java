package com.bankingkata.adapter.out.persistence;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionJpaEntity {
    @Id
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false)
    private LocalDateTime date;
}
