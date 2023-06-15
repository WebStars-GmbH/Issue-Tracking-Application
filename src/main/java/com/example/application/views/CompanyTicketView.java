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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
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
@Route(value = "CompanyTicketView", layout = MainLayout.class)
@PageTitle("Tickets | Webst@rs Ticketing Application")
public class CompanyTicketView extends VerticalLayout {
    Grid<Ticket> grid = new Grid<>(Ticket.class);

    TextField websiteFilterText = new TextField("Website");
    TextField statusFilterText = new TextField("Status");
    ComboBox<String> statusComboBox = new ComboBox<>("Status");
    TextField descriptionFilterText = new TextField("Description");
    ComboBox<TUser> assignedToComboBox = new ComboBox<>("Assigned To");

    TicketForm form;
    TicketAddForm addForm;
    CrmService service;
    TicketService ticketService;


    Grid.Column<Ticket> priorityColumn = grid.addColumn(Ticket::getPriority).setHeader("Priority").setSortable(true).setResizable(true);
    Grid.Column<Ticket> statusColumn = grid.addColumn(Ticket::getStatus).setHeader("Status").setSortable(true).setResizable(true);
    Grid.Column<Ticket> headerColumn = grid.addColumn(Ticket::getHeader).setHeader("Header").setSortable(true).setResizable(true);
    Grid.Column<Ticket> descriptionColumn = grid.addColumn(Ticket::getDescription).setHeader("Description").setSortable(true).setResizable(true);
    Grid.Column<Ticket> historyColumn = grid.addColumn(Ticket::getHistory).setHeader("History").setSortable(true).setResizable(true);
    Grid.Column<Ticket> solutionColumn = grid.addColumn(Ticket::getSolution).setHeader("Solution").setSortable(true).setResizable(true);
    Grid.Column<Ticket> websiteColumn = grid.addColumn(Ticket::getWebsite).setHeader("Website").setSortable(true).setResizable(true);
    Grid.Column<Ticket> registeredByColumn = grid.addColumn(Ticket::getRegistered_by).setHeader("Registered By").setSortable(true).setResizable(true);
    Grid.Column<Ticket> assignedToColumn = grid.addColumn(Ticket::getAssigned_to).setHeader("Assigned To").setSortable(true).setResizable(true);
    Grid.Column<Ticket> registerDateColumn = grid.addColumn(Ticket::getRegister_date).setHeader("Register Date").setSortable(true).setResizable(true);
    Grid.Column<Ticket> lastUpdateColumn = grid.addColumn(Ticket::getLast_update).setHeader("Last Update").setSortable(true).setResizable(true);
    Grid.Column<Ticket> closedDateColumn = grid.addColumn(Ticket::getClose_date).setHeader("Closed Date").setSortable(true).setResizable(true);
    Grid.Column<Ticket> closedByColumn = grid.addColumn(Ticket::getClosed_by).setHeader("Closed By").setSortable(true).setResizable(true);


