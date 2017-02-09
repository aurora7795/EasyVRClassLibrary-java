package com.aurora7795;

import gnu.io.NRSerialPort;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static gnu.io.NRSerialPort.getAvailableSerialPorts;

/**
 * Created by aurora7795 on 09/02/2017.
 */
public class OSXSerialPortWrapper implements ISerialPortWrapper {

    DataInputStream ins;
    DataOutputStream outs;
    NRSerialPort serial;

    public OSXSerialPortWrapper(String port, int baudRate){

        for (String s : getAvailableSerialPorts()) {
            System.out.println("Available port: " + s);
        }

        serial = new NRSerialPort(port, baudRate);

        serial.connect();

        ins = new DataInputStream(serial.getInputStream());
        outs = new DataOutputStream(serial.getOutputStream());
    }

    /**
     * Reads a byte off the input buffer
     * @return The byte output from the serial port
     * @throws IOException
     */
    public char Read() throws IOException {
        byte b = (byte) ins.read();
        char c = ((char) b);
        return c;
    }

    /**
     * Writes a string to the serial port
     * @param request the string to send to the serial port
     * @throws IOException
     */
    public void Write(String request) throws IOException {

        char[] tempArray = request.toCharArray();

        for (char tempChar : tempArray) {
            outs.write(tempChar);
        }
    }

    /**
     * Disconnects the serial port
     */
    public void Disconnect(){
        serial.disconnect();
    }
}
