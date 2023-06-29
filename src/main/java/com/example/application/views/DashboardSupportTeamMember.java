package com.example.application.views;

import com.example.application.data.entity.Ticket;
import com.example.application.data.service.TUserService;
import com.example.application.data.service.TeamService;
import com.example.application.data.service.TicketService;
import com.example.application.data.service.TimeFormatUtility;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.List;


@PermitAll
@Route(value = "dashboardSTM", layout = MainLayout.class)
@PageTitle("Dashboard Support-Team Member | Webst@rs Ticketing Application")
public class DashboardSupportTeamMember extends VerticalLayout {

    private final TicketService ticketService;
    private final TUserService tUserService;
    private final TeamService teamService;

    List<Ticket> allTickets;
    List<Ticket> openTickets;
    List<Ticket> solvedTickets;
    List<Ticket> cancelledTickets;

    Long averageSolveTime = Long.valueOf(0);


    public DashboardSupportTeamMember(TicketService ticketService, TUserService tUserService, TeamService teamService) {

        this.ticketService = ticketService;
        this.tUserService = tUserService;
        this.teamService = teamService;

        calculateStats();

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();

        HorizontalLayout charts = new HorizontalLayout();
        charts.add(getMemberChartPie(), getMemberChartSpeed());
        add(getSupportTeamMemberStats(), charts);
    }

    private void calculateStats(){
        allTickets = ticketService.findAllTicketsByAssignedTo(MainLayout.username);
        openTickets = ticketService.findAllTicketsByAssignedToAndStatus(MainLayout.username, "Registered", "Assigned", "In progress");
        solvedTickets = ticketService.findAllTicketsByAssignedToAndStatus(MainLayout.username, "Solved", "Solved", "Solved");
        cancelledTickets = ticketService.findAllTicketsByAssignedToAndStatus(MainLayout.username, "Cancelled", "Cancelled", "Cancelled");
        if (solvedTickets.size() != 0) {
            for (Ticket t : solvedTickets) averageSolveTime += t.getTimeBetweenAssignedAndSolved();
            averageSolveTime = averageSolveTime/solvedTickets.size();
        }
    }

    private Component getSupportTeamMemberStats() {
        addClassName("dashboard-view");
        String stats = ("User: " + MainLayout.username + "<br>Name: " + tUserService.findUserByUsername(MainLayout.username).getFirstname() + " " + tUserService.findUserByUsername(MainLayout.username).getLastname() + "<br><br>");
        stats += openTickets.size() + " open tickets<br>";
        if (solvedTickets.size() != 0) {
            stats += solvedTickets.size() + " solved tickets<br>";
            for (Ticket t : solvedTickets) averageSolveTime += t.getTimeBetweenAssignedAndSolved();
            averageSolveTime = averageSolveTime/solvedTickets.size();
            stats += "Average solve time (since Assignment): " + TimeFormatUtility.millisecondsToTimeFormat(averageSolveTime);
        }
        Span openTicketsStats = new Span();
        openTicketsStats.getElement().setProperty("innerHTML", stats);
        openTicketsStats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return openTicketsStats;
    }

    private Chart getMemberChartPie() {
        addClassName("dashboard-view");
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Solving Rate: " + (solvedTickets.size()*100)/allTickets.size() + "%");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setDepth(45);
        conf.setPlotOptions(plotOptions);

        // In 3D!
        Options3d options3d = new Options3d();
        options3d.setEnabled(true);
        options3d.setAlpha(60);

        conf.getChart().setOptions3d(options3d);

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Solved Tickets", solvedTickets.size()));
        series.add(new DataSeriesItem("Open Tickets", openTickets.size()));
        conf.addSeries(series);
        return chart;
    }
    private Chart getMemberChartSpeed() {
        addClassName("dashboard-view");
        Chart chart = new Chart(ChartType.GAUGE);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Average Solve Time");
        conf.getPane().setStartAngle(-135);
        conf.getPane().setEndAngle(135);

        YAxis yaxis = new YAxis();
        yaxis.setTitle("Days");
        yaxis.setMin(0);
        yaxis.setMax(30);

        PlotBand good = new PlotBand(0, 10, new SolidColor("#7FFF00"));
        PlotBand bad = new PlotBand(10, 20, new SolidColor("#F0F8FF"));
        PlotBand ugly = new PlotBand(20, 30, new SolidColor("#FFA07A"));

        yaxis.setPlotBands(good, bad, ugly);

        conf.addyAxis(yaxis);

        ListSeries series = new ListSeries("Average Solve Time", (averageSolveTime/86400000));
        conf.addSeries(series);
        return chart;
    }
}