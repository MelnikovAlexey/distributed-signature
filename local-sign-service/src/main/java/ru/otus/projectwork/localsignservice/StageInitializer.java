package ru.otus.projectwork.localsignservice;

import javafx.application.Platform;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.otus.projectwork.localsignservice.events.StageReadyEvent;

@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        Platform.setImplicitExit(false);
    }

}
