package ru.otus.projectwork.localsignservice.utils.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.lang.reflect.Constructor;
import java.net.URL;

public class FXMLFactory {
    private FXMLFactory(){
    }
    public static <T extends Node> T createController(Class<T> tcClass) {
        try {
            Constructor<T> con = tcClass.getConstructor();
            T t = (T) con.newInstance();
            FXMLFileBinding bi = (FXMLFileBinding)tcClass.getAnnotation(FXMLFileBinding.class);
            if (bi != null) {
                URL resource = tcClass.getResource(bi.value());
                if (resource == null) {
                    throw new RuntimeException("Ресурс не найден " + bi.value());
                }

                FXMLLoader loader = new FXMLLoader(resource);
                loader.setRoot(t);
                loader.setController(t);
                loader.load();
            }

            return t;
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }
}
