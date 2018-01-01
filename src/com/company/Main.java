package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            new Server().run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
