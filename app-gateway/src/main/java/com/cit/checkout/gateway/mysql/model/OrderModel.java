package com.cit.checkout.gateway.mysql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_order")
public class OrderModel implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private Long orderNumber;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private BigDecimal value;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PurchaseModel> items;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

}
