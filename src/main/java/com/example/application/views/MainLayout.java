package com.example.application.views;

import com.example.application.data.entity.Role;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TeamService;
import com.example.application.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.applayout.AppLayout;


//user angelegt (password):
// user
// admin
// member0
// coordinator
// manager

import java.util.List;

//user angelegt (password):
// user
// admin
// member0
// coordinator
// manager

import java.util.List;

public class MainLayout extends AppLayout {
    // Dark Theme
    Button darkThemetoggleButton = new Button("Toggle theme variant", click -> {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if (themeList.contains(Lumo.DARK)) {
            themeList.remove(Lumo.DARK);
            updateLogo(false);
        } else {
            themeList.add(Lumo.DARK);
            updateLogo(true);
        }
    });
    private void updateLogo(boolean isDarkTheme) {
        String logoLightPath = "images/logo.png";
        String logoDarkPath = "images/logo-white.png";

        if (isDarkTheme) {
            logo.setSrc(logoDarkPath);
        } else {
            logo.setSrc(logoLightPath);
        }
    }
    private final SecurityService securityService;
    public static TUserService tUserService;
    public TeamService teamService;
    public static String username;
    public static Role userRole;
    private Image logo;
    public MainLayout(SecurityService securityService, TUserService tUserService, TeamService teamService) {
        this.securityService = securityService;
        this.tUserService = tUserService;
        this.teamService = teamService;

        username = securityService.getAuthenticatedUser().getUsername();
        userRole = tUserService.findUserByUsername(username).getRole();

        setDarkTheme();
        createHeader();
        createDrawer();
    }
    private void setDarkTheme() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        themeList.add(Lumo.DARK);
    }

    private void createHeader() {
        H1 h1 = new H1("Ticketing Application");
        h1.addClassNames(
                LumoUtility.FontSize.XXLARGE,
                LumoUtility.Margin.MEDIUM);


        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        // Pfade zu den Logo-Bildern
        String logoLightPath = "images/logo.png";
        String logoDarkPath = "images/logo-white.png";

        // Dark Theme überprüfen und das entsprechende Logo setzen
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        boolean isDarkTheme = themeList.contains(Lumo.DARK);

        logo = new Image();
        updateLogo(isDarkTheme);
        // Anpassen der Breite des Logos
        logo.setMaxWidth("12%");



        var header = new HorizontalLayout(new DrawerToggle(), logo, h1, darkThemetoggleButton, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(h1);
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
                    new RouterLink("Dashboard", DashboardView.class),
                    new RouterLink("Tickets", UserTicketView.class)
            ));
 /* alternative if other internal roles than sys-admin should not see the list of users > if used line new RouterLink("Users", CreateUserView.class), in following else-block has to be deleted
        } else if (userRoleEntity.getRole_name().equals("System-Admin")) {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Dashboard", DashboardView.class),
                    new RouterLink("Tickets", CompanyTicketView.class),
                    new RouterLink("Users", CreateUserView.class),
                    new RouterLink("Teams", TeamView.class),
                    new RouterLink("Websites", WebsiteView.class)
            ));

  */
        }
        else  {
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

