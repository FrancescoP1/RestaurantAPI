package com.francesco.restaurant.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(strategy = "native", name = "native")
    private int orderId;
    @ManyToOne(targetEntity = RestaurantTable.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "TABLE_ID", referencedColumnName = "tableId", nullable = false)
    private RestaurantTable orderTable;
    private Timestamp orderDate;

    @ManyToMany(targetEntity = MenuItem.class, fetch = FetchType.EAGER,
                mappedBy = "orders", cascade = CascadeType.PERSIST)
    private Set<MenuItem> orderItems;

}
