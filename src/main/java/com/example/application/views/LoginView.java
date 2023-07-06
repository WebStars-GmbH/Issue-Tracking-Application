package com.example.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("login")
@PageTitle("Log in | Webst@rs Ticketing Application")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private final LoginForm login = new LoginForm();

	public LoginView(){
		setDarkTheme();
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		login.setAction("login");

		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setAdditionalInformation(
				"Contact admin@webstars.com if you're experiencing issues logging into your account");

		login.setI18n(i18n);
		login.addForgotPasswordListener(click -> Notification.show("Please contact admin@webstars.com"));

		add(new H1("Webst@rs Ticketing Application: Log in"), login);
	}
	private void setDarkTheme(){
		ThemeList themeList = UI.getCurrent().getElement().getThemeList();
		themeList.add(Lumo.DARK);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if(beforeEnterEvent.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error")) {
			login.setError(true);
		}
		VaadinSession vaadinSession = VaadinSession.getCurrent() ;
		vaadinSession.setAttribute("currentuser","user");
	}
}