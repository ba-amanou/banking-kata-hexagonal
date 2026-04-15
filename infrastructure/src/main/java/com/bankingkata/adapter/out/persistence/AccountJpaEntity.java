package com.bankingkata.adapter.out.persistence;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountJpaEntity {
    @Id
    private String id;
    
    @Column(nullable = false)
    private double balance;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<TransactionJpaEntity> transactions;

}
