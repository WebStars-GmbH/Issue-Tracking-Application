package com.example.application.views;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Team;
import com.example.application.data.entity.Website;
import com.example.application.data.service.RoleService;
import com.example.application.data.service.SecurityUserDetailsService;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.WebsiteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "CreateUser", layout = MainLayout.class)
@PageTitle("Create User")
@PreserveOnRefresh
public class CreateUserView extends VerticalLayout {
    Grid<TUser> grid = new Grid<>(TUser.class);
    Checkbox showInactiveUsers = new Checkbox();
    TextField filterText = new TextField();
    CreateUserForm form;
    TUserService userService;

    //public static String username;

    WebsiteService websiteService;
    RoleService roleService;

    private final SecurityUserDetailsService sUDservice;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CreateUserView(TUserService userService, WebsiteService websiteService, RoleService roleService, SecurityUserDetailsService sUDservice) {
        this.userService = userService;
        this.websiteService = websiteService;
        this.roleService = roleService;
        this.sUDservice = sUDservice;

        addClassName("user-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();

    }


    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumns( "firstname", "lastname", "username", "email", "role", "active" );

        grid.addColumn(user -> user.getTeams().stream()
                        .map(Team::getName)
                        .collect(Collectors.joining(", ")))
                .setHeader("Teams");

        grid.addColumn(user -> user.getWebsites().stream()
                        .map(Website::getWebsite_name)
                        .collect(Collectors.joining(", ")))
                .setHeader("Websites");

/*        grid.asSingleSelect().addValueChangeListener(event ->
                editUser(event.getValue()));
*/
        // only System-Admin can edit users
        if (userService.findUserByUsername(MainLayout.username).getRole().getRole_name().equals("System-Admin")) {
            grid.asSingleSelect().addValueChangeListener(event ->
                    editUser(event.getValue()));
        }

   }

    private void editUser(TUser user) {
        if (user == null) {
            closeEditor();
        } else {
            if (user.getUsername() != null){
                if (user.getUsername().equals(MainLayout.username)){
                    Notification notification = Notification.show("ERROR. You're trying to edit the logged in user: " + MainLayout.username);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    closeEditor();
                    return;
                }
                user = userService.findUserById(user.getId()); //reset user data
            }
            form.setUser(user);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void configureForm() {
        if (form == null) {
            form = new CreateUserForm(websiteService, roleService);
            form.addSaveListener(this::saveUser);
            form.addDeleteListener(this::deleteUser);
            form.addCloseListener(e -> closeEditor());
        }
    }

    private void updateWebsites(TUser user){
        //check if the websites assigned to the user have been changed, and update all website entities if that is the case
        List<Website> websitesOld = websiteService.getAllWebsitesByUser(user);
        List<Website> websitesNew = user.getWebsites();

        if (!websitesOld.equals(websitesNew)) {
            for (Website w : websitesOld) {
                if (!websitesNew.contains(w)) {
                    w.deleteUser();
                    websiteService.saveWebsite(w);
                }
            }
            for (Website w : websitesNew) {
                if (!websitesOld.contains(w)) {
                    w.deleteUser();
                    w.setUser(user);
                    websiteService.saveWebsite(w);
                }
            }
            UI.getCurrent().getPage().reload(); //Page needs to be reloaded for all changes to take place correctly
        }
    }
    private void saveUser(CreateUserForm.SaveEvent event) {

        TUser user = event.getUser();

        //When creating user, check if username is unique
        if (user.getId() == null && userService.findUserByUsername(user.getUsername()) != null){
            Notification notification = Notification.show("ERROR. Cannot create user. There already exists a user with this username.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        //When editing user, check if username is unique
        if (user.getId() != null && userService.findUserByUsername(user.getUsername()) != null && user.getId().intValue() != userService.findUserByUsername(user.getUsername()).getId().intValue()){
            Notification notification = Notification.show("ERROR modifying user with id " + user.getId() +". Cannot modify username. There already exists a user with this username. Userid: " + userService.findUserByUsername(user.getUsername()).getId());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        //Check if user already exits and if password has been changed
        String oldPassword = "";
        if (user.getId() != null) oldPassword = userService.findUserById(user.getId()).getPassword();
        if (user.getPassword() != oldPassword) {    //if so, encrypt the new password and update the DB
            String newPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(newPassword);
            user.setPasswordConfirm(newPassword);
        }

        userService.saveUser(user);
        updateList();
        closeEditor();

        updateWebsites(user);

        Notification notification = Notification
                .show("User " + user.getUsername() + " updated!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void deleteUser(CreateUserForm.DeleteEvent event) {
        userService.deleteUser(event.getUser().getId());
        updateList();
        closeEditor();
    }
    private void updateList() {
        String searchTerm = filterText.getValue().trim(); // fetch searchterm

/* old search without checkbox for inactive users
        if (searchTerm.isEmpty()) {
            grid.setItems(userService.findAllUsers()); // alle Anzeigen
        } else {
            List<TUser> filteredUsers = userService.findUsersBySearchTerm(searchTerm); // Suche
            grid.setItems(filteredUsers);
        }
 */

        // combination search and checkbox for inactive users
        if (searchTerm.isEmpty() && showInactiveUsers.getValue()) {
            grid.setItems(userService.findAllUsers()); // all users (active + inactive)
        } else if (searchTerm.isEmpty() && !showInactiveUsers.getValue()) {
            grid.setItems(userService.findAllActiveUsers()); // only active users
        } else if (!searchTerm.isEmpty() && showInactiveUsers.getValue()) {
            List<TUser> filteredUsers = userService.findAllUsersBySearchTerm(searchTerm);
            grid.setItems(filteredUsers); // all users (active + inactive) according to searchterm
        } else if (!searchTerm.isEmpty() && !showInactiveUsers.getValue()) {
            List<TUser> filteredUsers = userService.findActiveUsersBySearchTerm(searchTerm);
            grid.setItems(filteredUsers); // all active users according to searchterm
        }
    }
    private Component getToolbar() {
        showInactiveUsers.setLabel("show inactive users");
        add(showInactiveUsers);
        showInactiveUsers.addValueChangeListener(e -> updateList());

        filterText.setPlaceholder("search...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        //only System-Admin can create new users, other internal roles can only search the user list
        if (userService.findUserByUsername(MainLayout.username).getRole().getRole_name().equals("System-Admin")) {
            Button addTicketButton = new Button("Create User");
            addTicketButton.addClickListener(click -> addUser());

            var toolbar = new HorizontalLayout(filterText, addTicketButton);
            toolbar.addClassName("toolbar");
            return toolbar;
        } else {
            var toolbar = new HorizontalLayout(filterText);
            toolbar.addClassName("toolbar");
            return toolbar;
        }

    }

    private void addUser() {
        grid.asSingleSelect().clear();
        TUser user = new TUser();
        editUser(user);
    }


}