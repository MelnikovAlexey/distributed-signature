package ru.otus.projectwork.localsignservice.utils.fx;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FXMLFileBinding {
    String value();
}
