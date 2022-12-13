package com.francesco.restaurant.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(strategy = "native", name = "native")
    //@Column(name = "ORDER_ID")
    private int orderId;
    @ManyToOne(targetEntity = RestaurantTable.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "TABLE_ID", referencedColumnName = "tableId", nullable = true)
    private RestaurantTable orderTable;
    private Timestamp orderDate;

    /*@ManyToMany(fetch = FetchType.EAGER,
                mappedBy = "orders", cascade = CascadeType.PERSIST)

     */
    @ManyToMany(targetEntity = MenuItem.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "ORDER_ITEMS",
                joinColumns = {@JoinColumn(name = "ORDER_ID", referencedColumnName = "orderId")},
                inverseJoinColumns = {@JoinColumn(name = "ITEM_ID", referencedColumnName = "itemId")})
    private Set<MenuItem> orderItems = new HashSet<>();

}
