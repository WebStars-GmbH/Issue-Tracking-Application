package com.example.application.views;

import com.example.application.data.entity.Ticket;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TicketService;
import com.example.application.data.service.WebsiteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "UserTicketView", layout = MainLayout.class)
@PageTitle("Tickets | Webst@rs Ticketing Application")
public class UserTicketView extends VerticalLayout {
    Grid<Ticket> grid = new Grid<>(Ticket.class, false);

    TextField descriptionFilterText = new TextField("Description");


    TicketDetailsForm viewDetailsForm;
    TicketAddForm addForm;
    TUserService tUserService;
    TicketService ticketService;
    WebsiteService websiteService;

    Grid.Column<Ticket> statusColumn;
    Grid.Column<Ticket> headerColumn;
    Grid.Column<Ticket> descriptionColumn;
//    Grid.Column<Ticket> historyColumn;
    Grid.Column<Ticket> solutionColumn;
    Grid.Column<Ticket> websiteColumn;
    Grid.Column<Ticket> registeredByColumn;
    Grid.Column<Ticket> registerDateColumn;
    Grid.Column<Ticket> lastUpdateColumn;
    Grid.Column<Ticket> closedDateColumn;
    Grid.Column<Ticket> closedByColumn;


    public UserTicketView(TicketService ticketService, TUserService tUserService, WebsiteService websiteService) {
        this.tUserService = tUserService;
        this.ticketService = ticketService;
        this.websiteService = websiteService;
        addClassName("ticket-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, viewDetailsForm, addForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, viewDetailsForm);
        content.setFlexGrow(1, addForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        viewDetailsForm = new TicketDetailsForm(websiteService.getAllWebsites(), tUserService.findAllTUsersByRole("Support-Member"), false);
        viewDetailsForm.setWidth("70em");
        viewDetailsForm.addCloseListener(e -> closeEditor()); // <3>

        addForm = new TicketAddForm(websiteService.getAllWebsitesByUsername(MainLayout.username));
        addForm.setWidth("70em");
        addForm.addSaveListener(this::saveAddTicket); // <1>
        addForm.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveAddTicket(TicketAddForm.SaveEvent event) {
        ticketService.saveTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("ticket-grid");
        grid.setSizeFull();

        grid.setColumns();

        statusColumn = grid.addColumn(createStatusComponentRenderer()).setHeader("Status").setAutoWidth(true).setComparator(Ticket::getStatus);
        headerColumn = grid.addColumn(Ticket::getHeader).setHeader("Header").setSortable(true).setResizable(true);
        descriptionColumn = grid.addColumn(Ticket::getDescription).setHeader("Description").setSortable(true).setResizable(true);
//      History not displayed in grid, only in details
//      historyColumn = grid.addColumn(Ticket::getHistory).setHeader("History").setSortable(true).setResizable(true);
        solutionColumn = grid.addColumn(Ticket::getSolution).setHeader("Solution").setSortable(true).setResizable(true);
        websiteColumn = grid.addColumn(Ticket::getWebsite).setHeader("Website").setSortable(true).setResizable(true);
        registeredByColumn = grid.addColumn(Ticket::getRegistered_by).setHeader("Ticket Owner").setSortable(true).setResizable(true);
        registerDateColumn = grid.addColumn(new LocalDateRenderer<>(Ticket::getRegister_date, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Register Date").setSortable(true).setResizable(true);
        lastUpdateColumn = grid.addColumn(new LocalDateRenderer<>(Ticket::getLast_update, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Last Update").setSortable(true).setResizable(true);
        closedDateColumn = grid.addColumn(new LocalDateRenderer<>(Ticket::getClose_date, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Closed Date").setSortable(true).setResizable(true);
        closedByColumn = grid.addColumn(Ticket::getClosed_by).setHeader("Closed By").setSortable(true).setResizable(true);

        grid.setColumnReorderingAllowed(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        GridContextMenu<Ticket> menu = grid.addContextMenu();
        menu.addItem("View Details", event -> {        });
        //menu.addItem("Edit Ticket", event -> editTicket(event.getItem().get()));
        //menu.addItem("Delete Ticket", event -> ConfirmAndDelete(event.getItem().get()));
        grid.asSingleSelect().addValueChangeListener(event -> viewTicket(event.getValue()));

        List<Ticket> ticket = ticketService.findAllTicketsByRegisteredBy(MainLayout.username);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(ticket);
        add(grid);
    }

    private static final SerializableBiConsumer<Span, Ticket> statusComponentUpdater = (span, ticket) -> {
        String status = ticket.getStatus();
        String theme = "";
        if (status.equals("Solved")) theme = String.format("badge %s", "success");
        else if (status.equals("Cancelled")) theme = String.format("badge %s", "error");
        else if (status.equals("Registered")) theme = String.format("badge %s", "warning");
        else if (status.equals("Assigned")) theme = String.format("badge %s", "contrast");
        else if (status.equals("In Progress")) theme = String.format("badge");
        span.getElement().setAttribute("theme", theme);
        span.setText(status);
    };

    private static ComponentRenderer<Span, Ticket> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private Component getToolbar() {
        descriptionFilterText.setPlaceholder("Filter by Description...");
        descriptionFilterText.setTooltipText("Please type what the description should contain...");
        descriptionFilterText.setClearButtonVisible(true);
        descriptionFilterText.setValueChangeMode(ValueChangeMode.LAZY);
        descriptionFilterText.addValueChangeListener(e -> updateListByDescription());

        Button myOpenTicketsButton = new Button("My Open Tickets");
        myOpenTicketsButton.addClickListener(click -> updateListByRegisteredByAndStatus(MainLayout.username, "Registered", "Assigned", "In Progress"));

        Button myClosedTicketsButton = new Button("My Closed Tickets");
        myClosedTicketsButton.addClickListener(click -> updateListByRegisteredByAndStatus(MainLayout.username, "Solved", "Cancelled", "NULL"));

        Button allTicketsButton = new Button("All My Tickets");
        allTicketsButton.addClickListener(click -> updateListByRegistered(MainLayout.username));


        Button menuButton = new Button("Show/Hide Columns");
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(menuButton);
        columnToggleContextMenu.addColumnToggleItem("Status", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Header", headerColumn);
        columnToggleContextMenu.addColumnToggleItem("Description", descriptionColumn);
//      History not displayed in grid, only in details
//      columnToggleContextMenu.addColumnToggleItem("History", historyColumn);
        columnToggleContextMenu.addColumnToggleItem("Solution", solutionColumn);
        columnToggleContextMenu.addColumnToggleItem("Website", websiteColumn);
        columnToggleContextMenu.addColumnToggleItem("Registered By", registeredByColumn);
        columnToggleContextMenu.addColumnToggleItem("Register Date", registerDateColumn);
        columnToggleContextMenu.addColumnToggleItem("Last Update", lastUpdateColumn);
        columnToggleContextMenu.addColumnToggleItem("Closed Date", closedDateColumn);
        columnToggleContextMenu.addColumnToggleItem("Closed By", closedByColumn);

        Button addTicketButton = new Button("Add ticket");
        //<theme-editor-local-classname>
        addTicketButton.addClassName("UserTicketView-button-1");
        addTicketButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTicketButton.addClickListener(click -> addTicket());

        var toolbar = new HorizontalLayout(descriptionFilterText, myOpenTicketsButton, myClosedTicketsButton, allTicketsButton, menuButton, addTicketButton);
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void closeEditor() {
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

    private void updateListByRegisteredByAndStatus(String name, String status1, String status2, String status3) {
        grid.setItems(ticketService.searchTicketsByUserAndStatus(name, status1, status2, status3));
    }
    private void updateListByRegistered(String username) {
        grid.setItems(ticketService.findAllTicketsByRegisteredBy(username));
    }
    private void updateListByDescription() {
        grid.setItems(ticketService.findAllTicketsByDescriptionAndRegisteredBy(descriptionFilterText.getValue(), MainLayout.username));
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
}