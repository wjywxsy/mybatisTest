package com.wjyxsy.io;

import java.io.InputStream;

public class Resources {

    public static InputStream getResourceAsStream(String path) {
        InputStream stream = Resources.class.getClassLoader().getResourceAsStream(path);
        return stream;
    }
}
