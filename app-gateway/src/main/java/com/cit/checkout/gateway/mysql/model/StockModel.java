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
@Table(name = "stock")
public class StockModel implements Serializable {

    private static final long serialVersionUID = 4158873538380694739L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(name="product_id", nullable = false)
    private ProductModel product;

    @Column(nullable = false)
    private Integer total;

}
