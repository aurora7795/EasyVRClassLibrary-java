package com.aurora7795;

import gnu.io.NRSerialPort;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Controller {
    public ChoiceBox<String> portListBox;
    public Button ConnectBtn;
    public TextArea responseTB;
    public Button getIdBtn;
    public Button sendPhoneToneBtn;
    public Button startRecognitionBtn;

    purejavacommWrapper serialPort;

    private BooleanProperty shouldBeDisabled;

    @FXML
    public void initialize() {
        shouldBeDisabled = new SimpleBooleanProperty(true);

        List<String> availableSerialPorts = purejavacommWrapper.getAvailableSerialPorts();
        ObservableList<String> portList = FXCollections.observableArrayList(availableSerialPorts);
        portListBox.setItems(portList);

        getIdBtn.disableProperty().bind(shouldBeDisabled);
        sendPhoneToneBtn.disableProperty().bind(shouldBeDisabled);
        startRecognitionBtn.disableProperty().bind(shouldBeDisabled);
    }



    public void connectBtnClick(ActionEvent actionEvent) {

        String port = portListBox.getValue();

        serialPort = new purejavacommWrapper(port, 9600);
        responseTB.appendText(String.format("Connected to %s%s", port, System.getProperty("line.separator")));
        shouldBeDisabled.setValue(false);
    }


}
