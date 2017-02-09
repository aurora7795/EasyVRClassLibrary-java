package com.aurora7795;

import java.io.IOException;

/**
 * Created by aurora7795 on 09/02/2017.
 */
public interface ISerialPortWrapper {

    char Read() throws IOException;
    void Write(String request) throws IOException ;
}
