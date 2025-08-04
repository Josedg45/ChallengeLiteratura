package com.example.demo.repository;

import com.example.demo.entity.AutorEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<AutorEntidad, Long> {
    Optional<AutorEntidad> findByNombre(String nombre);
}
