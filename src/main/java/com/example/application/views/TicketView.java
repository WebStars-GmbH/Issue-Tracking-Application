package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Ticket;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "Tickets", layout = com.example.application.views.MainLayout.class)
@PageTitle("Tickets | Webst@rs Ticketing Application")
public class TicketView extends VerticalLayout {
    Grid<Ticket> grid = new Grid<>(Ticket.class);
    TextField websiteFilterText = new TextField("Website");
    TextField statusFilterText = new TextField("Status");
    TextField descriptionFilterText = new TextField("Description");

    ComboBox<TUser> assignedToComboBox = new ComboBox<>("Assigned To");
    com.example.application.views.TicketForm form;
    CrmService service;

    public TicketView(CrmService service) {
        this.service = service;
        addClassName("ticket-view");
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
        form = new com.example.application.views.TicketForm(service, service.findAllWebsites(), service.findAllTUsers());
        form.setWidth("25em");
        form.addSaveListener(this::saveTicket); // <1>
        form.addDeleteListener(this::deleteTicket); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveTicket(com.example.application.views.TicketForm.SaveEvent event) {
        service.saveTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void deleteTicket(com.example.application.views.TicketForm.DeleteEvent event) {
        service.deleteTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("ticket-grid");
        grid.setSizeFull();

        grid.setColumns("priority", "status", "registered_by", "register_date", "description", "close_date", "solution", "last_update", "history");

        grid.addColumn(ticket -> ticket.getWebsite().getURL()).setHeader("Website").setSortable (true);;

        grid.addColumn(ticket -> ticket.getAssigned_to().getUsername()).setHeader("Assigned to").setSortable (true);;

        grid.getColumns().forEach(col -> col.setAutoWidth(true));


        grid.asSingleSelect().addValueChangeListener(event ->
                editTicket(event.getValue()));
    }

    private Component getToolbar() {
        websiteFilterText.setPlaceholder("Filter by website...");
        websiteFilterText.setClearButtonVisible(true);
        websiteFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        websiteFilterText.addValueChangeListener(e -> updateList());

        statusFilterText.setPlaceholder("Filter by status...");
        statusFilterText.setClearButtonVisible(true);
        statusFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        statusFilterText.addValueChangeListener(e -> updateListByStatus());

        descriptionFilterText.setPlaceholder("Filter by description...");
        descriptionFilterText.setClearButtonVisible(true);
        descriptionFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        descriptionFilterText.addValueChangeListener(e -> updateListByDescription());

        List<TUser> users = service.findAllTUsersByRole("team_member");
        assignedToComboBox.setItems(users);
        assignedToComboBox.setItemLabelGenerator(TUser::getUsername);
        assignedToComboBox.addValueChangeListener(e -> updateListByAssignedTo());

        Button addTicketButton = new Button("Add ticket");
        addTicketButton.addClickListener(click -> addTicket());

        Button clearFieldsButton = new Button("Clear filters");
        clearFieldsButton.addClickListener(click -> clearFields());

        var toolbar = new HorizontalLayout(clearFieldsButton, statusFilterText, descriptionFilterText, websiteFilterText, assignedToComboBox, addTicketButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void clearFields() {
        websiteFilterText.clear();
        websiteFilterText.clear();
        statusFilterText.clear();
        descriptionFilterText.clear();
        assignedToComboBox.clear();

        grid.setItems(service.findAllTickets(""));
    }

    public void editTicket(Ticket ticket) {
        if (ticket == null) {
            closeEditor();
        } else {
            form.setTicket(ticket);
            form.setVisible(true);
            addClassName("editing");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ticket.setLast_update(timestamp);
            String timestampString = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(timestamp);
            //ticket.setHistory(ticket.getHistory() + timestampString + ": Ticket modified"); //TODO
        }
    }

    private void closeEditor() {
        form.setTicket(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addTicket() {
        grid.asSingleSelect().clear();
        Ticket ticket = new Ticket();
        String u = com.example.application.views.MainLayout.username;
        ticket.setRegistered_by(u);

        //TUser tuser = service.findAllTUsers(u).get(0);
        editTicket(ticket);
    }


    private void updateList() {
        grid.setItems(service.findAllTickets(websiteFilterText.getValue()));
    }
    private void updateListByStatus() {
        grid.setItems(service.findAllTicketsByStatus(statusFilterText.getValue()));
    }

    private void updateListByDescription() {
        grid.setItems(service.findAllTicketsByDescription(descriptionFilterText.getValue()));
    }
    private void updateListByAssignedTo() {
        grid.setItems(service.findAllTicketsByAssignedTo(assignedToComboBox.getValue().getUsername()));
    }
}
