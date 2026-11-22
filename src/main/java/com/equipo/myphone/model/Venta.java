package com.equipo.myphone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String detalleProductos;
    private Double subtotal;
    private Double descuento;
    private Double total;
    private LocalDateTime fecha = LocalDateTime.now();
}
