package com.example.application.views;

import com.example.application.data.entity.Website;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WebsiteViewTest {

    static {
        // Prevent Vaadin Development mode to launch browser window
        System.setProperty("vaadin.launch-browser", "false");
    }

    @Autowired
    private WebsiteView websiteView;

    @Test
    public void formShownWhenContactSelected() {
        Grid<Website> grid = websiteView.grid;
        Website firstTicket = getFirstItem(grid);

        WebsiteForm form = websiteView.form;

        assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstTicket);
        assertTrue(form.isVisible());
        assertEquals(firstTicket.getWebsite_name(), form.website_name.getValue());
    }

    private Website getFirstItem(Grid<Website> grid) {
        return( (ListDataProvider<Website>) grid.getDataProvider()).getItems().iterator().next();
    }
}