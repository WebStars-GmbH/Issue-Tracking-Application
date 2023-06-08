package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Ticket;
import com.example.application.data.service.CrmService;
import com.example.application.data.service.TicketService;
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
    TicketForm form;
    TicketAddForm addForm;
    CrmService service;
    TicketService ticketService;

    public TicketView(CrmService service, TicketService ticketService) {
        this.service = service;
        this.ticketService = ticketService;
        addClassName("ticket-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form, addForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new TicketForm(service, service.findAllWebsites(), service.findAllTUsers());
        form.setWidth("70em");
        form.addSaveListener(this::saveTicket); // <1>
        form.addDeleteListener(this::deleteTicket); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>

        addForm = new TicketAddForm(service, service.findAllWebsites(), service.findAllTUsers());
        addForm.setWidth("70em");
        addForm.addSaveListener(this::saveAddTicket); // <1>
        addForm.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveAddTicket(TicketAddForm.SaveEvent event) {
        ticketService.saveTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void saveTicket(TicketForm.SaveEvent event) {
        ticketService.saveTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void deleteTicket(TicketForm.DeleteEvent event) {
        ticketService.deleteTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("ticket-grid");
        grid.setSizeFull();

        grid.setColumns("priority", "header", "status", "registered_by", "register_date", "description", "close_date", "solution", "last_update", "history");

        grid.addColumn(ticket -> ticket.getWebsite().getURL()).setHeader("Website").setSortable(true);

        grid.addColumn(ticket -> ticket.getAssigned_to() == null ? "" : ticket.getAssigned_to().getUsername()).setHeader("Assigned to").setSortable(true);

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

        grid.setItems(ticketService.findAllTickets(""));
    }

    public void editTicket(Ticket ticket) {
        if (ticket == null) {
            closeEditor();
        } else {
            closeEditor();
            form.setTicket(ticket);
            form.setVisible(true);
            addClassName("editing");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ticket.setLast_update(timestamp);
            String timestampString = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(timestamp);
            String u = com.example.application.views.MainLayout.username;
            ticket.setHistory(ticket.getHistory() + timestampString + ": modified by " + u + "; "); //TODO
        }
    }

    private void closeEditor() {
        form.setTicket(null);
        form.setVisible(false);
        addForm.setTicket(null);
        addForm.setVisible(false);
        removeClassName("editing");
    }

    private void addTicket() {
        closeEditor();
        grid.asSingleSelect().clear();
        Ticket ticket = new Ticket();
        ticket.setStatus("registered");
        //editTicket(ticket);

        addForm.setTicket(ticket);
        addForm.setVisible(true);
        addClassName("adding");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ticket.setStatus("registered");
        String u = com.example.application.views.MainLayout.username;

        TUser tuser = service.getTUserByUsername(u);//TODO
        ticket.setRegistered_by(u);
        ticket.setRegister_date(timestamp);
        ticket.setLast_update(timestamp);
        String timestampString = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(timestamp);
        ticket.setHistory(timestampString + ": created by " + u + "; "); //TODO
    }

    private void updateList() {
        grid.setItems(ticketService.findAllTickets(websiteFilterText.getValue()));
    }
    private void updateListByStatus() {
        grid.setItems(ticketService.findAllTicketsByStatus(statusFilterText.getValue()));
    }

    private void updateListByDescription() {
        grid.setItems(ticketService.findAllTicketsByDescription(descriptionFilterText.getValue()));
    }
    private void updateListByAssignedTo() {
        if (assignedToComboBox.getValue() != null) grid.setItems(ticketService.findAllTicketsByAssignedTo(assignedToComboBox.getValue().getUsername()));
    }
}
