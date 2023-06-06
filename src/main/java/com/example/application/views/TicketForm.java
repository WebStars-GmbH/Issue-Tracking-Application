package com.example.application.views;

import com.example.application.data.entity.*;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;

import java.util.List;

public class TicketForm extends FormLayout {
    TextField description = new TextField("Description");

    IntegerField priority = new IntegerField();


    ComboBox<Website> website = new ComboBox<>("Website");
    ComboBox<TUser> assigned_to = new ComboBox<>("Assigned to");

    TextField solution = new TextField("Solution");

    TextField status = new TextField("Status");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    //Button closeTicket = new Button("Close");
    Binder<Ticket> binder = new BeanValidationBinder<>(Ticket.class);

    public TicketForm(CrmService service, List<Website> websites, List<TUser>users) {
        setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.ASIDE));
        priority.setValue(2);
        priority.setStepButtonsVisible(true);
        priority.setMin(0);
        priority.setMax(2);

        addClassName("ticket-form");
        binder.bindInstanceFields(this);

        website.setItems(websites);
        website.setItemLabelGenerator(Website::getWebsite_name);

        users = service.findAllTUsersByRole("team_member");
        assigned_to.setItems(users);
        assigned_to.setItemLabelGenerator(TUser::getUsername);

        add(description,
                solution,
                website,
                assigned_to,
                priority,
                status,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        //closeTicket.addThemeVariants(ButtonVariant.LUMO_PRIMARY); TODO

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        //closeTicket.addClickListener(event -> closeTicket()); TODO

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
        //return new HorizontalLayout(save, delete, close, closeTicket); TODO
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setTicket(Ticket ticket) {

        binder.setBean(ticket);
    }

    // Events
    public static abstract class TicketFormEvent extends ComponentEvent<TicketForm> {
        private Ticket ticket;

        protected TicketFormEvent(TicketForm source, Ticket ticket) {
            super(source, false);
            this.ticket = ticket;
        }

        public Ticket getTicket() {
            return ticket;
        }
    }

    public static class SaveEvent extends TicketFormEvent {

        SaveEvent(TicketForm source, Ticket ticket) {
            super(source, ticket);
        }
    }

    public static class DeleteEvent extends TicketFormEvent {
        DeleteEvent(TicketForm source, Ticket ticket) {
            super(source, ticket);
        }

    }

    public static class CloseEvent extends TicketFormEvent {
        CloseEvent(TicketForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}