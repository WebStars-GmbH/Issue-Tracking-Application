package com.example.application.views;

import com.example.application.data.entity.Ticket;
import com.example.application.data.entity.Website;
import com.example.application.data.service.WebsiteService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.util.stream.Collectors;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "Websites", layout = com.example.application.views.MainLayout.class)
@PageTitle("Websites | Webst@rs Ticketing Application")
public class WebsiteView extends VerticalLayout {
    Grid<Website> grid = new Grid<>(Website.class);

    WebsiteService websiteService;

    public WebsiteView(WebsiteService websiteService) {
        this.websiteService = websiteService;
        addClassName("website-view");
        setSizeFull();
        configureGrid();

        add(getContent());
        updateList();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.addClassNames("website-grid");
        grid.setSizeFull();

        grid.setColumns("website_name", "URL");

        grid.addColumn(website -> website.getUser() == null ? "" : website.getUser().getUsername()).setHeader("User").setSortable(true);

        grid.addColumn(website -> website.getTickets().stream()
                        .map(Ticket::getHeader)
                        .collect(Collectors.joining(", ")))
                .setHeader("Tickets");
    }

    private void updateList() {
        grid.setItems(websiteService.getAllWebsites());
    }
}
