package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Ticket;
import com.example.application.data.entity.Website;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class TicketAddForm extends FormLayout {
    TextField header = new TextField("Header");
    TextArea description = new TextArea("Description");

    ComboBox<Website> website = new ComboBox<>("Website");

    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Ticket> binder = new BeanValidationBinder<>(Ticket.class);

    public TicketAddForm(List<Website> websites, List<TUser>users) {

        addClassName("ticket-form");
        binder.bindInstanceFields(this);

        website.setAllowCustomValue(true);
        website.setItems(websites);
        website.setItemLabelGenerator(Website::getWebsite_name);

        add(header,
                website,
                description,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
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
    public static abstract class TicketFormEvent extends ComponentEvent<TicketAddForm> {
        private Ticket ticket;

        protected TicketFormEvent(TicketAddForm source, Ticket ticket) {
            super(source, false);
            this.ticket = ticket;
        }

        public Ticket getTicket() {
            return ticket;
        }
    }

    public static class SaveEvent extends TicketFormEvent {

        SaveEvent(TicketAddForm source, Ticket ticket) {
            super(source, ticket);
            Notification notification = Notification
                    .show("Ticket successfully created!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }

    public static class DeleteEvent extends TicketFormEvent {
        DeleteEvent(TicketAddForm source, Ticket ticket) {
            super(source, ticket);
        }

    }

    public static class CloseEvent extends TicketFormEvent {
        CloseEvent(TicketAddForm source) {
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