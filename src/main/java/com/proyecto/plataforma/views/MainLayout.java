package com.proyecto.plataforma.views;

import com.proyecto.plataforma.data.User;
import com.proyecto.plataforma.views.creadorcuestionario.CreadorCuestionarioView;
import com.proyecto.plataforma.views.creadorcurso.CreadorCursoView;
import com.proyecto.plataforma.views.gestionusuario.GestionUsuarioView;
import com.proyecto.plataforma.views.gestorclases.GestorClasesView;
import com.proyecto.plataforma.views.gestorcuestionario.GestorCuestionarioView;
import com.proyecto.plataforma.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Proyecto Plataforma Aprendizaje");
        logo.addClassNames("text-l", "m-m");

        Button logoutButton = new Button("Cerrar sesión", e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            UI.getCurrent().navigate(LoginView.class);
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutButton);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        if (user != null) {
            if ("Admin".equals(user.getRol())) {
                addToDrawer(new VerticalLayout(
                        new RouterLink("Gestión de Usuarios", GestionUsuarioView.class)
                ));
            } else if ("Profesor".equals(user.getRol())) {
                addToDrawer(new VerticalLayout(
                        new RouterLink("Crear Cuestionario", CreadorCuestionarioView.class),
                        new RouterLink("Gestor Clases", GestorClasesView.class),
                        new RouterLink("Creador Cursos", CreadorCursoView.class),
                        new RouterLink("Gestionar Cuestionarios", GestorCuestionarioView.class)
                ));
            } else if ("Estudiante".equals(user.getRol())) {
                addToDrawer(new VerticalLayout(
                        new RouterLink("Buscar Cursos", BuscarCursoVista.class),
                        new RouterLink("Cursos tomados", CursosTomadosVista.class),
                        new RouterLink("Ver mis notas", VerMisNotasVista.class)
                ));
            }
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (VaadinSession.getCurrent().getAttribute(User.class) == null) {
            event.rerouteTo(LoginView.class);
        }
    }
}
//Final version

