package com.aurora7795;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import purejavacomm.*;

/**
 * Created by aurora7795 on 13/02/2017.
 */
public class purejavacommWrapper implements ISerialPortWrapper{

   private SerialPort _serialPort;
   private DataInputStream ins;
   private DataOutputStream outs;


    public purejavacommWrapper(String port, int baudRate) {

        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
            _serialPort = (SerialPort) portId.open("EasyVR",2000);
            _serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            _serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);


            ins = new DataInputStream(_serialPort.getInputStream());
            outs = new DataOutputStream(_serialPort.getOutputStream());

        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a byte off the internal buffer
     *
     * @return The byte output from the serial port
     * @throws IOException
     */
    public char Read() throws IOException {

        byte b = (byte) ins.read();
        char c = ((char) b);
        return c;
    }

    /**
     * Writes a character to the serial port
     *
     * @param request the string to send to the serial port
     * @throws IOException
     */
    public void Write(char request) throws IOException {

        System.out.printf("writing: %s%n", request);
        outs.write(request);

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
        _serialPort.close();
    }

    @Override
    public void finalize() {
        System.out.println("finalize called");
        _serialPort.close();
    }

    public void SetTimeout(int timeout){

        if(timeout > 0) {

            try {
                _serialPort.enableReceiveTimeout(timeout);
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace();
            }
            System.out.printf("Timeout set to: %d ms %n", _serialPort.getReceiveTimeout());
        }
        else{
            _serialPort.disableReceiveTimeout();
            System.out.println("Timeout disabled.");
        }

    }

    public static List<String> getAvailableSerialPorts(){
    List<String> list = new ArrayList<String>();

    Enumeration portList = CommPortIdentifier.getPortIdentifiers();
    while (portList.hasMoreElements()) {
        CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            list.add(portId.getName());
        }
    }
    return list;
    }
}
