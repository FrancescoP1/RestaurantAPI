package com.francesco.restaurant.service;

import com.francesco.restaurant.exception.ObjectNotFoundException;
import com.francesco.restaurant.model.MenuItem;
import com.francesco.restaurant.model.Response;
import com.francesco.restaurant.repository.MenuItemRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem validateItem(int itemId) throws ObjectNotFoundException {
        Optional<MenuItem> menuItem = menuItemRepository.findById(itemId);
        Response response = new Response();
        if(!(menuItem.isPresent() && menuItem.get().getItemId() > 0)) {
            response.setMessage("Item id: " + itemId + " not found!");
            response.setStatusCode(404);
            throw new ObjectNotFoundException(response);
        }
        return menuItem.get();
    }
}
