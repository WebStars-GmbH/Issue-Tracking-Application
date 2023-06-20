package com.example.application.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Tickets | Webst@rs Ticketing Application")
public class LandingPage extends VerticalLayout  {

    public LandingPage() {
        //Image from resources
        Image logo = new Image("images/logo.png", "Webst@rs Ticketing Application Logo");
        // adapt width to the available space
        logo.setMaxWidth("100%");
        add(logo);
    }
}
