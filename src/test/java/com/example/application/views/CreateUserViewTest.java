//package com.example.application.views;
//
//import com.example.application.data.entity.TUser;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.data.provider.ListDataProvider;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class CreateUserViewTest {
//    @Autowired
//    private CreateUserView createUserView;
//    @Test
//    public void formShownWhenUserSelected() {
//        Grid<TUser> grid = createUserView.grid;
//        TUser firstUser = getFirstItem(grid);
//
//        CreateUserForm form = createUserView.form;
//
//        assertFalse(form.isVisible());
//        grid.asSingleSelect().setValue(firstUser);
//        assertTrue(form.isVisible());
//        assertEquals(firstUser.getUsername(), form.username.getValue());
//    }
//
//    private TUser getFirstItem(Grid<TUser> grid) {
//        return((ListDataProvider<TUser>) grid.getDataProvider()).getItems().iterator().next();
//    }
//
//}