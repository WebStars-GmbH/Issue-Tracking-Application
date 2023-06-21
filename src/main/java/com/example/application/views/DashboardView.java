package com.example.application.views;

import com.example.application.data.entity.Ticket;
import com.example.application.data.entity.Website;
import com.example.application.data.service.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.List;


@PermitAll
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Webst@rs Ticketing Application")
public class DashboardView extends VerticalLayout {

    private final CrmService service;
    private final TicketService ticketService;
    private final TUserService tUserService;
    private final WebsiteService websiteService;
    private final TeamService teamService;

    Grid<Website> grid = new Grid<>(Website.class);


    public DashboardView(CrmService service, TicketService ticketService, TUserService tUserService, WebsiteService websiteService, TeamService teamService) {
        this.service = service;
        this.ticketService = ticketService;
        this.tUserService = tUserService;
        this.teamService = teamService;
        this.websiteService = websiteService;



        addClassName("dashboard-view");
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();
        add(getTicketStats(), getOpenSolvedTicketStats(), getCancelledTicketStats(), getWebsiteAndTicketStats());
        grid.setItems(websiteService.getAllWebsites());

        //TODO: chart components need a Vaadin Pro server license
        //add(getWebsitesAllTicketsChart(), getWebsitesOpenTicketsChart(), getWebsitesSolvedTicketsChart());
    }

    private Component getTicketStats() {
        Span allTickets = new Span(ticketService.countTickets() + " tickets");
        allTickets.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return allTickets;
    }

    private Component getOpenSolvedTicketStats() {
        List<Ticket> openTickets = ticketService.findAllOpenTickets();
        List<Ticket> solvedTickets = ticketService.findAllTicketsByStatus("Solved");
        if (solvedTickets.size() == 0) return null;
        Long averageSolveTime = Long.valueOf(0);
        for (Ticket t: solvedTickets){
            averageSolveTime += t.getTimeBetweenRegisteredAndSolved();
        }
        TimeFormatUtility tu = new TimeFormatUtility();
        Span openTicketsStats = new Span(openTickets.size() + " open tickets. " + solvedTickets.size() + " solved tickets. Average solve time: " + tu.millisecondsToTimeFormat(averageSolveTime/solvedTickets.size()));
        openTicketsStats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return openTicketsStats;
    }

    private Component getCancelledTicketStats() {
        List<Ticket> cancelledTickets = ticketService.findAllTicketsByStatus("Cancelled");
        if (cancelledTickets.size() == 0) return null;
        Long averageCancelTime = Long.valueOf(0);
        for (Ticket t: cancelledTickets){
            averageCancelTime += t.getTimeBetweenRegisteredAndCancelled();
        }
        TimeFormatUtility tu = new TimeFormatUtility();

        Span cancelledTicketsStats = new Span(cancelledTickets.size() + " cancelled tickets. Average cancel time: " + tu.millisecondsToTimeFormat(averageCancelTime/cancelledTickets.size()));
        cancelledTicketsStats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return cancelledTicketsStats;
    }

    private HorizontalLayout getWebsiteAndTicketStats() {
        HorizontalLayout content = new HorizontalLayout(grid);
        grid.addClassNames("website-grid");
        grid.setSizeFull();
        grid.setColumns("website_name");
        grid.addColumn(website -> website.getTicketsCount()).setHeader("Tickets").setSortable(true);
        grid.addColumn(website -> website.getOpenTicketsCount()).setHeader("Open Tickets").setSortable(true);
        grid.addColumn(website -> website.getSolvedTicketsCount()).setHeader("Solved Tickets").setSortable(true);
        grid.addColumn(website -> website.getAverageSolveTimeString()).setHeader("Average Solve Time").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }


    //TODO Some beautiful charts...
    /*
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
*/

}