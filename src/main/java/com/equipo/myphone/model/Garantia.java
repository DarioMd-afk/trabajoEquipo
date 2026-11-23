package com.equipo.myphone.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "garantias")
public class Garantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;
    private String codigoProducto;
    private String motivoFalla;
    private String estado = "En revisión";
    private LocalDate fechaRegistro = LocalDate.now();
}