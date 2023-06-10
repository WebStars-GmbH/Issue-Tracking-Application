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
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TeamForm extends FormLayout {
    TextField name = new TextField("Name");

    //MultiSelectComboBox<Website> websitesCB = new MultiSelectComboBox<>("Websites"); //TODO
    MultiSelectComboBox<TUser> membersCB = new MultiSelectComboBox<>("Team Members");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    // Other fields omitted
    Binder<Team> binder = new BeanValidationBinder<>(Team.class);

    public TeamForm(List<Website> websites, List<TUser> members) {
        addClassName("team-form");
        binder.bindInstanceFields(this);

        //binder.forField(websitesCB).<List<Website>> withConverter(ArrayList::new, HashSet::new).bind(Team::getWebsites, Team::setWebsites);

        binder.forField(membersCB).<List<TUser>> withConverter(ArrayList::new, HashSet::new).bind(Team::getTeam_members, Team::setTeam_members);

        //websitesCB.setItems(websites);
        //websitesCB.setItemLabelGenerator(Website::getWebsite_name);
        membersCB.setItems(members);
        membersCB.setItemLabelGenerator(TUser::getUsername);



        add(name,
                //websitesCB,
                membersCB,
                createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // <2>
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }


    public void setTeam(Team team) {
        binder.setBean(team); // <1>
    }

    // Events
    public static abstract class TeamFormEvent extends ComponentEvent<TeamForm> {
        private Team team;

        protected TeamFormEvent(TeamForm source, Team team) {
            super(source, false);
            this.team = team;
        }

        public Team getTeam() {
            return team;
        }
    }

    public static class SaveEvent extends TeamFormEvent {
        SaveEvent(TeamForm source, Team team) {
            super(source, team);
        }
    }

    public static class DeleteEvent extends TeamFormEvent {
        DeleteEvent(TeamForm source, Team team) {
            super(source, team);
        }

    }

    public static class CloseEvent extends TeamFormEvent {
        CloseEvent(TeamForm source) {
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


