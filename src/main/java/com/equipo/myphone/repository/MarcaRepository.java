package com.equipo.myphone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equipo.myphone.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}