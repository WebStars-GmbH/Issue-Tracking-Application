package com.example.application.views;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Website;
import com.example.application.data.service.EmailCheckUtility;
import com.example.application.data.service.RoleService;
import com.example.application.data.service.WebsiteService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class CreateUserForm extends FormLayout {

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField username = new TextField("User name");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    PasswordField passwordConfirm = new PasswordField("Confirm password");
    ComboBox<Role> role = new ComboBox<>("Role");
    MultiSelectComboBox<Website> websitesNew = new MultiSelectComboBox<>("Websites");

    Checkbox active = new Checkbox("Active");

    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<TUser> binder = new BeanValidationBinder<>(TUser.class);


    public CreateUserForm(WebsiteService websiteService, RoleService roleService) {
        addClassName("user-form");

        binder.bindInstanceFields(this);

        websitesNew.setItems(websiteService.getAllWebsitesWithEmptyUser());

        role.setItems(roleService.getAllRoles());
        role.setItemLabelGenerator(Role::getRole_name);

        binder.forField(websitesNew).<List<Website>> withConverter(ArrayList::new, HashSet::new).bind(TUser::getWebsites, TUser::setWebsites);

        username.setValueChangeMode(ValueChangeMode.EAGER);
        email.setValueChangeMode(ValueChangeMode.EAGER);
        password.setValueChangeMode(ValueChangeMode.EAGER);
        passwordConfirm.setValueChangeMode(ValueChangeMode.EAGER);

        binder.addStatusChangeListener(status -> {
            if (!EmailCheckUtility.isValid(email.getValue())) email.setHelperText("Not a valid email address...");
            else email.setHelperText("");

            if (!password.getValue().equals(passwordConfirm.getValue())) passwordConfirm.setHelperText("Passwords don't match");
            else password.setHelperText("");

            if (!binder.isValid() || !password.getValue().equals(passwordConfirm.getValue())) save.setText("...");
            else save.setText("Save");
        });

        add(firstName,
                lastName,
                username,
                email,
                role,
                websitesNew,
                password,
                passwordConfirm,
                active,
                createButtonsLayout());
    }


    private class decrypt implements Converter<String, String> {
        @Override
        public Result<String> convertToModel(
                String fieldValue, ValueContext context) {
            return null;
        }

        @Override
        public String convertToPresentation(
                String string, ValueContext context) {
            return string + "hallo";
        }
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
