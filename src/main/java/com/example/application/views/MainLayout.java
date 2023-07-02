package com.example.application.views;

import com.example.application.data.entity.Role;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TeamService;
import com.example.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;


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

/*    private void createDrawer() {
            Role userRoleEntity = tUserService.findUserByUsername(username).getRole();

        if (userRoleEntity.getRole_name().equals("Customer")) {
            addToDrawer(new VerticalLayout(
                    //new RouterLink("Dashboard", DashboardView.class),
                    new RouterLink("My Tickets", UserTicketView.class)
            ));
        }
        else if (userRoleEntity.getRole_name().equals("Support-Member")) {
            addToDrawer(new VerticalLayout(
                    new RouterLink("My Statistics", DashboardSupportTeamMember.class),
                    new RouterLink("My Tickets", CompanyTicketView.class)
            ));
        }
        else if (userRoleEntity.getRole_name().equals("Support-Coordinator")) {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Team Statistics", DashboardSupportCoordinator.class),
                    new RouterLink("Manage Teams", TeamView.class),
                    new RouterLink("Tickets", CompanyTicketView.class)
            ));
        }
        else if (userRoleEntity.getRole_name().equals("Management")) {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Company Statistics", DashboardView.class),
                    new RouterLink("Tickets", CompanyTicketView.class)
            ));
        }
        else  {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Dashboard", DashboardView.class),
                    new RouterLink("Users", CreateUserView.class),
                    new RouterLink("Teams", TeamView.class),
                    new RouterLink("Websites", WebsiteView.class),
                    new RouterLink("Tickets", CompanyTicketView.class)
            ));
        }

    }*/



// ...



// ...

    private void createDrawer() {
        Role userRoleEntity = tUserService.findUserByUsername(username).getRole();

        if (userRoleEntity.getRole_name().equals("Customer")) {
            addToDrawer(new VerticalLayout(
                    createDrawerLink("My Tickets", VaadinIcon.TICKET, UserTicketView.class)
            ));
        }
        else if (userRoleEntity.getRole_name().equals("Support-Member")) {
            addToDrawer(new VerticalLayout(
                    createDrawerLink("My Statistics", VaadinIcon.CHART, DashboardSupportTeamMember.class),
                    createDrawerLink("My Tickets", VaadinIcon.TICKET, CompanyTicketView.class)
            ));
        }
        else if (userRoleEntity.getRole_name().equals("Support-Coordinator")) {
            addToDrawer(new VerticalLayout(
                    createDrawerLink("Team Statistics", VaadinIcon.CHART, DashboardSupportCoordinator.class),
                    createDrawerLink("Manage Teams", VaadinIcon.USER, TeamView.class),
                    createDrawerLink("Tickets", VaadinIcon.TICKET, CompanyTicketView.class)
            ));
        }
        else if (userRoleEntity.getRole_name().equals("Management")) {
            addToDrawer(new VerticalLayout(
                    createDrawerLink("Company Statistics", VaadinIcon.CHART, DashboardView.class),
                    createDrawerLink("Tickets", VaadinIcon.TICKET, CompanyTicketView.class)
            ));
        }
        else {
            addToDrawer(new VerticalLayout(
                    createDrawerLink("Dashboard", VaadinIcon.DASHBOARD, DashboardView.class),
                    createDrawerLink("Users", VaadinIcon.USER, CreateUserView.class),
                    createDrawerLink("Teams", VaadinIcon.USERS, TeamView.class),
                    createDrawerLink("Websites", VaadinIcon.GLOBE, WebsiteView.class),
                    createDrawerLink("Tickets", VaadinIcon.TICKET, CompanyTicketView.class)
            ));
        }
    }

    private HorizontalLayout createDrawerLink(String text, VaadinIcon icon, Class<?> navigationTarget) {
        Icon vaadinIcon = new Icon(icon);
        RouterLink link = new RouterLink(text, (Class<? extends Component>) navigationTarget);

        HorizontalLayout layout = new HorizontalLayout(vaadinIcon, link);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setSpacing(true);
        layout.setPadding(false);
        layout.setMargin(false);

        return layout;
    }


}
