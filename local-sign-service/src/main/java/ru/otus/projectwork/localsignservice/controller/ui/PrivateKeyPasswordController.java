package ru.otus.projectwork.localsignservice.controller.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.otus.projectwork.localsignservice.domain.PasswordKeyType;
import ru.otus.projectwork.localsignservice.service.KeyStoreService;
import ru.otus.projectwork.localsignservice.utils.fx.FXMLFactory;
import ru.otus.projectwork.localsignservice.utils.fx.FXMLFileBinding;


import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

@FXMLFileBinding("/fxml/PrivateKeyPasswordContainer.fxml")
public class PrivateKeyPasswordController extends BorderPane implements Initializable {

    @FXML
    private CheckBox remember;

    @FXML
    private TextField privateKeyPassword;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final Map<PasswordKeyType, String> map = new HashMap<>();

    private Optional<Map<PasswordKeyType, String>> mapOptional;

    private KeyStoreService keyStoreService;

    private void setKeyStoreService(KeyStoreService keyStoreService) {
        this.keyStoreService = keyStoreService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveButton.setOnAction(this::saveOnAction);
        cancelButton.setOnAction(this::cancelOnAction);
        remember.setOnAction(this::rememberOnAction);
        privateKeyPassword.textProperty().addListener((observable, oldValue, newValue) -> privateKeyPassword.setEffect(null));
    }

    private Effect bloomEffect() {
        DropShadow bloom = new DropShadow();
        bloom.setColor(Color.RED);
        bloom.setOffsetX(0f);
        bloom.setOffsetY(0f);
        bloom.setHeight(10);
        bloom.setWidth(10);
        return bloom;
    }

    public Optional<Map<PasswordKeyType, String>> getMap() {
        createScene();
        return mapOptional;
    }

    public boolean isRemember() {
        return remember.isSelected();
    }


    private void createScene() {
        Scene sc = new Scene(this);
        Stage stage = new Stage();
        stage.resizableProperty().set(false);
        stage.setTitle("Авторизация контейнера");
        stage.setScene(sc);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
    }

    private void saveOnAction(ActionEvent event) {
        if (privateKeyPassword.getText().isEmpty()) {
            privateKeyPassword.setEffect(bloomEffect());
            return;
        }
        if (!keyStoreService.checkPrivKeyPass(privateKeyPassword.getText())) {
            new Alert(Alert.AlertType.WARNING, "Пароль к контейнеру указан неверно").showAndWait();
            return;
        }
        map.put(PasswordKeyType.PRIVATE_PASS_KEY, privateKeyPassword.getText());
        mapOptional = Optional.of(map);
        close();
    }

    private void cancelOnAction(ActionEvent event) {
        mapOptional = Optional.empty();
        close();
    }

    private void rememberOnAction(ActionEvent event) {
        if (remember.isSelected()) {
            confirmation();
        }
    }

    private void confirmation() {
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Данная операция не безопасна!", ButtonType.OK).
                showAndWait();
        remember.setSelected(buttonType.isPresent());
    }


    private void close() {
        ((Stage) this.getScene().getWindow()).close();
    }

    public static ControlBuilder builder() {
        return new ControlBuilder();
    }

    public static class ControlBuilder {
        private ControlBuilder() {
        }

        public PrivateKeyPasswordController create(KeyStoreService service) {
            PrivateKeyPasswordController m = FXMLFactory.createController(PrivateKeyPasswordController.class);
            m.setKeyStoreService(service);
            return m;
        }
    }
}
