package com.equipo.myphone.controller;

import com.equipo.myphone.model.Garantia;
import com.equipo.myphone.repository.GarantiaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/garantias")
public class GarantiaController {

    private final GarantiaRepository garantiaRepository;

    public GarantiaController(GarantiaRepository garantiaRepository) {
        this.garantiaRepository = garantiaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("garantias", garantiaRepository.findAll());
        model.addAttribute("nuevaGarantia", new Garantia());
        return "garantias";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Garantia garantia) {
        garantiaRepository.save(garantia);
        return "redirect:/garantias";
    }

    @PostMapping("/estado")
    public String cambiarEstado(@RequestParam Long id, @RequestParam String estado) {
        Garantia g = garantiaRepository.findById(id).orElse(null);
        if (g != null) {
            g.setEstado(estado);
            garantiaRepository.save(g);
        }
        return "redirect:/garantias";
    }
}