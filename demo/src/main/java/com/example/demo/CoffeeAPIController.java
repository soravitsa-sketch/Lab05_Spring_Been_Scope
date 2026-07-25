package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoffeeAPIController {
    
    @GetMapping("/")
    public String HomePage(){
        return "Hello Coffee!";
    }

    private final List<Coffee> coffees;

    public CoffeeAPIController() {
        coffees = new ArrayList<>();
        coffees.add(new Coffee(1, "Espresso", 45.0));
        coffees.add(new Coffee(2, "Latte", 55.0));
    }

    @GetMapping("/coffees")
    @ResponseStatus(HttpStatus.OK)
    public List<Coffee> getCoffees() {
        return coffees;
    }

}

record Coffee(int id, String name, double price) {}
