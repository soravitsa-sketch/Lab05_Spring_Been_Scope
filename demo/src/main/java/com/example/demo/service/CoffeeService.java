package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Coffee;

@Service
public class CoffeeService {

    private final List<Coffee> coffees = new ArrayList<>();
    private Long nextId = 3L;

    public CoffeeService() {
        coffees.add(new Coffee(1L, "Espresso", 45.0));
        coffees.add(new Coffee(2L, "Latte", 55.0));
    }

    public List<Coffee> getAll() {
        return coffees;
    }

    public Coffee getById(Long id) {
        return coffees.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Coffee add(Coffee coffee) {
        coffee.setId(nextId++);
        coffees.add(coffee);
        return coffee;
    }

    public Coffee update(Long id, Coffee newCoffee) {

        Coffee coffee = getById(id);

        if (coffee == null)
            return null;

        coffee.setName(newCoffee.getName());
        coffee.setPrice(newCoffee.getPrice());

        return coffee;
    }

    public boolean delete(Long id) {

        Coffee coffee = getById(id);

        if (coffee == null)
            return false;

        coffees.remove(coffee);
        return true;
    }
}

