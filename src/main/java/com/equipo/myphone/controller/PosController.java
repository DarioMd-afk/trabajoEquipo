package com.equipo.myphone.controller;

import com.equipo.myphone.model.Producto;
import com.equipo.myphone.model.Venta;
import com.equipo.myphone.repository.ProductoRepository;
import com.equipo.myphone.repository.VentaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class PosController {

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final List<Producto> carrito = new ArrayList<>();

    public PosController(ProductoRepository productoRepository, VentaRepository ventaRepository) {
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
    }

    @GetMapping
    public String verCarrito(@RequestParam(required = false) String sku, Model model) {
        if (sku != null && !sku.isBlank()) {
            Producto p = productoRepository.findByCodigoBarras(sku);
            if (p != null && p.getStockDisponible() > 0) carrito.add(p);
        }
        double subtotal = carrito.stream().mapToDouble(Producto::getPrecioBase).sum();
        double descuento = carrito.size() >= 2 ? subtotal * 0.15 : 0.0;
        double total = subtotal - descuento;

        model.addAttribute("carrito", carrito);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuento", descuento);
        model.addAttribute("total", total);
        model.addAttribute("ventasRecientes", ventaRepository.findAll());
        return "carrito";
    }

    @PostMapping("/agregar")
    public String agregarPorSku(@RequestParam String codigoBarras) {
        Producto p = productoRepository.findByCodigoBarras(codigoBarras.trim());
        if (p != null && p.getStockDisponible() > 0) carrito.add(p);
        return "redirect:/carrito";
    }

    @PostMapping("/cobrar")
    public String cobrar() {
        if (!carrito.isEmpty()) {
            double subtotal = carrito.stream().mapToDouble(Producto::getPrecioBase).sum();
            double descuento = carrito.size() >= 2 ? subtotal * 0.15 : 0.0;
            StringBuilder detalle = new StringBuilder();

            for (Producto item : carrito) {
                Producto dbProd = productoRepository.findById(item.getId()).orElse(null);
                if (dbProd != null && dbProd.getStockDisponible() > 0) {
                    dbProd.setStockDisponible(dbProd.getStockDisponible() - 1);
                    productoRepository.save(dbProd);
                }
            detalle.append(item.getNombre()).append(" [SKU: ").append(item.getCodigoBarras()).append("], ");
            }

            Venta v = new Venta();
            v.setDetalleProductos(detalle.toString());
            v.setSubtotal(subtotal);
            v.setDescuento(descuento);
            v.setTotal(subtotal - descuento);
            ventaRepository.save(v);
            carrito.clear();
        }
        return "redirect:/carrito";
    }

    @PostMapping("/vaciar")
    public String vaciar() {
        carrito.clear();
        return "redirect:/carrito";
    }
}