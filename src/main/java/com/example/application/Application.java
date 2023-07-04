package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 *
 */
@SpringBootApplication
@Theme(value = "webstars")
@PWA(
        name = "Vaadin CRM",
        shortName = "CRM",
        //iconPath = "/icons/logo.png",
        offlinePath="src/main/resources/META-INF/resources/offline.html",
        offlineResources = { "gelb.png" }
)
@ComponentScan(basePackages = {"com.example.application.views", "com.example.application.data", "com.example.application.security"})
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }


}
