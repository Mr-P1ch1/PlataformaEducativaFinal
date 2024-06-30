package com.proyecto.plataforma.services;

import com.proyecto.plataforma.data.Cursos;
import com.proyecto.plataforma.data.Estudiante;
import com.proyecto.plataforma.repository.CursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursosService {

    @Autowired
    private CursosRepository cursosRepository;

    public Cursos saveCursos(Cursos cursos) {
        return cursosRepository.save(cursos);
    }

    public Cursos encontrarPorTitulo(String titulo) {
        return cursosRepository.findByTitulo(titulo);
    }

    public Iterable<Cursos> encontrarTodos() {
        return cursosRepository.findAll();
    }

    public void eliminar(Cursos cursos) {
        cursosRepository.delete(cursos);
    }

    public Optional<Cursos> encontrarPorId(String id) {
        return cursosRepository.findById(id);
    }

    public List<Cursos> buscarPorTema(String tema) {
        return cursosRepository.findByTema(tema);
    }

    public List<Cursos> buscarPorProfesor(String profesor) {
        return cursosRepository.findByProfesor(profesor);
    }

    public List<Cursos> buscarPorTemaYProfesor(String tema, String profesor) {
        return cursosRepository.findByTemaAndProfesor(tema, profesor);
    }

    public Cursos agregarAlumnoAlCurso(String cursoId, Estudiante alumno) {
        Cursos curso = cursosRepository.findById(cursoId).orElse(null);
        if (curso != null) {
            curso.agregarAlumnoRegistrado(alumno);
            return cursosRepository.save(curso);
        }
        return null;
    }
}
//Final version

