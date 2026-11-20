package com.equipo.myphone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equipo.myphone.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Producto findByCodigoBarras(String codigoBarras);
    List<Producto> findByCategoriaIgnoreCase(String categoria);
    List<Producto> findByModelosCompatiblesContainingIgnoreCase(String texto);
    List<Producto> findByStockDisponibleLessThanEqual(Integer limite);
}