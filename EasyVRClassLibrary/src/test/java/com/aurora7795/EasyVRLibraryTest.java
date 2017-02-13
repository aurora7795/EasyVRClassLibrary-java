package com.aurora7795;

import com.aurora7795.Protocol.Baudrate;
import com.sun.org.apache.xpath.internal.operations.Bool;
import junit.framework.TestCase;
import com.aurora7795.EasyVRLibrary.*;

/**
 * Created by Martin Bradford Gago on 10/02/2017.
 */
public class EasyVRLibraryTest extends TestCase {

    private String comPort = "/dev/tty.usbserial-fd1";
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