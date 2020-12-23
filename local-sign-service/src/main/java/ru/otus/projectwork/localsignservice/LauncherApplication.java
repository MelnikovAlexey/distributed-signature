package ru.otus.projectwork.localsignservice;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.projectwork.localsignservice.events.StageReadyEvent;

public class LauncherApplication extends Application {
    private ConfigurableApplicationContext applicationContext;
    private Stage primaryStage;

    @Override
    public void init() {
        final SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(LocalSignServiceApplication.class);
        springApplicationBuilder.headless(false);
        applicationContext = springApplicationBuilder.run();
    }

    @Override
    public void stop() {
        applicationContext.stop();
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        applicationContext.publishEvent(new StageReadyEvent(primaryStage, applicationContext::getBean));
    }


}
