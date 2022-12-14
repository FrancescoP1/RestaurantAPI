package com.francesco.restaurant.service;

import com.francesco.restaurant.constants.TableConstants;
import com.francesco.restaurant.exception.BusinessException;
import com.francesco.restaurant.exception.ObjectNotFoundException;
import com.francesco.restaurant.model.Response;
import com.francesco.restaurant.model.RestaurantTable;
import com.francesco.restaurant.repository.TableRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableService {
    @Autowired
    private TableRepository tableRepository;

    public RestaurantTable validateTable(int tableId) throws ObjectNotFoundException, BusinessException {
        Optional<RestaurantTable> tableToValidate = tableRepository.findById(tableId);
        Response response = new Response();
        if(!(tableToValidate.isPresent() && tableToValidate.get().getTableId() > 0)) {
            response.setMessage("Table id: " + tableId + " not found!");
            response.setStatusCode(404);
            throw new ObjectNotFoundException(response);
        } else if (!Objects.equals(tableToValidate.get().getStatus(), TableConstants.AVAILABLE)) {
            response.setMessage("Table id: " + tableId + " is already RESERVED!");
            response.setStatusCode(400);
            throw new BusinessException(response);
        }
        return tableToValidate.get();
    }
}
