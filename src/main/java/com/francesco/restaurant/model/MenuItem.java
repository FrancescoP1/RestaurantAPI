package com.francesco.restaurant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MENU_ITEMS")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(strategy = "native", name = "native")
    private int itemId;
    private String name;
    private String description;

    @ManyToMany(targetEntity = Order.class, fetch = FetchType.EAGER,
                cascade = CascadeType.PERSIST)
    @JoinTable(name = "ORDER_ITEMS",
                joinColumns = {@JoinColumn(name = "ITEM_ID", referencedColumnName = "itemId")},
                inverseJoinColumns = {@JoinColumn(name = "ORDER_ID", referencedColumnName = "orderId")})
    private Set<Order> orders;
}
