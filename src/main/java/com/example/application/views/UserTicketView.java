package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Ticket;
import com.example.application.data.service.CrmService;
import com.example.application.data.service.TicketService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "UserTicketView", layout = MainLayout.class)
@PageTitle("Tickets | Webst@rs Ticketing Application")
public class UserTicketView extends VerticalLayout {
    Grid<Ticket> grid = new Grid<>(Ticket.class);

    TextField descriptionFilterText = new TextField("Description");


    TicketForm form;
    TicketAddForm addForm;
    CrmService service;
    TicketService ticketService;

    Grid.Column<Ticket> statusColumn = grid.addColumn(Ticket::getStatus).setHeader("Status").setSortable(true).setResizable(true);
    Grid.Column<Ticket> headerColumn = grid.addColumn(Ticket::getHeader).setHeader("Header").setSortable(true).setResizable(true);
    Grid.Column<Ticket> descriptionColumn = grid.addColumn(Ticket::getDescription).setHeader("Description").setSortable(true).setResizable(true);
    Grid.Column<Ticket> historyColumn = grid.addColumn(Ticket::getHistory).setHeader("History").setSortable(true).setResizable(true);
    Grid.Column<Ticket> solutionColumn = grid.addColumn(Ticket::getSolution).setHeader("Solution").setSortable(true).setResizable(true);
    Grid.Column<Ticket> websiteColumn = grid.addColumn(Ticket::getWebsite).setHeader("Website").setSortable(true).setResizable(true);
    Grid.Column<Ticket> registeredByColumn = grid.addColumn(Ticket::getRegistered_by).setHeader("Registered By").setSortable(true).setResizable(true);
    Grid.Column<Ticket> registerDateColumn = grid.addColumn(Ticket::getRegister_date).setHeader("Register Date").setSortable(true).setResizable(true);
    Grid.Column<Ticket> lastUpdateColumn = grid.addColumn(Ticket::getLast_update).setHeader("Last Update").setSortable(true).setResizable(true);
    Grid.Column<Ticket> closedDateColumn = grid.addColumn(Ticket::getClose_date).setHeader("Closed Date").setSortable(true).setResizable(true);
    Grid.Column<Ticket> closedByColumn = grid.addColumn(Ticket::getClosed_by).setHeader("Closed By").setSortable(true).setResizable(true);


