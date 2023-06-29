//package com.example.application.views;
//
//import com.example.application.data.entity.TUser;
//import com.example.application.views.CreateUserView;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.data.provider.ListDataProvider;
//import org.hibernate.validator.internal.util.stereotypes.Lazy;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//
//
//import static com.helger.commons.mock.CommonsAssert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//class CreateUserViewTest {
//    @Autowired
//    private CreateUserView createUserView;
//
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