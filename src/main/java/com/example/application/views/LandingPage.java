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
/*            Image catImage = new Image("https://img.freepik.com/free-vector/flat-construction-template_23-2147739822.jpg?w=1800&t=st=1686774399~exp=1686774999~hmac=1511d4a455d2e5903add7d35342c0f852cfcffa8c749142a4dbee3d072b887ff", "Katzenbild");
            catImage.setWidth("50%");
            add(catImage);*/
            //Image from resources
            Image logo = new Image("images/logo.png", "Webst@rs Ticketing Application Logo");
            // adapt width to the available space
            logo.setMaxWidth("100%");
            add(logo);
        }
}
