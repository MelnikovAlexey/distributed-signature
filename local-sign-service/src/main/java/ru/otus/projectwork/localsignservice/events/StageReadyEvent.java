package ru.otus.projectwork.localsignservice.events;

import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.context.ApplicationEvent;

public class StageReadyEvent extends ApplicationEvent {
    private final Callback<Class<?>, Object> controllerFactory;

    public StageReadyEvent(Stage primaryStage, Callback<Class<?>, Object> controllerFactory) {
        super(primaryStage);
        this.controllerFactory = controllerFactory;
    }

    public Stage getStage() {
        return (Stage) getSource();
    }

    public Callback<Class<?>, Object> getControllerFactory() {
        return controllerFactory;
    }
}
