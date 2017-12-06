package com.android.lyric;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;



public class IOutil {

    public static void close(InputStream io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void close(Reader io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(OutputStream io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
