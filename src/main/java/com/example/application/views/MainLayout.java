package com.example.application.views;

import com.example.application.data.entity.Role;
import com.example.application.data.service.TUserService;
import com.example.application.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    public TUserService tUserService;
    public static String username;
    public static Role userRole;

    public MainLayout(SecurityService securityService, TUserService tUserService) {
        this.securityService = securityService;
        this.tUserService = tUserService;

        username = securityService.getAuthenticatedUser().getUsername();
        userRole = tUserService.findUserByUsername(username).getRole();
        String role = securityService.getAuthenticatedUser().getAuthorities().toString();
        com.vaadin.flow.component.notification.Notification notification = Notification
                .show("Username: " + username + "; Role: " + userRole.getRole_name() + "; Authorities: " + role);

        createHeader();
        createDrawer();
        //createDrawerCustomer();
    }

    private void createHeader() {
        H1 logo = new H1("Webst@rs Ticketing Application: Main");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                //new RouterLink("List", ListView.class),
                //new RouterLink("Dashboard", DashboardView.class), entfernt build fail coused by licence checker
                new RouterLink("Tickets", TicketView.class),
                new RouterLink("Users", CreateUserView.class),
                new RouterLink("Teams", TeamView.class),
                new RouterLink("Websites", WebsiteView.class)
        ));
    }

    private void createDrawerCustomer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Tickets", TeamView.class)
        ));
    }

}