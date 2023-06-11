package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Team;
import com.example.application.data.entity.Website;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class WebsiteForm extends FormLayout {
    TextField website_name = new TextField("Name");

    TextField url = new TextField("URL");

    ComboBox<TUser> tuser = new ComboBox<>("Owner");

    ComboBox<Team> team = new ComboBox<>("Team");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Website> binder = new BeanValidationBinder<>(Website.class);

    public WebsiteForm(List<Team> teams, List<TUser> users) {
        addClassName("website-form");
        binder.bindInstanceFields(this);
        binder.forField(tuser).bind(Website::getUser, Website::setUser);

        team.setItems(teams);
        team.setItemLabelGenerator(Team::getName);

        tuser.setItems(users);
        tuser.setItemLabelGenerator(TUser::getUsername);

        add(website_name,
                tuser,
                team,
                url,
                createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> ConfirmAndDelete());
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        //return new HorizontalLayout(save, delete, close);
        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }


    public void setWebsite(Website website) {
        binder.setBean(website); // <1>
    }

    // Events
    public static abstract class WebsiteFormEvent extends ComponentEvent<WebsiteForm> {
        private Website website;

        protected WebsiteFormEvent(WebsiteForm source, Website website) {
            super(source, false);
            this.website = website;
        }

        public Website getWebsite() {
            return website;
        }
    }

    public static class SaveEvent extends WebsiteFormEvent {
        SaveEvent(WebsiteForm source, Website website) {
            super(source, website);
        }
    }

    public static class DeleteEvent extends WebsiteForm.WebsiteFormEvent {
        DeleteEvent(WebsiteForm source, Website website) {
            super(source, website);
        }
    }

    private void ConfirmAndDelete(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Do you want to delete this Website?");
        dialog.setText("Are you sure you want to permanently delete this Website? This cannot be reversed.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete Website");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> fireEvent(new WebsiteForm.DeleteEvent(this, binder.getBean())));
        dialog.open();
    }

    public static class CloseEvent extends WebsiteFormEvent {
        CloseEvent(WebsiteForm source) {
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


