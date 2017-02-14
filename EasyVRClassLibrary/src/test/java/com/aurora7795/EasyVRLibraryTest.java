package com.aurora7795;

import com.aurora7795.Protocol.Baudrate;
import com.sun.org.apache.xpath.internal.operations.Bool;
import junit.framework.TestCase;
import com.aurora7795.EasyVRLibrary.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Created by Martin Bradford Gago on 10/02/2017.
 */
public class EasyVRLibraryTest extends TestCase {

    @Test
    public void testDumpMessage() throws Exception {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();
        //Act
        DumpMessageResult response = tempVr.DumpMessage((byte) 0);
        //Assert
        assertNotNull(response);
        assertTrue(response.length == 0);
        assertTrue(response.type == 0);

    }

    public void testDumpGrammar() throws Exception {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        byte flags;
        int count;
        DumpGrammarResult response = tempVr.DumpGrammar(0);
        //Assert
        assertNotNull(response);
        assertTrue(response.flags > 0);
        assertTrue(response.count > 0);

    }

     private String comPort = "/dev/tty.usbserial-fd1";
   // private String comPort = "COM3";
    private int baudRate = 9600;

    public void testChangeBaudrate() throws Exception {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.ChangeBaudrate(Baudrate.B9600);
        //Assert
        assertTrue(response);
    }


    public void testPlayPhoneTone() throws Exception {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.PlayPhoneTone(1, 9);
        //Assert
        assertTrue(response);
    }

    public void testAddCommand() throws Exception {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();

        //Act
        Boolean response = tempVr.AddCommand(0, 0);
        //Assert
        assertTrue(response);


    }

}