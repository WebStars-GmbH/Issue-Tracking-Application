package com.example.application.views;

import com.example.application.data.entity.TUser;
import com.example.application.data.entity.Team;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TeamService;
import com.example.application.data.service.TicketService;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;


@PermitAll
@Route(value = "dashboardSTC", layout = MainLayout.class)
@PageTitle("Dashboard Support-Team Coordinator | Webst@rs Ticketing Application")
public class DashboardSupportCoordinator extends VerticalLayout {

    private final TicketService ticketService;
    private final TUserService tUserService;
    private final TeamService teamService;

    List<Team> teams;

    Long averageSolveTime = Long.valueOf(0);


    public DashboardSupportCoordinator(TicketService ticketService, TUserService tUserService, TeamService teamService) {

        this.teams = teamService.findAllTeams("");
        this.ticketService = ticketService;
        this.tUserService = tUserService;
        this.teamService = teamService;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();

        HorizontalLayout charts = new HorizontalLayout();

        for (Team t : this.teams) {
            Stats teamStats = calculateStats(t);
            Chart chart = getTeamChartPie(teamStats);
            charts.add(chart);
        }
        add(charts);
    }

    class Stats {
        String teamName = "";
        int allTicketsCount = 0;
        int openTicketsCount = 0;
        int solvedTicketsCount = 0;
        int cancelledTicketsCount = 0;
    }

    private Stats calculateStats(Team team) {
        Stats teamStats = new Stats();
        teamStats.teamName = team.getName();
        for (TUser u : team.getTeam_members()) {
            teamStats.openTicketsCount += ticketService.openTicketCountByAssignedTo(u.getUsername());
            teamStats.solvedTicketsCount += ticketService.ticketCountByAssignedTo(u.getUsername(), "Solved");
            teamStats.cancelledTicketsCount += ticketService.ticketCountByAssignedTo(u.getUsername(), "Cancelled");
            teamStats.allTicketsCount += ticketService.ticketCountByAssignedTo(u.getUsername(), "");
        }
        return teamStats;
    }

    /*
    private Component getSupportTeamMemberStats() {
        String stats = ("User: " + MainLayout.username + "<br>Name: " + tUserService.findUserByUsername(MainLayout.username).getFirstname() + " " + tUserService.findUserByUsername(MainLayout.username).getLastname() + "<br><br>");
        stats += openTicketsCount + " open tickets<br>";
        if (solvedTicketsCount != 0) {
            stats += "Average solve time (since Assignment): " + TimeFormatUtility.millisecondsToTimeFormat(averageSolveTime);
        }
        Span openTicketsStats = new Span();
        openTicketsStats.getElement().setProperty("innerHTML", stats);
        openTicketsStats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return openTicketsStats;
    }
    */

    private Chart getTeamChartPie(Stats teamStats) {

        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        if (teamStats.allTicketsCount != 0) {
            conf.setTitle(teamStats.teamName + " Solving Rate: " + (teamStats.solvedTicketsCount * 100) / teamStats.allTicketsCount + "%");

            PlotOptionsPie plotOptions = new PlotOptionsPie();
            plotOptions.setDepth(45);
            conf.setPlotOptions(plotOptions);

            conf.getTitle().getStyle().setColor(SolidColor.GRAY);
            conf.getChart().setBackgroundColor(new SolidColor(255, 255, 255, 0));

            /*
            // In 3D!
            Options3d options3d = new Options3d();
            options3d.setEnabled(true);
            options3d.setAlpha(60);
            conf.getChart().setOptions3d(options3d);
             */

            DataSeries series = new DataSeries();
            series.add(new DataSeriesItem("Open Tickets", teamStats.openTicketsCount));
            series.add(new DataSeriesItem("Cancelled Tickets", teamStats.cancelledTicketsCount));
            series.add(new DataSeriesItem("Solved Tickets", teamStats.solvedTicketsCount));

            conf.addSeries(series);
        }
        return chart;
    }
}
