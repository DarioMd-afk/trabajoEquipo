package com.equipo.myphone.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String manejarErrores(Exception e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError", "Faltan campos obligatorios o los datos ingresados no son válidos.");
        String paginaAnterior = request.getHeader("Referer");
        return "redirect:" + (paginaAnterior != null ? paginaAnterior : "/admin");
    }
}