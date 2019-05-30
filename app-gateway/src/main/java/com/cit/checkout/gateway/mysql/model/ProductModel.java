package com.cit.checkout.gateway.mysql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class ProductModel implements Serializable {

    private static final long serialVersionUID = 3004703626452808651L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal value;

}
