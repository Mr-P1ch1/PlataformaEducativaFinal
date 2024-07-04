package com.proyecto.plataforma.capaNegocio;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Document(collection = "cursos")
public class Cursos {
    @Id
    private String id;
    private String titulo;
    private String descripcion;
    private String area;
    private String tema;
    private String profesor;
    private PriorityQueue<Capitulo> capitulos;

    @DBRef
    private List<Estudiante> alumnosRegistrados;

    public Cursos(String id, String titulo, String descripcion, String area, String tema, String profesor) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.area = area;
        this.tema = tema;
        this.profesor = profesor;
        this.capitulos = new PriorityQueue<>();
        this.alumnosRegistrados = new ArrayList<>();
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public PriorityQueue<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(PriorityQueue<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

    public List<Estudiante> getAlumnosRegistrados() {
        return alumnosRegistrados;
    }

    public void setAlumnosRegistrados(List<Estudiante> alumnosRegistrados) {
        this.alumnosRegistrados = alumnosRegistrados;
    }

    public void agregarAlumnoRegistrado(Estudiante alumno) {
        this.alumnosRegistrados.add(alumno);
    }
}
//Final version
