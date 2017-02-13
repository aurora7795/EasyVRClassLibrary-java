package com.aurora7795;

import gnu.io.NRSerialPort;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static gnu.io.NRSerialPort.getAvailableSerialPorts;

/**
 * Created by aurora7795 on 09/02/2017.
 */
public class NRSerialPortWrapper implements ISerialPortWrapper {

    DataInputStream ins;
    DataOutputStream outs;
    NRSerialPort serial;

    private char internalResponseBuffer;

    public NRSerialPortWrapper(String port, int baudRate) {

        for (String s : getAvailableSerialPorts()) {
            System.out.println("Available port: " + s);
        }

        serial = new NRSerialPort(port, baudRate);

        serial.connect();

        ins = new DataInputStream(serial.getInputStream());
        outs = new DataOutputStream(serial.getOutputStream());
    }

    /**
     * Reads a byte off the internal buffer
     *
     * @return The byte output from the serial port
     * @throws IOException
     */
    public char Read() throws IOException {

        //small sleep to allow before calling read - was having problems without it...
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte b = (byte) ins.read();
        char c = ((char) b);
        System.out.printf("Character off buffer: %s%n", c);
        internalResponseBuffer = c;
        return internalResponseBuffer;
    }

    /**
     * Writes a character to the serial port
     *
     * @param request the string to send to the serial port
     * @throws IOException
     */
    public void Write(char request) throws IOException {

        // to keep responses in sync - on writing, we also send a read and push the response to an internal
        // buffer

        System.out.printf("writing: %s%n", request);
        outs.write(request);

        byte b = (byte) ins.read();
        char c = ((char) b);
        System.out.printf("Character off buffer: %s%n", c);
        internalResponseBuffer = c;

    }

    public void Write(String request) throws IOException {

        char[] charArray = request.toCharArray();

        for (char tempChar : charArray) {
            outs.write(tempChar);
        }
    }

    /**
     * Disconnects the serial port
     */
    public void Disconnect() {
        serial.disconnect();
    }

    @Override
    public void finalize() {
        System.out.println("finalize called");
        serial.disconnect();
    }
}
