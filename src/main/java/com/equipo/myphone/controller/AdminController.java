package com.equipo.myphone.controller;

import com.equipo.myphone.model.Marca;
import com.equipo.myphone.model.Producto;
import com.equipo.myphone.repository.MarcaRepository;
import com.equipo.myphone.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        if (marcaRepository.count() == 0) {
            List<String> marcasBase = List.of("Apple", "Samsung", "Xiaomi", "Motorola", "Huawei", "Honor", "Oppo", "Universal");
            for (String nombre : marcasBase) {
                Marca m = new Marca();
                m.setNombre(nombre);
                marcaRepository.save(m);
            }
        }

        List<Producto> productos = productoRepository.findAll();
        long totalStockBajo = productos.stream()
                .filter(p -> p.getStockDisponible() != null && p.getStockDisponible() <= 5)
                .count();

        model.addAttribute("productos", productos);
        model.addAttribute("marcas", marcaRepository.findAll());
        model.addAttribute("totalStockBajo", totalStockBajo);
        model.addAttribute("nuevoProducto", new Producto());
        return "admin";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto nuevoProducto, RedirectAttributes redirectAttributes) {
        try {
            productoRepository.save(nuevoProducto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar: el código SKU ya existe o los datos son inválidos.");
        } finally {
            System.out.println("Finalizó el intento de registro para SKU: " + nuevoProducto.getCodigoBarras());
        }
        return "redirect:/admin";
    }

    @PostMapping("/productos/reabastecer")
    public String reabastecerProducto(@RequestParam Long id, @RequestParam Integer cantidad, RedirectAttributes redirectAttributes) {
        try {
            Producto p = productoRepository.findById(id).orElseThrow();
            p.setStockDisponible(p.getStockDisponible() + cantidad);
            productoRepository.save(p);
            redirectAttributes.addFlashAttribute("mensajeExito", "Stock actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar el stock.");
        } finally {
            System.out.println("Finalizó el intento de reabastecimiento para ID: " + id);
        }
        return "redirect:/admin";
    }

    @PostMapping("/marcas/guardar")
    public String guardarMarca(@RequestParam String nombreMarca, RedirectAttributes redirectAttributes) {
        try {
            Marca nueva = new Marca();
            nueva.setNombre(nombreMarca.trim());
            marcaRepository.save(nueva);
            redirectAttributes.addFlashAttribute("mensajeExito", "Marca agregada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar la marca (puede que ya exista).");
        } finally {
            System.out.println("Finalizó el intento de registro de marca: " + nombreMarca);
        }
        return "redirect:/admin";
    }
}