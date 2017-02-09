package com.aurora7795;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class Controller {
    public TextArea responseTB;
    public Button sendBtn;
    public TextArea requestTB;

    OSXSerialPortWrapper serialPort;

    public Controller() {
        serialPort = new OSXSerialPortWrapper("/dev/tty.usbserial-fd1", 9600);
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

}
