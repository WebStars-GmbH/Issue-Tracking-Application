package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Ticket;
import com.example.application.data.entity.Website;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class TicketDetailsForm extends FormLayout {

    boolean showEditButton = false;
    TextField header = new TextField("Header");
    TextField registered_by = new TextField("Registered by");
    TextField register_date_String = new TextField("Register Date");
    TextField last_update_String = new TextField("Last Update");
    TextArea description = new TextArea("Description");
    IntegerField priority = new IntegerField("Priority");
    TextField website_name = new TextField("Website");
    TextField assigned_to_username = new TextField("Assigned to");
    TextArea solution = new TextArea("Solution");
    TextField close_date_String = new TextField("Close Date");
    TextField status = new TextField("Status");
    Button edit = new Button("Edit");
    Button close = new Button("Close");
    TextArea history = new TextArea("History");
    Binder<Ticket> binder = new BeanValidationBinder<>(Ticket.class);

    public TicketDetailsForm(List<Website> websites, List<TUser>users, boolean showEditButton) {
        this.showEditButton = showEditButton;
        priority.setHelperText("0: None. 1: Low. 2: Medium. 3: High.");

        addClassName("ticket-form");
        binder.bindInstanceFields(this);

        binder.forField(website_name).bind(Ticket::getWebsite_name, null);
        binder.forField(assigned_to_username).bind(Ticket::getAssigned_to_username, null);
        binder.forField(register_date_String).bind(Ticket::getRegisterDateString, null);
        binder.forField(last_update_String).bind(Ticket::getLastUpdateString, null);
        binder.forField(close_date_String).bind(Ticket::getClosedDateString, null);

        header.setReadOnly(true);
        registered_by.setReadOnly(true);
        description.setReadOnly(true);
        solution.setReadOnly(true);
        status.setReadOnly(true);
        history.setReadOnly(true);
        priority.setReadOnly(true);

        add(header,
                registered_by,
                register_date_String,
                last_update_String,
                website_name,
                description,
                solution,
                close_date_String,
                priority,
                assigned_to_username,
                status,
                history,
                createButtonsLayout()
        );
    }

    private HorizontalLayout createButtonsLayout() {
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);
        edit.addClickListener(event -> {
                    Ticket t = binder.getBean();
                    if (t == null) return;
                    if (MainLayout.userRole.getRole_name().equals("System-Admin") || MainLayout.userRole.getRole_name().equals("Support-Coordinator")) fireEvent(new EditEvent(this));
                    else if (t.getAssigned_to() != null && t.getAssigned_to().getUsername().equals(MainLayout.username)) fireEvent(new EditEvent(this));
                    else {
                        Notification n = Notification.show("This ticket is not assigned to you. Editing is not allowed.");
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                });
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        if (showEditButton) return new HorizontalLayout(edit, close);
        else return new HorizontalLayout(close);
    }

    public void goToEdit(){

    }
    public void setTicket(Ticket ticket) {
        binder.setBean(ticket);
    }

    // Events
    public static abstract class TicketFormEvent extends ComponentEvent<TicketDetailsForm> {
        private Ticket ticket;
        protected TicketFormEvent(TicketDetailsForm source, Ticket ticket) {
            super(source, false);
            this.ticket = ticket;
        }
        public Ticket getTicket() {
            return ticket;
        }
    }

    public static class CloseEvent extends TicketFormEvent {
        CloseEvent(TicketDetailsForm source) {
            super(source, null);
        }
    }

    public static class EditEvent extends TicketFormEvent {
        EditEvent(TicketDetailsForm source) {
            super(source, null);
        }
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }
}