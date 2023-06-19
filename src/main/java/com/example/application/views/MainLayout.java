package com.example.application.views;

import com.example.application.data.entity.Role;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TeamService;
import com.example.application.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

//user angelegt (password):
// user
// admin
// member0
// coordinator
// manager

import java.util.List;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    public TUserService tUserService;
    public TeamService teamService;
    public static String username;
    public static Role userRole;

    public MainLayout(SecurityService securityService, TUserService tUserService, TeamService teamService) {
        this.securityService = securityService;
        this.tUserService = tUserService;
        this.teamService = teamService;

        username = securityService.getAuthenticatedUser().getUsername();
        userRole = tUserService.findUserByUsername(username).getRole();

        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Webst@rs Ticketing Application: Main");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());
        Boolean enabled = securityService.getAuthenticatedUser().isEnabled();

        if (!enabled) return;

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
        //Intern3t says Hashmap has too much overhead for small amount of data
        Role userRoleEntity = tUserService.findUserByUsername(username).getRole();

        if (userRoleEntity.getRole_name().equals("Customer")) {
            addToDrawer(new VerticalLayout(
//                    new RouterLink("Dashboard", DashboardView.class),
                    new RouterLink("Tickets", UserTicketView.class)
            ));
        } else  {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Dashboard", DashboardView.class),
                    new RouterLink("Tickets", CompanyTicketView.class),
                    new RouterLink("Users", CreateUserView.class),
                    new RouterLink("Teams", TeamView.class),
                    new RouterLink("Websites", WebsiteView.class)
            ));
        }
    }

/*    //or like this with a new function for every role and the if in the mainlayout function
    private void createDrawerCustomer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Tickets", UserTicketView.class)
                new RouterLink("Tickets", TeamView.class)
        ));
    }*/

}