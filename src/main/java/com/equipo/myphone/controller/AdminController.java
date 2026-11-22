package com.equipo.myphone.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.equipo.myphone.model.Marca;
import com.equipo.myphone.model.Producto;
import com.equipo.myphone.repository.MarcaRepository;
import com.equipo.myphone.repository.ProductoRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;

    public AdminController(ProductoRepository productoRepository, MarcaRepository marcaRepository) {
        this.productoRepository = productoRepository;
        this.marcaRepository = marcaRepository;
    }

    @GetMapping
    public String panelAdmin(Model model) {
        // Si la tabla de marcas esta vacia, carga las marcas base automaticamente
        if (marcaRepository.count() == 0) {
            for (String nombre : List.of("Apple", "Samsung", "Xiaomi", "Motorola", "Huawei", "Honor", "Oppo", "Universal")) {
                Marca m = new Marca();
                m.setNombre(nombre);
                marcaRepository.save(m);
            }
        }

        var productos = productoRepository.findAll();
        var stockBajo = productoRepository.findByStockDisponibleLessThanEqual(5);
        model.addAttribute("productos", productos);
        model.addAttribute("marcas", marcaRepository.findAll());
        model.addAttribute("totalStockBajo", stockBajo.size());
        model.addAttribute("nuevoProducto", new Producto());
        return "admin";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoRepository.save(producto);
        return "redirect:/admin";
    }

    @PostMapping("/productos/reabastecer")
    public String reabastecer(@RequestParam Long id, @RequestParam Integer cantidad) {
        Producto p = productoRepository.findById(id).orElse(null);
        if (p != null && cantidad > 0) {
            p.setStockDisponible(p.getStockDisponible() + cantidad);
            productoRepository.save(p);
        }
        return "redirect:/admin";
    }

    @PostMapping("/marcas/guardar")
    public String guardarMarca(@RequestParam String nombreMarca) {
        String limpia = nombreMarca.trim();
        if (!limpia.isEmpty() && !marcaRepository.existsByNombreIgnoreCase(limpia)) {
            Marca nueva = new Marca();
            nueva.setNombre(limpia);
            marcaRepository.save(nueva);
        }
        return "redirect:/admin";
    }
}