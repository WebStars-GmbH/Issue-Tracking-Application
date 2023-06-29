package com.example.application.views;

import com.example.application.data.entity.Website;
import com.example.application.data.service.CrmService;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TeamService;
import com.example.application.data.service.WebsiteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

@org.springframework.stereotype.Component
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "Websites", layout = com.example.application.views.MainLayout.class)
@PageTitle("Websites | Webst@rs Ticketing Application")
public class WebsiteView extends VerticalLayout {
    Grid<Website> grid = new Grid<>(Website.class);
    WebsiteForm form;
    CrmService service;


    WebsiteService websiteService;

    TUserService tUserService;
    TeamService teamService;

    public WebsiteView(WebsiteService websiteService, TUserService tUserService, TeamService teamService) {
        this.teamService = teamService;
        this.websiteService = websiteService;
        this.tUserService = tUserService;
        addClassName("website-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new WebsiteForm(teamService.findAllTeams(""), tUserService.findAllUsers());
        form.setWidth("70em");
        form.addSaveListener(this::saveWebsite); // <1>
        form.addDeleteListener(this::deleteWebsite); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveWebsite(WebsiteForm.SaveEvent event) {
        websiteService.saveWebsite(event.getWebsite());
        updateList();
        closeEditor();
    }

    private void ConfirmAndDelete(Website website){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Do you want to delete this Website?");
        dialog.setText("Are you sure you want to permanently delete this Website? This cannot be reversed.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete Website");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            websiteService.deleteWebsite(website);
            updateList();
            form.setWebsite(null);
            form.setVisible(false);});
        dialog.open();
    }

    private void deleteWebsite(WebsiteForm.DeleteEvent event) {
        websiteService.deleteWebsite(event.getWebsite());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("website-grid");
        grid.setSizeFull();
        grid.setColumns("website_name");


        grid.addColumn(website -> website.getTeam().getName()).setHeader("Team").setSortable(true);
        grid.addColumn(website -> website.getUser() == null ? "" : website.getUser().getUsername()).setHeader("Owner").setSortable(true);
        grid.addColumn(
                        LitRenderer.<Website>of(LIT_TEMPLATE_HTML)
                                .withProperty("id", website -> website.getTicketsCount())
                                .withFunction("clickHandler", website -> {
                                    getUI().get().navigate(CompanyTicketView.class, website.getWebsite_name());
                                }))
                .setHeader("Tickets").setSortable(true);

        //UNCOMMENT THIS IF YOU ALSO WANT TO SHOW TICKET HEADERS
        /*
        grid.addColumn(website -> website.getTickets().stream()
                        .map(Ticket::getHeader)
                        .collect(Collectors.joining(", ")))
                .setHeader("Tickets");
        */

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editWebsite(event.getValue()));
    }

    private static final String LIT_TEMPLATE_HTML = """
            <vaadin-button title="Go to ..."
                           @click="${clickHandler}"
                           theme="tertiary-inline small link">
                ${item.id}
            </vaadin-button>""";

    private Component getToolbar() {
        Button addWebsiteButton = new Button("Add Website");
        addWebsiteButton.addClickListener(click -> addWebsite());

        var toolbar = new HorizontalLayout(addWebsiteButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editWebsite(Website ticket) {
        if (ticket == null) {
            closeEditor();
        } else {
            closeEditor();
            form.setWebsite(ticket);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setWebsite(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addWebsite() {
        closeEditor();
        grid.asSingleSelect().clear();
        Website ticket = new Website();

        form.setWebsite(ticket);
        form.setVisible(true);
        editWebsite(ticket);
    }

    private void updateList() {
        grid.setItems(websiteService.getAllWebsites());
    }
}

