package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.WebsiteService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import com.example.application.data.entity.Website;


import java.util.List;
import java.util.stream.Collectors;


@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "CreateUser", layout = MainLayout.class)
@PageTitle("Create User")
public class CreateUserView extends VerticalLayout {
    Grid<TUser> grid = new Grid<>(TUser.class);
    TextField filterText = new TextField();
    CreateUserForm form;
    TUserService userService;
    WebsiteService websiteService;

    public CreateUserView(TUserService userService, WebsiteService websiteService) {
        this.userService = userService;
        this.websiteService = websiteService;
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
        grid.setColumns( "firstname", "lastname", "username", "email", "role" );  // Set the columns that you want to display in your grid
       // grid.addColumn("websites");
        //grid.addColumn(user -> user.getWebsites().getWebsite_name().setHeader("Website").setSortable(true);
        grid.addColumn(user -> user.getWebsites().stream()
                        .map(Website::getWebsite_name)
                        .collect(Collectors.joining(", ")))
                .setHeader("Websites");
        // You can also add a click listener to handle row clicks
        grid.asSingleSelect().addValueChangeListener(event ->
                editUser(event.getValue()));
    }



    private void editUser(TUser user) {
        if (user == null) {
            closeEditor();
        } else {
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
            form = new CreateUserForm(websiteService);
            form.addSaveListener(this::saveUser);
            form.addDeleteListener(this::deleteUser);
            form.addCloseListener(e -> closeEditor());
        }
    }

    private void saveUser(CreateUserForm.SaveEvent event) {
        TUser user = event.getUser();
        // Zuerst den Benutzer speichern
        userService.saveUser(user);

        List<Website> selectedWebsites = user.getWebsites();
        if (selectedWebsites != null) {
            for (Website website : selectedWebsites) {
                website.setUser(user);
                // Dann die Website speichern
                websiteService.saveWebsite(website);
            }
        }
        updateList();
        closeEditor();
    }

    private void deleteUser(CreateUserForm.DeleteEvent event) {
        userService.deleteUser(event.getUser().getId());
        updateList();
        closeEditor();
    }
    private void updateList() {
        grid.setItems(userService.findAllUsers());
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addTicketButton = new Button("Create User");
        addTicketButton.addClickListener(click -> addUser());

        var toolbar = new HorizontalLayout(filterText, addTicketButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new TUser());
    }

}