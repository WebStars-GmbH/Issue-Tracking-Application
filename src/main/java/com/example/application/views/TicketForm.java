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
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class TicketForm extends FormLayout {

    TextField header = new TextField("Header");

    TextArea description = new TextArea("Description");

    IntegerField priority = new IntegerField("Priority");

    ComboBox<Website> website = new ComboBox<>("Website");
    ComboBox<TUser> assigned_to = new ComboBox<>("Assigned to");

    TextArea solution = new TextArea("Solution");

    RadioButtonGroup<String> status = new RadioButtonGroup<>("Status");


    Button save = new Button("Save");
    Button delete = new Button("Delete Ticket");
    Button close = new Button("Close");
    TextArea history = new TextArea("History");

    Binder<Ticket> binder = new BeanValidationBinder<>(Ticket.class);

    public TicketForm(List<Website> websites, List<TUser>users) {

        description.setMaxLength(1000);
        description.setHelperText("Max 1000 characters");
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + 1000);
        });

        solution.setMaxLength(1000);
        solution.setHelperText("Max 1000 characters");
        solution.setValueChangeMode(ValueChangeMode.EAGER);
        solution.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + 1000);
        });

        setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.ASIDE));
        priority.setValue(2);
        priority.setStepButtonsVisible(true);
        priority.setMin(0);
        priority.setMax(3);
        priority.setHelperText("0: None. 1: Low. 2: Medium. 3: High.");
        priority.addThemeVariants(TextFieldVariant.LUMO_HELPER_ABOVE_FIELD);

        addClassName("ticket-form");
        binder.bindInstanceFields(this);
        binder.forField(header).bind(Ticket::getHeader, null);
        binder.forField(website).bind(Ticket::getWebsite, null);
        binder.forField(history).bind(Ticket::getHistory, null);

        status.setItems("Registered", "Assigned", "In Progress", "Cancelled", "Solved");
        status.setHelperText("Choose new status");
        status.addThemeVariants(RadioGroupVariant.LUMO_HELPER_ABOVE_FIELD);
        status.getStyle().set("--vaadin-input-field-border-width", "1px");

        website.setItems(websites);
        website.setItemLabelGenerator(Website::getWebsite_name);

        assigned_to.setItems(users);
        assigned_to.setItemLabelGenerator(TUser::getUsername);
        assigned_to.setHelperText("Available Support-Team members");
        status.setHelperText("Choose new status");
        assigned_to.addThemeVariants(ComboBoxVariant.LUMO_HELPER_ABOVE_FIELD);
        assigned_to.setClearButtonVisible(true);

        //history.setVisible(false);
        /*
        VerticalLayout content = new VerticalLayout(history);
        content.setSpacing(false);
        content.setPadding(false);
        Details details = new Details("History", content);
        details.setOpened(false);
        */

        add(header,
                website,
                description,
                solution,
                priority,
                assigned_to,
                status,
                createButtonsLayout(),
                //details,
                history
        );
    }

    public void updateAssignedTo(List<TUser> members){
        assigned_to.setItems(members);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        //save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> ConfirmAndDelete());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            ConfirmAndSave();
        }
    }

    private void ConfirmAndSave(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Save ticket?");
        dialog.setText("Are you sure you want to save this ticket?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Yes");
        dialog.setCancelText("No");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> fireEvent(new TicketForm.SaveEvent(this, binder.getBean())));
        dialog.open();
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
            Notification notification = Notification
                    .show("Ticket deleted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }

    private void ConfirmAndDelete(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Do you want to delete this ticket?");
        dialog.setText("Are you sure you want to permanently delete this ticket? This cannot be reversed.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete Ticket");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        dialog.open();
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