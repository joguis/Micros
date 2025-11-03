package com.danielblanco.arquitecturasmodernas.microservices.user.controller;

import com.danielblanco.arquitecturasmodernas.microservices.user.model.ZonaVeredal;
import com.danielblanco.arquitecturasmodernas.microservices.user.service.ZonaVeredalService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zonas-veredales")
public class ZonaVeredalController {

    @Autowired
    private ZonaVeredalService zonaVeredalService;

    @GetMapping("/")
    public List<ZonaVeredal> findAll() {
        return zonaVeredalService.findAll();
    }

    @GetMapping("/{id}")
    public ZonaVeredal findById(@PathVariable Long id) {
        return zonaVeredalService.findById(id)
            .orElseThrow(() -> new RuntimeException("Zona Veredal no encontrada"));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ZonaVeredal create(@RequestBody ZonaVeredal zonaVeredal) {
        return zonaVeredalService.save(zonaVeredal);
    }
}