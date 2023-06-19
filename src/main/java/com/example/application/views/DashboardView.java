package com.example.application.views;

import com.example.application.data.service.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;



@PermitAll
@Route(value = "dashboard", layout = MainLayout.class) // <1>
@PageTitle("Dashboard | Webst@rs Ticketing Application")
public class DashboardView extends VerticalLayout {
    private final CrmService service;
    private final TicketService ticketService;
    private final TUserService tUserService;
    private final WebsiteService websiteService;
    private final TeamService teamService;


    public DashboardView(CrmService service, TicketService ticketService, TUserService tUserService, WebsiteService websiteService, TeamService teamService) { // <2>
        this.service = service;
        this.ticketService = ticketService;
        this.tUserService = tUserService;
        this.teamService = teamService;
        this.websiteService = websiteService;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // <3>

        add(getWebsitesAllTicketsChart(), getWebsitesOpenTicketsChart(), getWebsitesSolvedTicketsChart());
    }

    private Component getTicketStats() {
        Span allTickets = new Span(ticketService.countTickets() + " tickets"); // <4>
        allTickets.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return allTickets;
    }

    private Component getOpenSolvedTicketStats() {
        Span openTickets = new Span(ticketService.findAllOpenTickets().size() + " open tickets\t" + ticketService.findAllTicketsByStatus("Solved").size() + " solved tickets "); // <4>
        openTickets.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return openTickets;
    }

    private Chart getWebsitesOpenTicketsChart() {
        Chart chart = new Chart(ChartType.PIE);
        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle(ticketService.findAllOpenTickets().size() + " open tickets");
        //conf.getLegend().setEnabled(false);

        DataSeries dataSeries = new DataSeries();
        websiteService.getAllWebsites().forEach(website ->
                dataSeries.add(new DataSeriesItem(website.getWebsite_name(), website.getOpenTicketsCount()))); // <5>
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }

    private Chart getWebsitesSolvedTicketsChart() {
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle(ticketService.findAllTicketsByStatus("Solved").size() + " solved tickets ");
        //conf.getLegend().setEnabled(false);

        DataSeries dataSeries = new DataSeries();
        websiteService.getAllWebsites().forEach(website ->
                dataSeries.add(new DataSeriesItem(website.getWebsite_name(), website.getSolvedTicketsCount()))); // <5>
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }

    private Chart getWebsitesAllTicketsChart() {
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("All tickets");
        //conf.getLegend().setEnabled(false);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setDepth(45);
        conf.setPlotOptions(plotOptions);

        // In 3D!
        Options3d options3d = new Options3d();
        options3d.setEnabled(true);
        options3d.setAlpha(60);

        conf.getChart().setOptions3d(options3d);

        DataSeries dataSeries = new DataSeries();
        websiteService.getAllWebsites().forEach(website ->
                dataSeries.add(new DataSeriesItem(website.getWebsite_name(), website.getTicketsCount()))); // <5>
        chart.getConfiguration().setSeries(dataSeries);

        return chart;
    }
}