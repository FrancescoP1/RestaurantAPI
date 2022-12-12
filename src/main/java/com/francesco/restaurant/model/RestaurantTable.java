package com.francesco.restaurant.model;

import com.francesco.restaurant.constants.TableConstants;
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
@Table(name = "TABLES")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(strategy = "native", name = "native")
    private int tableId;
    private int capacity;
    private String status;

    @OneToMany(targetEntity = Order.class, cascade = CascadeType.PERSIST,
                fetch = FetchType.EAGER, mappedBy = "orderTable")
    private Set<Order> orders;
}
