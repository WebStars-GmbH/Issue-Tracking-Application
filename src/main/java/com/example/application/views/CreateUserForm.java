package com.example.application.views;

import com.example.application.data.entity.*;
import com.example.application.data.service.WebsiteService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateUserForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField username = new TextField("User name");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    PasswordField passwordConfirm = new PasswordField("Confirm password");
    ComboBox<String> role = new ComboBox<>("Role");
    ComboBox<Website> website = new ComboBox<>("Website");

    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<TUser> binder = new BeanValidationBinder<>(TUser.class);

    @Autowired
    public CreateUserForm(WebsiteService websiteService) {
        addClassName("user-form");

        role.setItems("Customer", "Support-Coordinator", "Support-Member", "System-Admin", "Management");  // Add as many roles as you need
        binder.bindInstanceFields(this);


        website.setItems(websiteService.getAllWebsites());


        add(firstName,
                lastName,
                username,
                email,
                role,
                website,
                password,
                passwordConfirm,
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
        if (binder.isValid()) {
            if (password.getValue().equals(passwordConfirm.getValue())) {
                fireEvent(new SaveEvent(this, binder.getBean()));
            } else {
                passwordConfirm.setErrorMessage("Passwords do not match!");
                passwordConfirm.setInvalid(true);
            }
        }
    }


    public void setUser(TUser user) {
        binder.setBean(user);
    }

    // Events
    public static abstract class UserFormEvent extends ComponentEvent<CreateUserForm> {
        private final TUser user;

        protected UserFormEvent(CreateUserForm source, TUser user) {
            super(source, false);
            this.user = user;
        }

        public TUser getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(CreateUserForm source, TUser user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(CreateUserForm source) {
            super(source, null);
        }
    }

    public static class DeleteEvent extends ComponentEvent<CreateUserForm> {
        private final TUser user;

        DeleteEvent(CreateUserForm source, TUser user) {
            super(source, false);
            this.user = user;
        }

        public TUser getUser() {
            return user;
        }
    }


    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }
}
