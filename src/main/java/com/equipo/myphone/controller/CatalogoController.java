package com.equipo.myphone.controller;

import com.equipo.myphone.repository.MarcaRepository;
import com.equipo.myphone.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogoController {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;

    public CatalogoController(ProductoRepository productoRepository, MarcaRepository marcaRepository) {
        this.productoRepository = productoRepository;
        this.marcaRepository = marcaRepository;
    }

    @GetMapping("/")
    public String inicio(@RequestParam(required = false) String categoria, Model model) {
        var productos = (categoria != null && !categoria.isBlank())
                ? productoRepository.findByCategoriaIgnoreCase(categoria)
                : productoRepository.findAll();
        model.addAttribute("productos", productos);
        model.addAttribute("categoriaActual", categoria);
        return "inicio";
    }

    @GetMapping("/compatibilidad")
    public String compatibilidad(@RequestParam(required = false) String modelo, Model model) {
        var resultados = (modelo != null && !modelo.isBlank())
                ? productoRepository.findByModelosCompatiblesContainingIgnoreCase(modelo)
                : productoRepository.findAll();
        model.addAttribute("productos", resultados);
        model.addAttribute("marcas", marcaRepository.findAll());
        model.addAttribute("modeloBuscado", modelo);
        return "compatibilidad";
    }
}