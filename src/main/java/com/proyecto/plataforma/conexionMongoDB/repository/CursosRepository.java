package com.proyecto.plataforma.conexionMongoDB.repository;

import com.proyecto.plataforma.capaNegocio.Cursos;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CursosRepository extends MongoRepository<Cursos, String> {
        List<Cursos> findByTema(String tema);
        List<Cursos> findByProfesor(String profesorId);
        List<Cursos> findByTemaAndProfesor(String tema, String profesor);
        Cursos findByTitulo(String titulo);
}
//Final version
