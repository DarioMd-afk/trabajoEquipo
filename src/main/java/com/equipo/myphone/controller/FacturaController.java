package com.equipo.myphone.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.equipo.myphone.repository.ProductoRepository;
import com.equipo.myphone.repository.VentaRepository;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public FacturaController(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public String historialFacturas(Model model) {
        // Trae las ventas más recientes primero
        var facturas = ventaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        var productos = productoRepository.findAll();

        double totalVendido = facturas.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        model.addAttribute("facturas", facturas);
        model.addAttribute("productos", productos);
        model.addAttribute("totalVendido", totalVendido);
        return "facturas";
    }
}