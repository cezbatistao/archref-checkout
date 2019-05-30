package com.cit.checkout.gateway.mysql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase")
public class PurchaseModel implements Serializable{

    private static final long serialVersionUID = -2847258243104084133L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @OneToOne
    @JoinColumn(name="product_id", nullable = false)
    private ProductModel product;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderModel order;

}
