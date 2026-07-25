package com.example.demo.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Coffee;
import com.example.demo.service.CoffeeService;

@RestController
@RequestMapping("/coffees")
public class CoffeeAPIController {
    
    private final CoffeeService service;

    public CoffeeAPIController(CoffeeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Coffee> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coffee> getById(@PathVariable Long id) {

        Coffee coffee = service.getById(id);

        if (coffee == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(coffee);
    }

    @PostMapping
    public ResponseEntity<Coffee> add(@RequestBody Coffee coffee) {

        Coffee created = service.add(coffee);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coffee> update(
            @PathVariable Long id,
            @RequestBody Coffee coffee) {

        Coffee updated = service.update(id, coffee);

        if (updated == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        boolean deleted = service.delete(id);

        if (!deleted)
            return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Coffee>> searchByName(@RequestParam String name) {

    List<Coffee> results = service.searchByName(name);

    return ResponseEntity.ok(results);
    }

}