    public CompanyTicketView(CrmService service, TicketService ticketService) {
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


        grid.setColumns("priority", "status", "header", "description", "history", "solution", "website", "registered_by", "assigned_to", "register_date", "last_update", "close_date", "closed_by");

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
        websiteFilterText.setPlaceholder("Filter by website...");
        websiteFilterText.setTooltipText("Please type what the website name should contain...");
        websiteFilterText.setClearButtonVisible(true);
        websiteFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        websiteFilterText.addValueChangeListener(e -> updateListByWebsite());

        statusFilterText.setPlaceholder("Filter by status...");
        statusFilterText.setClearButtonVisible(true);
        statusFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        statusFilterText.addValueChangeListener(e -> updateListByStatus());

        descriptionFilterText.setPlaceholder("Filter by description...");
        descriptionFilterText.setTooltipText("Please type what the description should contain...");
        descriptionFilterText.setClearButtonVisible(true);
        descriptionFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        descriptionFilterText.addValueChangeListener(e -> updateListByDescription());

        statusComboBox.setItems("Registered", "Assigned", "In progress", "Cancelled", "Solved");
        statusComboBox.setTooltipText("Please choose the status of the tickets you want to look for...");
        statusComboBox.addValueChangeListener(e -> updateListByStatus());

        List<TUser> users = service.findAllTUsersByRole("Support-Member");
        assignedToComboBox.setTooltipText("Please choose the assigned users you want to look for...");
        assignedToComboBox.setItems(users);
        assignedToComboBox.setItemLabelGenerator(TUser::getUsername);
        assignedToComboBox.addValueChangeListener(e -> updateListByAssignedTo());

        Button myOpenTicketsButton = new Button("My Assigned Tickets");
     //   myOpenTicketsButton.addClickListener(click -> updateListByStatus(MainLayout.username, "Registered"));

        Button myClosedTicketsButton = new Button("My To-Do Tickets");
     //   myClosedTicketsButton.addClickListener(click -> updateListByStatus(MainLayout.username, "Closed"));

        Button allMyTicketsButton = new Button("All My Tickets");
     //   allTicketsButton.addClickListener(click -> updateListByRegistered(MainLayout.username));

        Button allTicketsButton = new Button("All Tickets");
        allTicketsButton.addClickListener(click -> updateList());

        Button addTicketButton = new Button("Add ticket");
        addTicketButton.addClickListener(click -> addTicket());

        Button clearFieldsButton = new Button("Clear filters");
        clearFieldsButton.addClickListener(click -> clearFields());

        Button applyAllFiltersButton = new Button("Apply all filters");
        applyAllFiltersButton.addClickListener(click -> updateListByAllFilters());

        Button registeredButton = new Button("Registered Tickets");
        registeredButton.addClickListener(click -> updateListByStatusRegistered());

        Button allAssignedButton = new Button("All Assigned Tickets");
        allAssignedButton.addClickListener(click -> updateListByStatus());


        Button menuButton = new Button("Show/Hide");
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        CompanyTicketView.ColumnToggleContextMenu columnToggleContextMenu = new CompanyTicketView.ColumnToggleContextMenu(menuButton);
        columnToggleContextMenu.addColumnToggleItem("Priority", priorityColumn);
        columnToggleContextMenu.addColumnToggleItem("Status", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Header", headerColumn);
        columnToggleContextMenu.addColumnToggleItem("Description", descriptionColumn);
        columnToggleContextMenu.addColumnToggleItem("History", historyColumn);
        columnToggleContextMenu.addColumnToggleItem("Solution", solutionColumn);
        columnToggleContextMenu.addColumnToggleItem("Website", websiteColumn);
        columnToggleContextMenu.addColumnToggleItem("Registered By", registeredByColumn);
        columnToggleContextMenu.addColumnToggleItem("Assigned To", assignedToColumn);
        columnToggleContextMenu.addColumnToggleItem("Register Date", registerDateColumn);
        columnToggleContextMenu.addColumnToggleItem("Last Update", lastUpdateColumn);
        columnToggleContextMenu.addColumnToggleItem("Closed Date", closedDateColumn);
        columnToggleContextMenu.addColumnToggleItem("Closed By", closedByColumn);


        HorizontalLayout toolbar;
        if (String.valueOf(MainLayout.userRole).equals("System-Admin")) {
            toolbar = new HorizontalLayout(clearFieldsButton, descriptionFilterText, statusFilterText, websiteFilterText, assignedToComboBox, allTicketsButton, addTicketButton, menuButton);
        } else if (String.valueOf(MainLayout.userRole).equals("Support-Member")) {
            toolbar = new HorizontalLayout(clearFieldsButton, descriptionFilterText, statusFilterText, websiteFilterText, assignedToComboBox, myOpenTicketsButton, myClosedTicketsButton, allMyTicketsButton, allTicketsButton, addTicketButton, menuButton);
        } else if (String.valueOf(MainLayout.userRole).equals("Support-Coordinator")) {
            toolbar = new HorizontalLayout(clearFieldsButton, descriptionFilterText, statusFilterText, websiteFilterText, assignedToComboBox, allAssignedButton, allTicketsButton, addTicketButton, menuButton);
        } else if (String.valueOf(MainLayout.userRole).equals("Manager")) {
            toolbar = new HorizontalLayout(descriptionFilterText, allTicketsButton, addTicketButton, menuButton);
        } else {
            toolbar = new HorizontalLayout(menuButton);
        }
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void clearFields() {
        websiteFilterText.clear();
        statusFilterText.clear();
        descriptionFilterText.clear();
        assignedToComboBox.clear();
        statusComboBox.clear();

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


    private void updateListByWebsite() {
        grid.setItems(ticketService.findAllTickets(websiteFilterText.getValue()));
    }
    private void updateListByStatus() {
        if (statusComboBox.getValue() != null) grid.setItems(ticketService.findAllTicketsByStatus(statusComboBox.getValue()));
    }
    private void updateListByStatusRegistered() {
        grid.setItems(ticketService.findAllTicketsByStatus("Registered"));
    }
    private void updateListByDescription() {
        grid.setItems(ticketService.findAllTicketsByDescription(descriptionFilterText.getValue()));
    }
    private void updateListByAssignedTo() {
        if (assignedToComboBox.getValue() != null) grid.setItems(ticketService.findAllTicketsByAssignedTo(assignedToComboBox.getValue().getUsername()));
    }

    private void updateListByAllFilters() {
        String statusFilter = "";
        if (statusComboBox.getValue() != null) statusFilter = statusComboBox.getValue();

        String websiteFilter = "";
        if (websiteFilterText.getValue() != null) websiteFilter = websiteFilterText.getValue();

        String descriptionFilter = "";
        if (descriptionFilterText.getValue() != null) descriptionFilter = descriptionFilterText.getValue();

        String assignedToFilter = "";
        if (assignedToComboBox.getValue() == null) grid.setItems(ticketService.findAllTicketsByStatusWebsiteDescription(statusFilter, websiteFilter, descriptionFilter));
        else {
            assignedToFilter = assignedToComboBox.getValue().getUsername();
            grid.setItems(ticketService.findAllTicketsByAllFilters(statusFilter, websiteFilter, descriptionFilter, assignedToFilter));
        }

        Notification notification = Notification
                .show("Found " + grid.getDataProvider().size(new Query<>()) + " Tickets with: Status: " + statusFilter + ", Website: " + websiteFilter + ", Description: " + descriptionFilter + ", Assigned to: " + assignedToFilter + ";");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

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

    private void updateList() {
        grid.setItems(ticketService.findAllTickets(""));
    }

    //use to show only records for a certain user, function above has to be deleted
}