    public UserTicketView(CrmService service, TicketService ticketService) {
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
        form = new TicketForm(service.findAllWebsites(), service.findAllTUsersByRole("Support-Member"));
        form.setWidth("70em");
        form.addSaveListener(this::saveTicket); // <1>
        form.addDeleteListener(this::deleteTicket); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>

        addForm = new TicketAddForm(service.findAllWebsites(), service.findAllTUsers("Support-Member"));
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

    private void ConfirmAndDelete(Ticket ticket){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Do you want to delete this ticket?");
        dialog.setText("Are you sure you want to permanently delete this ticket? This cannot be reversed.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete Ticket");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            ticketService.deleteTicket(ticket);
            updateList();
            form.setTicket(null);
            form.setVisible(false);});
        dialog.open();
    }

    private void deleteTicket(TicketForm.DeleteEvent event) {
        ticketService.deleteTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("ticket-grid");
        grid.setSizeFull();


        grid.setColumns("status", "header", "description", "history", "solution", "website", "registered_by", "register_date", "last_update", "close_date", "closed_by");

        grid.setColumnReorderingAllowed(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        GridContextMenu<Ticket> menu = grid.addContextMenu();
        menu.addItem("View Details", event -> {        });
        menu.addItem("Edit Ticket", event -> editTicket(event.getItem().get()));
        menu.addItem("Delete Ticket", event -> ConfirmAndDelete(event.getItem().get()));
        grid.asSingleSelect().addValueChangeListener(event -> editTicket(event.getValue()));

        List<Ticket> ticket = ticketService.findAllTicketsByRegisteredBy(MainLayout.username);
        grid.setItems(ticket);

        Span title = new Span("Tickets");
        title.getStyle().set("font-weight", "bold");
        add(grid);
    }
    private Component getToolbar() {
        descriptionFilterText.setPlaceholder("Filter by description...");
        descriptionFilterText.setTooltipText("Please type what the description should contain...");
        descriptionFilterText.setClearButtonVisible(true);
        descriptionFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        descriptionFilterText.addValueChangeListener(e -> updateListByDescription());

        Button myOpenTicketsButton = new Button("My Open Tickets");
        myOpenTicketsButton.addClickListener(click -> updateListByStatus(MainLayout.username, "Registered"));

        Button myClosedTicketsButton = new Button("My Closed Tickets");
        myClosedTicketsButton.addClickListener(click -> updateListByStatus(MainLayout.username, "Closed"));

        Button allTicketsButton = new Button("All My Tickets");
        allTicketsButton.addClickListener(click -> updateListByRegistered(MainLayout.username));

        Button addTicketButton = new Button("Add ticket");
        addTicketButton.addClickListener(click -> addTicket());

        Button menuButton = new Button("Show/Hide");
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(menuButton);
        columnToggleContextMenu.addColumnToggleItem("Status", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Header", headerColumn);
        columnToggleContextMenu.addColumnToggleItem("Description", descriptionColumn);
        columnToggleContextMenu.addColumnToggleItem("History", historyColumn);
        columnToggleContextMenu.addColumnToggleItem("Solution", solutionColumn);
        columnToggleContextMenu.addColumnToggleItem("Website", websiteColumn);
        columnToggleContextMenu.addColumnToggleItem("Registered By", registeredByColumn);
        columnToggleContextMenu.addColumnToggleItem("Register Date", registerDateColumn);
        columnToggleContextMenu.addColumnToggleItem("Last Update", lastUpdateColumn);
        columnToggleContextMenu.addColumnToggleItem("Closed Date", closedDateColumn);
        columnToggleContextMenu.addColumnToggleItem("Closed By", closedByColumn);

        var toolbar = new HorizontalLayout(descriptionFilterText, myOpenTicketsButton, myClosedTicketsButton, allTicketsButton, addTicketButton, menuButton);
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        return toolbar;
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
            String u = MainLayout.username;
            ticket.setHistory(ticket.getHistory() + timestampString + ": modified by " + u + "; " + " \n"); //TODO
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
        addForm.setTicket(ticket);
        addForm.setVisible(true);
        addClassName("adding");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ticket.setStatus("Registered");
        String u = MainLayout.username;
        TUser tuser = service.getTUserByUsername(u);//TODO
        ticket.setRegistered_by(u);
        ticket.setRegister_date(timestamp);
        ticket.setLast_update(timestamp);
        String timestampString = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(timestamp);
        ticket.setHistory(timestampString + ": created by " + u + "; "); //TODO
    }

    private void updateListByStatus(String name, String status) {
        grid.setItems(ticketService.searchTicketsByStatus(name, status));
    }
    private void updateListByRegistered(String username) {
        grid.setItems(ticketService.findAllTicketsByRegisteredBy(username));
    }
    private void updateListByDescription() {
        grid.setItems(ticketService.findAllTicketsByDescription(descriptionFilterText.getValue()));
    }

    static class ColumnToggleContextMenu extends ContextMenu {
        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        void addColumnToggleItem(String label, Grid.Column<Ticket> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }

    //use to show only records for a certain user
    private List<Ticket> filterUser(List<Ticket> tickets) {
        List<Ticket> filteredTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (Objects.equals(ticket.getRegistered_by(), MainLayout.username))
                filteredTickets.add(ticket);
        }
        return filteredTickets;
    }
    private void updateList() {
        List<Ticket> allTickets = ticketService.findAllTickets("");
        List<Ticket> userTickets = filterUser(allTickets);
        grid.setItems(userTickets);
    }
}
