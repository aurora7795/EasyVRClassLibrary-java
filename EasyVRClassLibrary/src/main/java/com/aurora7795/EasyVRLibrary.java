package com.aurora7795;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;

import static com.aurora7795.Protocol.*;

/**
 * Hello world!
 */
public class EasyVRLibrary {
    public static int EASYVR_RX_TIMEOUT = 500;
    public static int EASYVR_STORAGE_TIMEOUT = 500;
    public static int DEF_TIMEOUT = EASYVR_RX_TIMEOUT;
    public static int STORAGE_TIMEOUT = EASYVR_STORAGE_TIMEOUT;
    public int EASYVR_WAKE_TIMEOUT = 200;
    public int EASYVR_PLAY_TIMEOUT = 5000;
    public int EASYVR_TOKEN_TIMEOUT = 1500;
    protected byte Group;
    protected byte Id;
    private int Value;
    private Status _status = new Status();

    static ISerialPortWrapper _serialPort;

    public EasyVRLibrary(String portName, int baudRate) {
        if (_serialPort != null) return;
        // Create the serial port with basic settings
        _serialPort = new purejavacommWrapper(portName, baudRate);

        Value = -1;
        Group = -1;
        Id = -1;
        _status.V = 0;
    }

    private static void SendCommand(char command) {
        try {
            _serialPort.Write(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int ReceiveArgumentAsInt() throws IOException {
        int response;
        SendCommand((char) Protocol.ARG_ACK);
        response = ArgumentEncoding.ConvertArgumentCode(GetResponse(DEF_TIMEOUT));
        return response;
    }

    private static char ReceiveArgumentAsChar() throws IOException {
        char response = ' ';
        SendCommand((char) ARG_ACK);

        //TODO: need some way of handling timeouts

        response = GetResponse(DEF_TIMEOUT);
        return response;

    }

    private static void SendArgument(int argument) {
        try {
            _serialPort.Write(ArgumentEncoding.IntToArgumentChar(argument));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void SendCharacter(char argument) {
        try {
            _serialPort.Write(argument);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static char GetResponse(int timeout) throws IOException {

        _serialPort.SetTimeout(timeout);
        char temp;
        temp = _serialPort.Read();
        System.out.printf("read off buffer: %s%n", temp);
        return temp;
    }

    /**
     * Adds a new custom command to a group.
     *
     * @param group (0-16) is the target group, or one of the values in #Groups
     * @param index (0-31) is the index of the command within the selected group
     * @return true if the operation is successful
     */
    public Boolean AddCommand(int group, int index) {
        if (group < 0 || group > 16) throw new IllegalArgumentException(Integer.toString(group));
        if (index < 0 || index > 31) throw new IllegalArgumentException(Integer.toString(index));

        SendCommand(CMD_GROUP_SD);
        SendArgument(group);
        SendArgument(index);

        int rx = 0;
        try {
            rx = GetResponse(STORAGE_TIMEOUT);
        } catch (IOException e) {
            return false;
        }
        if (rx == STS_SUCCESS)
            return true;

        _status.V = 0;

        if (rx == STS_OUT_OF_MEM)
            _status.Memfull = true;

        return false;
    }

    /**
     * Sets the new communication speed. You need to modify the baudrate of the
     * underlying Stream object accordingly, after the function returns successfully.
     *
     * @param baudRate one of values in #Baudrate
     * @return true if the operation is successful
     */
    public Boolean ChangeBaudrate(Baudrate baudRate) {
        SendCommand(CMD_BAUDRATE);
        SendArgument(baudRate.getValue());

        try {
            return GetResponse(DEF_TIMEOUT) == STS_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Performs a memory check for consistency.
     * <p>
     * If a memory write or erase operation does not complete due to unexpecte conditions, like power losses, the
     * memory contents may be corrupted.
     * When the check fails #getError() returns #ERR_CUSTOM_INVALID.
     *
     * @return true if the operation is successful
     */
    public Boolean CheckMessages() {
        SendCommand(CMD_VERIFY_RP);
        SendArgument(-1);
        SendArgument(0);

        char rx = 0;
        try {
            rx = GetResponse(STORAGE_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        ReadStatus(rx);
        return (_status.V == 0);
    }

    /**
     * Detects an EasyVR module, waking it from sleep mode and checking it responds correctly.
     *
     * @return true if a compatible module has been found
     */
    public Boolean Detect() {
        int i;
        for (i = 0; i < 5; ++i) {
            SendCommand(CMD_BREAK);

            try {
                if (GetResponse(DEF_TIMEOUT) == STS_SUCCESS)
                    return true;
            } catch (IOException e) {

                return false;
            }
        }
        return false;
    }

    /**
     * Starts listening for a SonicNet token. Manually check for completion with #hasFinished().
     * <p>
     * The module is busy until token detection completes and it cannot accept other commands.You can interrupt
     * listening with #stop().
     *
     * @param bits      (4 or 8) specifies the length of received tokens
     * @param rejection rejection (0-2) specifies the noise rejection level, it can be one of the values in
     *                  #RejectionLevel
     * @param timeout   (1-28090) is the maximum time in milliseconds to keep listening for a valid token or(0)
     *                  to listen without time limits.
     */
    public void DetectToken(BitNumber bits, RejectionLevel rejection, int timeout) {
        SendCommand(CMD_RECV_SN);
        SendArgument(bits.getValue());
        SendArgument(rejection.getValue());

        if (timeout > 0)
            timeout = (timeout * 2 + 53) / 55; // approx / 27.46 - err < 0.15%
        SendArgument((timeout >> 5) & 0x1F);
        SendArgument(timeout & 0x1F);
    }

    /**
     * Retrieves the contents of a built-in or a custom grammar.
     * Command labels contained in the grammar can be obtained by calling #getNextWordLabel()
     *
     * @param grammar (0-31) is the target grammar, or one of the values in #Wordset
     * @return DumpGrammarResult is successful, containing:
     * Flags - a variable that holds some grammar flags when the function returns. See #GrammarFlag
     * Count - count is a variable that holds the number of words in the grammar when the function returns.
     * Null if failed
     */
    public DumpGrammarResult DumpGrammar(int grammar) {
        if (grammar < 0 || grammar > 31) throw new IllegalArgumentException(Integer.toString(grammar));

        DumpGrammarResult response = new DumpGrammarResult();

        SendCommand(CMD_DUMP_SI);
        SendArgument(grammar);

        try {
            if (GetResponse(DEF_TIMEOUT) != STS_GRAMMAR) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }

        char rx;
        try {
            rx = ReceiveArgumentAsChar();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        response.flags = (byte) (rx == -1 ? 32 : rx);

        try {
            rx = ReceiveArgumentAsChar();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        response.count = (byte) rx;
        return response;
    }

    public Boolean ResetAll() {
        SendCommand(CMD_RESETALL);
        SendCommand('R');

        try {
            return GetResponse(5000) == STS_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int GetId() {
        SendCommand(STS_ID);

        char response = 0;
        try {
            response = GetResponse(DEF_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != STS_ID)
            try {
                throw new Exception("Invalid response: " + response);
            } catch (Exception e) {
                e.printStackTrace();
            }

        try {
            response = ReceiveArgumentAsChar();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int decodedValue = ArgumentEncoding.ConvertArgumentCode(response);
        return decodedValue;
    }

    public DumpMessageResult DumpMessage(byte index) {

        DumpMessageResult response = new DumpMessageResult();

        SendCommand(CMD_DUMP_RP);
        SendArgument(-1);
        SendArgument(index);

        char sts = 0;
        try {
            sts = GetResponse(STORAGE_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sts != STS_MESSAGE) {
            ReadStatus(sts);
            return null;
        }

        // if communication should fail
        _status.V = 0;
        _status.Error = true;

        try {
            response.type = ReceiveArgumentAsInt();
        } catch (IOException e) {
          return null;
        }

        response.length = 0;
        if (response.type == 0)
            return response;

        int[] tempArray = new int[7];

        for (int i = 0; i < 6; ++i) {
            char rx;

            try{
                rx = ReceiveArgumentAsChar();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


            tempArray[i] |= rx & 0x0F;

            try{
                rx = ReceiveArgumentAsChar();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            tempArray[i] |= (rx << 4) & 0xF0;
        }

        _status.V = 0;
        return response;
    }

    public Boolean PlayPhoneTone(int tone, int duration) {
        SendCommand(CMD_PLAY_DTMF);
        SendArgument(-1);
        SendArgument(tone);
        SendArgument(duration);

        try {
            char response;
            response = GetResponse(5000);
            return response == STS_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void ReadStatus(char rx) {
        _status.V = 0;
        Value = 0;

        switch (rx) {
            case STS_SUCCESS:
                return;

            case STS_SIMILAR:

                try {
                    _status.Builtin = true;
                    rx = ReceiveArgumentAsChar();
                    Value = rx;
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            case STS_RESULT:
                _status.Command = true;

                try {
                    rx = ReceiveArgumentAsChar();
                    Value = rx;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;

            case STS_TOKEN:
                _status.Token = true;

                try {
                    rx = ReceiveArgumentAsChar();
                    Value = rx << 5;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    rx = ReceiveArgumentAsChar();
                    Value |= rx;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;

            case STS_AWAKEN:
                _status.Awakened = true;
                return;

            case STS_TIMEOUT:
                _status.Timeout = true;
                return;

            case STS_INVALID:
                _status.Invalid = true;
                return;

            case STS_ERROR:
                _status.Error = true;

                try {
                    rx = ReceiveArgumentAsChar();
                    Value = rx << 4;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    rx = ReceiveArgumentAsChar();
                    Value |= rx;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;
        }

        // unexpected condition (communication error)
        _status.V = 0;
        _status.Error = true;
        Value = 0;
    }

    public class DumpGrammarResult {
        byte flags;
        int count;
    }

    private class Status {
        public Boolean Awakened;
        public Boolean Builtin;
        public Boolean Command;
        public Boolean Conflict;
        public Boolean Error;
        public Boolean Invalid;
        public Boolean Memfull;
        public Boolean Timeout;
        public Boolean Token;
        public byte V;
    }
}
