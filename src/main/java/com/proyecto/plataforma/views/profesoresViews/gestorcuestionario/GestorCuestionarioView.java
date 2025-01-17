package com.proyecto.plataforma.views.profesoresViews.gestorcuestionario;

import com.proyecto.plataforma.capaNegocio.Cuestionario;
import com.proyecto.plataforma.estructuras.CuestionarioLista;
import com.proyecto.plataforma.capaNegocio.Pregunta;
import com.proyecto.plataforma.capaNegocio.Profesor;
import com.proyecto.plataforma.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Route(value = "gestor-cuestionario", layout = MainLayout.class)
public class GestorCuestionarioView extends VerticalLayout {

    private final CuestionarioLista cuestionarioLista;
    private final Grid<Cuestionario> grid;

    @Autowired
    public GestorCuestionarioView(CuestionarioLista cuestionarioLista) {
        this.cuestionarioLista = cuestionarioLista;
        this.grid = new Grid<>(Cuestionario.class);

        setSizeFull();
        configureGrid();
        add(grid);
        updateGrid();
    }

    private void configureGrid() {
        grid.setColumns("titulo", "intentos");
        grid.addColumn(cuestionario -> cuestionario.getPreguntas().size()).setHeader("Número de Preguntas");

        grid.addColumn(new ComponentRenderer<>(cuestionario -> {
            Div div = new Div();
            int index = 1;
            for (Pregunta pregunta : cuestionario.getPreguntas()) {
                Span span = new Span("Pregunta " + index + ": " + pregunta.getTexto());
                span.getStyle().set("display", "block");
                div.add(span);
                index++;
            }
            return div;
        })).setHeader("Preguntas");

        grid.addColumn(new ComponentRenderer<>(cuestionario -> {
            Div div = new Div();
            for (Pregunta pregunta : cuestionario.getPreguntas()) {
                Div preguntaDiv = new Div();
                preguntaDiv.add(new Span("Respuestas: " + String.join(", ", pregunta.getOpciones())));
                div.add(preguntaDiv);
            }
            return div;
        })).setHeader("Respuestas");

        grid.addColumn(new ComponentRenderer<>(cuestionario -> {
            Div div = new Div();
            for (Pregunta pregunta : cuestionario.getPreguntas()) {
                Div preguntaDiv = new Div();
                preguntaDiv.add(new Span("Respuesta Correcta: " + pregunta.getRespuestaCorrecta()));
                div.add(preguntaDiv);
            }
            return div;
        })).setHeader("Respuestas Correctas");

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        Button editarPreguntaButton = new Button("Editar Pregunta", event -> {
            Cuestionario selectedCuestionario = grid.asSingleSelect().getValue();
            if (selectedCuestionario != null) {
                abrirDialogoEdicion(selectedCuestionario);
            } else {
                Notification.show("Seleccione un cuestionario para editar");
            }
        });

        Button deleteButton = new Button("Eliminar Cuestionario(s) Seleccionado(s)", event -> {
            Cuestionario selectedCuestionario = grid.asSingleSelect().getValue();
            if (selectedCuestionario != null) {
                cuestionarioLista.eliminarCuestionario(selectedCuestionario);
                Notification.show("Cuestionario eliminado exitosamente");
                updateGrid();
            } else {
                Notification.show("Seleccione un cuestionario para eliminar");
            }
        });

        add(editarPreguntaButton, deleteButton);
    }

    private void updateGrid() {
        Profesor currentUser = VaadinSession.getCurrent().getAttribute(Profesor.class);
        if (currentUser != null) {
            List<Cuestionario> cuestionariosDelProfesor = cuestionarioLista.buscarPorProfesorString(currentUser.getId());
            if (cuestionariosDelProfesor.isEmpty()) {
                Notification.show("No se encontraron cuestionarios para el profesor actual.");
            } else {
                grid.setItems(cuestionariosDelProfesor);
            }
        } else {
            Notification.show("No se encontró un profesor en la sesión.");
            grid.setItems(Collections.emptyList());
        }
    }

    private void abrirDialogoEdicion(Cuestionario cuestionario) {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();

        List<TextField> preguntaFields = new ArrayList<>();
        List<TextArea> opcionesFields = new ArrayList<>();
        List<ComboBox<String>> respuestaCorrectaFields = new ArrayList<>();

        for (int i = 0; i < cuestionario.getPreguntas().size(); i++) {
            Pregunta pregunta = cuestionario.getPreguntas().get(i);

            Span preguntaLabel = new Span("Pregunta " + (i + 1));
            preguntaLabel.getStyle().set("font-weight", "bold");
            TextField preguntaField = new TextField("Texto de la pregunta");
            preguntaField.setValue(pregunta.getTexto());
            preguntaFields.add(preguntaField);

            TextArea opcionesField = new TextArea("Opciones (separadas por comas)");
            opcionesField.setValue(String.join(", ", pregunta.getOpciones()));
            opcionesFields.add(opcionesField);

            ComboBox<String> respuestaCorrectaField = new ComboBox<>("Respuesta Correcta");
            respuestaCorrectaField.setItems(pregunta.getOpciones());
            respuestaCorrectaField.setValue(pregunta.getRespuestaCorrecta());
            respuestaCorrectaFields.add(respuestaCorrectaField);

            opcionesField.addValueChangeListener(event -> {
                String[] opciones = opcionesField.getValue().split(",\\s*");
                respuestaCorrectaField.setItems(opciones);
            });

            layout.add(preguntaLabel, preguntaField, opcionesField, respuestaCorrectaField);
        }

        Button saveButton = new Button("Guardar", event -> {
            for (int i = 0; i < cuestionario.getPreguntas().size(); i++) {
                Pregunta pregunta = cuestionario.getPreguntas().get(i);
                pregunta.setTexto(preguntaFields.get(i).getValue());
                pregunta.setOpciones(List.of(opcionesFields.get(i).getValue().split(", ")));
                pregunta.setRespuestaCorrecta(respuestaCorrectaFields.get(i).getValue());
            }
            cuestionarioLista.guardarCuestionario(cuestionario);
            Notification.show("Pregunta actualizada exitosamente");
            updateGrid();
            dialog.close();
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        layout.add(buttonsLayout);

        dialog.add(layout);
        dialog.open();
    }
}
