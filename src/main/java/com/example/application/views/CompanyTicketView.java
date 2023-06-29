package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Ticket;
import com.example.application.data.service.CrmService;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TicketService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@org.springframework.stereotype.Component
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "CompanyTicketView", layout = MainLayout.class)
@PageTitle("Tickets | Webst@rs Ticketing Application")
public class CompanyTicketView extends VerticalLayout implements HasUrlParameter<String> {
    Grid<Ticket> grid = new Grid<>(Ticket.class);

    TextField websiteFilterText = new TextField("Website");
    TextField statusFilterText = new TextField("Status");
    ComboBox<String> statusComboBox = new ComboBox<>("Status");
    TextField descriptionFilterText = new TextField("Description");
    ComboBox<TUser> assignedToComboBox = new ComboBox<>("Assigned To");

    TicketForm form;
    TicketAddForm addForm;

    TicketDetailsForm viewDetailsForm;
    CrmService service;
    TicketService ticketService;
    TUserService tUserService;


    Grid.Column<Ticket> priorityColumn;
    Grid.Column<Ticket> statusColumn;
    Grid.Column<Ticket> headerColumn;
    Grid.Column<Ticket> descriptionColumn;
    Grid.Column<Ticket> historyColumn;
    Grid.Column<Ticket> solutionColumn;
    Grid.Column<Ticket> websiteColumn;
    Grid.Column<Ticket> registeredByColumn;
    Grid.Column<Ticket> assignedToColumn;
    Grid.Column<Ticket> registerDateColumn;
    Grid.Column<Ticket> lastUpdateColumn;
    Grid.Column<Ticket> closedDateColumn;
    Grid.Column<Ticket> closedByColumn;

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        if (parameter == null) {
            websiteFilterText.setValue("");
        } else {
            websiteFilterText.setValue(parameter);
        }
    }

    public CompanyTicketView(CrmService service, TicketService ticketService, TUserService tUserService) {
        this.service = service;
        this.ticketService = ticketService;
        this.tUserService = tUserService;
        addClassName("ticket-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form, addForm, viewDetailsForm);
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

        addForm = new TicketAddForm(service.findAllWebsites());
        addForm.setWidth("70em");
        addForm.addSaveListener(this::saveAddTicket); // <1>
        addForm.addCloseListener(e -> closeEditor()); // <3>

        viewDetailsForm = new TicketDetailsForm(service.findAllWebsites(), service.findAllTUsers("Support-Member"), true);
        viewDetailsForm.setWidth("70em");
        viewDetailsForm.addEditListener(e -> editTicket(grid.getSelectedItems().iterator().next()));
        viewDetailsForm.addCloseListener(e -> closeEditor()); // <3>
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
        closeEditor();
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Do you want to delete this ticket?");
        dialog.setText("Are you sure you want to delete this ticket?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete Ticket");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            ticketService.setTicketStatusToCancelled(ticket);
            //ticketService.deleteTicket(ticket); UNCOMMENT if you want to permanently delete tickets
            updateList();
            form.setTicket(null);
            form.setVisible(false);});
        dialog.open();
    }

    private void deleteTicket(TicketForm.DeleteEvent event) {
        ticketService.setTicketStatusToCancelled(event.getTicket());
        //ticketService.deleteTicket(event.getTicket()); UNCOMMENT if you want to permanently delete tickets
        updateList();
        closeEditor();
    }

    private void ConfirmAndDeletePermanently(Ticket ticket){
        closeEditor();
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Do you want to delete this ticket?");
        dialog.setText("Are you sure you want to delete this ticket?");
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

    private void configureGrid() {
        grid.addClassNames("ticket-grid");
        grid.setSizeFull();


        grid.setColumns();

        priorityColumn = grid.addColumn(Ticket::getPriority).setHeader("Priority").setSortable(true).setResizable(true);
        statusColumn = grid.addColumn(Ticket::getStatus).setHeader("Status").setSortable(true).setResizable(true);
        headerColumn = grid.addColumn(Ticket::getHeader).setHeader("Header").setSortable(true).setResizable(true);
        descriptionColumn = grid.addColumn(Ticket::getDescription).setHeader("Description").setSortable(true).setResizable(true);
        historyColumn = grid.addColumn(Ticket::getHistory).setHeader("History").setSortable(true).setResizable(true);
        solutionColumn = grid.addColumn(Ticket::getSolution).setHeader("Solution").setSortable(true).setResizable(true);
        websiteColumn = grid.addColumn(Ticket::getWebsite).setHeader("Website").setSortable(true).setResizable(true);
        registeredByColumn = grid.addColumn(Ticket::getRegistered_by).setHeader("Ticket Owner").setSortable(true).setResizable(true);
        assignedToColumn = grid.addColumn(ticket -> ticket.getAssigned_to() == null ? "" : ticket.getAssigned_to().getUsername()).setHeader("Assigned to").setSortable(true).setResizable(true);
        registerDateColumn = grid.addColumn(new LocalDateRenderer<>(Ticket::getRegister_date, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Register Date").setSortable(true).setResizable(true);
        lastUpdateColumn = grid.addColumn(new LocalDateRenderer<>(Ticket::getLast_update, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Last Update").setSortable(true).setResizable(true);
        closedDateColumn = grid.addColumn(new LocalDateRenderer<>(Ticket::getClose_date, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Closed Date").setSortable(true).setResizable(true);
        closedByColumn = grid.addColumn(Ticket::getClosed_by).setHeader("Closed By").setSortable(true).setResizable(true);
        grid.setColumnReorderingAllowed(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        GridContextMenu<Ticket> menu = grid.addContextMenu();
        menu.addItem("View Details", event -> viewTicket(event.getItem().get()));
        menu.addItem("Edit Ticket", event -> {
                    Ticket t = event.getItem().isPresent() ? event.getItem().get() : null;
                    if (t == null) return;
                    if (MainLayout.userRole.getRole_name().equals("System-Admin") || MainLayout.userRole.getRole_name().equals("Support-Coordinator")) editTicket(t);
                    else if (t.getAssigned_to() != null && t.getAssigned_to().getUsername().equals(MainLayout.username)) editTicket(t);
                    else {
                        Notification n = Notification.show("This ticket is not assigned to you. Editing is not allowed.");
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }
        );
        menu.addItem("Delete Ticket",
            event -> {
                Ticket t = event.getItem().isPresent() ? event.getItem().get() : null;
                if (t == null) return;
                if (MainLayout.userRole.getRole_name().equals("System-Admin") || MainLayout.userRole.getRole_name().equals("Support-Coordinator")) ConfirmAndDelete(t);
                else if (t.getAssigned_to() != null && t.getAssigned_to().getUsername().equals(MainLayout.username)) ConfirmAndDelete(t);
                else {
                    Notification n = Notification.show("This ticket is not assigned to you. Deleting is not allowed.");
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        );
        if (MainLayout.userRole.getRole_name().equals("System-Admin")) menu.addItem("Delete Ticket Permanently (System Admin)", event -> ConfirmAndDeletePermanently(event.getItem().get()));
        menu.addItem("Edit Ticket", event -> editTicket(event.getItem().get()));
        menu.addItem("Delete Ticket", event -> ConfirmAndDelete(event.getItem().get()));
        grid.asSingleSelect().addValueChangeListener(event -> viewTicket(event.getValue()));
        grid.addItemDoubleClickListener(event -> editTicket(event.getItem()));

        List<Ticket> ticket = ticketService.findAllTicketsByRegisteredBy(MainLayout.username);
        grid.setItems(ticket);

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
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
        statusFilterText.addValueChangeListener(e -> updateListWithStatus());

        descriptionFilterText.setPlaceholder("Filter by description...");
        descriptionFilterText.setTooltipText("Please type what the description should contain...");
        descriptionFilterText.setClearButtonVisible(true);
        descriptionFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        descriptionFilterText.addValueChangeListener(e -> updateListByDescription());

        statusComboBox.setItems("Registered", "Assigned", "In progress", "Cancelled", "Solved");
        statusComboBox.setTooltipText("Please choose the status of the tickets you want to look for...");
        statusComboBox.addValueChangeListener(e -> updateListWithStatus());

        List<TUser> users = service.findAllTUsersByRole("Support-Member");
        assignedToComboBox.setTooltipText("Please choose the assigned users you want to look for...");
        assignedToComboBox.setItems(users);
        assignedToComboBox.setItemLabelGenerator(TUser::getUsername);
        assignedToComboBox.addValueChangeListener(e -> updateListByAssignedTo());


        Button myAssignedTicketsButton = new Button("My Assigned Tickets");
        myAssignedTicketsButton.addClickListener(click -> updateListAssignedToAndByStatus(MainLayout.username, "Registered", "NULL", "NULL"));

        Button myToDoTicketsButton = new Button("My To-Do Tickets");
        myToDoTicketsButton.addClickListener(click -> updateListAssignedToAndByStatus(MainLayout.username, "Assigned", "In progress", "Registered"));

        Button allMyTicketsButton = new Button("All My Tickets");
        allMyTicketsButton.addClickListener(click -> updateListByAssignedToMe());

        Button allTicketsButton = new Button("All Tickets");
        allTicketsButton.addClickListener(click -> updateList());

        Button addTicketButton = new Button("Add ticket");
        addTicketButton.addClickListener(click -> addTicket());

        Button clearFieldsButton = new Button("Clear filters");
        clearFieldsButton.addClickListener(click -> clearFields());

        Button applyAllFiltersButton = new Button("Apply all filters");
        applyAllFiltersButton.addClickListener(click -> updateListByAllFilters());

        Button registeredButton = new Button("Registered Tickets");
        registeredButton.addClickListener(click -> updateListByStatus("Registered", "NULL", "NULL"));

        Button allAssignedButton = new Button("All Assigned Tickets");
        allAssignedButton.addClickListener(click -> updateListByStatus("Assigned", "NULL", "NULL"));

        Button allOpenTicketsButton = new Button("All Open Tickets");
        allOpenTicketsButton.addClickListener(click -> updateListByStatus("Assigned", "In progress", "Registered"));

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

        HorizontalLayout headerLayout = new HorizontalLayout(clearFieldsButton, descriptionFilterText, statusFilterText, websiteFilterText, assignedToComboBox, addTicketButton, menuButton);
        headerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        headerLayout.addClassName("seerch-row");

        HorizontalLayout headerLayout = new HorizontalLayout(clearFieldsButton, descriptionFilterText, statusFilterText, websiteFilterText, assignedToComboBox, addTicketButton, menuButton);
        headerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        headerLayout.addClassName("seerch-row");

        HorizontalLayout toolbar;
        if (String.valueOf(MainLayout.userRole).equals("System-Admin")) {
            toolbar = new HorizontalLayout(myAssignedTicketsButton, myToDoTicketsButton, allMyTicketsButton, allTicketsButton, registeredButton, allAssignedButton, allOpenTicketsButton);
        } else if (String.valueOf(MainLayout.userRole).equals("Support-Member")) {
            toolbar = new HorizontalLayout(myAssignedTicketsButton, myToDoTicketsButton, allMyTicketsButton, allOpenTicketsButton, allTicketsButton);
        } else if (String.valueOf(MainLayout.userRole).equals("Support-Coordinator")) {
            toolbar = new HorizontalLayout(registeredButton, allOpenTicketsButton);
        } else if (String.valueOf(MainLayout.userRole).equals("Manager")) {
            toolbar = new HorizontalLayout(descriptionFilterText, allTicketsButton, addTicketButton);
        } else {
            toolbar = new HorizontalLayout(menuButton);
        }
        toolbar.addClassName("toolbar");

        VerticalLayout verticalToolbar = new VerticalLayout(headerLayout, toolbar);
        verticalToolbar.setAlignItems(Alignment.BASELINE);
        verticalToolbar.addClassName("verticalToolbar");
        return verticalToolbar;
    }

    private void clearFields() {
        websiteFilterText.clear();
        statusFilterText.clear();
        descriptionFilterText.clear();
        assignedToComboBox.clear();
        statusComboBox.clear();

        grid.setItems(ticketService.findAllTickets(""));
    }

    public void viewTicket(Ticket ticket) {
        if (ticket == null) {
            closeEditor();
        } else {
            closeEditor();
            viewDetailsForm.setTicket(ticket);
            viewDetailsForm.setVisible(true);
            addClassName("viewing");
        }
    }

    public void editTicket(Ticket ticket) {
        if (ticket == null) {
            closeEditor();
        } else {
            if (ticket.getStatus().equals("Solved") || ticket.getStatus().equals("Cancelled")){
                Notification notification = Notification.show("This ticket cannot be edited. Status: " + ticket.getStatus());
                return;
            }
            closeEditor();

            ticket = ticketService.getTicket(ticket.getId());
            //TICKETFORM: UNCOMMENT, IF YOU WANT TO FILTER SUPPORT-MEMBERS BY TEAMS ASSIGNED TO THE WEBSITE, OTHERWISE ALL SUPPORT-MEMBERS WILL BE SHOWN
            form.updateAssignedTo(tUserService.findUsersByTeam(ticket.getWebsite().getTeam()));
            form.setTicket(ticket);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setTicket(null);
        form.setVisible(false);
        addForm.setTicket(null);
        addForm.setVisible(false);
        viewDetailsForm.setTicket(null);
        viewDetailsForm.setVisible(false);
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

        ticket.setRegistered_by(u);
        ticket.setRegister_date(timestamp);
        ticket.setLast_update(timestamp);
        String timestampString = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(timestamp);
        ticket.setHistory(timestampString + ": created by " + u + "; \n");
    }


    private void updateListByWebsite() {
        grid.setItems(ticketService.findAllTickets(websiteFilterText.getValue()));
    }
    private void updateListWithStatus() {
        if (statusComboBox.getValue() != null) grid.setItems(ticketService.findAllTicketsWithStatus(statusComboBox.getValue()));
    }
    private void updateListByStatus(String status1, String status2, String status3) {
        grid.setItems(ticketService.findAllTicketsByStatus(status1, status2, status3));
    }
    private void updateListByDescription() {
        grid.setItems(ticketService.findAllTicketsByDescription(descriptionFilterText.getValue()));
    }
    private void updateListByAssignedTo() {
        if (assignedToComboBox.getValue() != null) grid.setItems(ticketService.findAllTicketsByAssignedTo(assignedToComboBox.getValue().getUsername()));
    }
    private void updateListByAssignedToMe() {
        grid.setItems(ticketService.findAllTicketsByAssignedTo(MainLayout.username));
    }
    private void updateListAssignedToAndByStatus(String username, String status1, String status2, String status3) {
        grid.setItems(ticketService.findAllTicketsByAssignedToAndStatus(username, status1, status2, status3));
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
}
