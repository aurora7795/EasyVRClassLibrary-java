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
import java.util.Set;

public class Controller {
    public TextArea responseTB;
    public Button sendBtn;
    public TextArea requestTB;
    public ChoiceBox<String> portListBox;
    public Button ConnectBtn;
    public Button readBtn;

    NRSerialPortWrapper serialPort;

    private BooleanProperty shouldBeDisabled;

    @FXML
    public void initialize() {
        shouldBeDisabled = new SimpleBooleanProperty(true);

        Set<String> availableSerialPorts = NRSerialPort.getAvailableSerialPorts();
        String[] tempList = new String[availableSerialPorts.size()];
        availableSerialPorts.toArray(tempList);
        ObservableList<String> portList = FXCollections.observableArrayList(tempList);
        portListBox.setItems(portList);

        sendBtn.disableProperty().bind(shouldBeDisabled);
    }

    public void submit(ActionEvent actionEvent) {

        try {
            serialPort.Write(requestTB.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            responseTB.appendText(String.format("%s%s", serialPort.Read(), System.getProperty("line.separator")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void connectBtnClick(ActionEvent actionEvent) {

        String port = portListBox.getValue();

        serialPort = new NRSerialPortWrapper(port, 9600);
        responseTB.appendText(String.format("Connected to %s%s", port, System.getProperty("line.separator")));
        shouldBeDisabled.setValue(false);
    }

    public void readClick(ActionEvent actionEvent) {
        try {
            responseTB.appendText(String.format("%s%s", serialPort.Read(), System.getProperty("line.separator")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
