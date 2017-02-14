package com.aurora7795;

import com.aurora7795.EasyVRLibrary.DumpGrammarResult;
import com.aurora7795.Protocol.*;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Martin Bradford Gago on 10/02/2017.
 */
public class EasyVRLibraryTest extends TestCase {

    private String comPort = "/dev/tty.usbserial-fd1";
    // private String comPort = "COM3";
    private int baudRate = 9600;

    @Test
    public void AddCommandTest_GroupOutOfRange_ThrowsException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            Boolean response = tempVr.AddCommand(17, 12);
        });
    }

    @Test
    public void AddCommandTest_IndexOutOfRange_ThrowsException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            Boolean response = tempVr.AddCommand(17, 45);
        });
    }

    @Test
    public void AddCommandTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();

        //Act
        Boolean response = tempVr.AddCommand(0, 0);
        //Assert
        assertTrue(response);
    }

    @Test
    public void ChangeBaudrateTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.ChangeBaudrate(Baudrate.B9600);
        //Assert
        assertTrue(response);
    }

    @Test
    public void EraseCommandTest_GroupOutOfRange_ThrowsException() {

        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            tempVr.EraseCommand(17, 12);
        });

    }

    @Test
    public void EraseCommandTest_IndexOutOfRange_ThrowsException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            tempVr.EraseCommand(2, 45);
        });
    }

    @Test
    public void EraseCommandTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();

        tempVr.AddCommand(1, 0);

        //Act
        Boolean response = tempVr.EraseCommand(1, 0);
        //Assert
        assertTrue(response);
    }

    @Test
    public void GetCommandCount_GroupOutOfRange_ThrowsException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            tempVr.GetCommandCount(17);
        });
    }

    @Test
    public void GetCommandCount_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        int response = tempVr.GetCommandCount(3);
        //Assert
        assertTrue(response >= 0);
    }

    @Test
    public void GetGrammarsCount_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        int response = tempVr.GetGrammarsCount();
        //Assert
        assertTrue(response >= 0);
    }

    @Test
    public void PlayPhoneTone_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.PlayPhoneTone(1, 9);
        //Assert
        assertTrue(response);
    }

    @Test
    public void PlaySoundTest_InvalidVolume_ThrowException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            tempVr.PlaySound(1, 345);
        });
    }

    @Test
    public void PlaySoundTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.PlaySound(1, 15);
        //Assert
        assertTrue(response);
    }

    @Test
    public void RealtimeLipsyncTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.RealtimeLipsync(2, 100);
        //Assert
        assertTrue(response);
    }

    @Test
    public void RemoveCommandTest_GroupOutOfRange_ThrowsException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            tempVr.RemoveCommand(17, 12);
        });
    }

    @Test
    public void RemoveCommandTest_IndexOutOfRange_ThrowsException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            tempVr.RemoveCommand(2, 45);
        });
    }

    @Test
    public void RemoveCommandTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();

        tempVr.AddCommand(1, 0);
        //Act
        Boolean response = tempVr.RemoveCommand(1, 0);
        //Assert
        assertTrue(response);
    }

    @Test
    public void ResetAllTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.ResetAll();
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetCommandLatencyTest() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetCommandLatency(CommandLatency.MODE_NORMAL);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetDelayTest_OutsideBounds_ThrowsException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            tempVr.SetDelay(2000);
        });
    }

    @Test
    public void SetDelayTest_Rounding10_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetDelay(23);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetDelayTest_Rounding100_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetDelay(93);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetDelayTest_Rounding1000_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetDelay(223);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetDelayTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetDelay(20);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetKnobTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetKnob(Protocol.Knob.LOOSE);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetLanguageTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetLanguage(Protocol.Language.ENGLISH);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetLevelTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetLevel(Protocol.Level.HARD);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetMicDistanceTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetMicDistance(Protocol.Distance.FAR_MIC);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetTimeoutTest_InvalidTimeout_ThrowsException() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        assertThrows(IllegalArgumentException.class, () -> {
            Boolean response = tempVr.SetTimeout(60);
        });

    }

    @Test
    public void SetTimeoutTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetTimeout(1);
        //Assert
        assertTrue(response);
    }

    @Test
    public void SetTrailingSilenceTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.SetTrailingSilence(Protocol.TrailingSilence.TRAILING_300MS);
        //Assert
        assertTrue(response);
    }

    @Test
    public void Stop_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.Stop();
        //Assert
        assertTrue(response);
    }

    @Test
    public void GetIdTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        ModuleId response = tempVr.GetId();
        //Assert
        assertTrue(response == ModuleId.EASYVR3_4);
    }

    @Test
    public void CheckMessagesTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.CheckMessages();
        //Assert
        assertTrue(response);
    }

    @Test
    public void DetectTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        Boolean response = tempVr.Detect();
        //Assert
        assertTrue(response);
    }

    @Test
    public void DumpGrammarTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        //Act
        DumpGrammarResult response = tempVr.DumpGrammar(0);
        //Assert
        assertTrue(response != null);
        assertTrue(response.flags > 0);
        assertTrue(response.count > 0);
    }

    @Test
    public void DumpMessageTest_NoMessageAvailable_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();
        //Act
        DumpMessageResult response = tempVr.DumpMessage(0);
        //Assert
        assertTrue(response != null);
        assertTrue(response.type == 0);
        assertTrue(response.length == 0);
    }

    @Test
    public void PlayMessageAsyncTest_Success() {
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);

        tempVr.PlayMessageAsync(1, Protocol.MessageSpeed.SPEED_NORMAL, Protocol.MessageAttenuation.ATTEN_NONE);

    }

    @Test
    public void PlaySoundAsyncTest_Success() {
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.PlaySoundAsync(1, 15);
    }

    @Test
    public void RecordMessageAsyncTest() {
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);

        tempVr.RecordMessageAsync(1, MessageType.MSG_EMPTY, 5);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tempVr.PlayMessageAsync(1, MessageSpeed.SPEED_NORMAL, MessageAttenuation.ATTEN_NONE);
    }

    @Test
    public void DumpCommandTest() {
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);


        DumpCommandResult response = tempVr.DumpCommand(1, 0);

        assertTrue(response != null);
        assertTrue(response.name == "TESTING123");
        assertTrue(response.training == 2);
    }

    @Test
    public void SetCommandLabelTest_Success() {
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();
        Boolean response = tempVr.AddCommand(0, 0);

        assertTrue(response);

        response = tempVr.SetCommandLabel(0, 0, "testCom1");
        assertTrue(response);

        DumpCommandResult CommandResponse = tempVr.DumpCommand(0, 0);

        assertTrue(CommandResponse != null);
        assertTrue(CommandResponse.name == "TESTCOM1");
        assertTrue(CommandResponse.training == 0);
    }

    @Test
    public void DumpSoundTableTest_Success() {
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();

        DumpSoundTableResult STresponse = tempVr.DumpSoundTable();

        assertTrue(STresponse != null);
        assertTrue(STresponse.name == "SND_BEEP");
        assertTrue(STresponse.count == 1);
    }

    @Test
    public void FixMessagesTest_Success() {
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);
        tempVr.ResetAll();

        Boolean response = tempVr.FixMessages(true);

        assertTrue(response);
    }

    @Test
    public void GetNextWordLabelTest_Success() {
        //Arrange
        EasyVRLibrary tempVr = new EasyVRLibrary(comPort, baudRate);

        byte flags;
        int count;
        DumpGrammarResult response = tempVr.DumpGrammar(0);
        assertTrue(response != null);

        //Act
        String name = tempVr.GetNextWordLabel();

        //Assert
        assertTrue(response != null);
        assertTrue(name == "ROBOT");

    }

}