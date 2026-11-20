package com.equipo.myphone.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigoBarras; // Ej: SKU-1042

    @Column(nullable = false)
    private String nombre;       // Ej: Funda MagSafe

    @Column(nullable = false)
    private String categoria;    // Fundas, Micas, Cargadores, Cables

    @Column(nullable = false)
    private Double precioBase;

    @Column(nullable = false)
    private Integer stockDisponible;

    private String modelosCompatibles; // Ej: Apple iPhone 15, Apple iPhone 15 Pro
}