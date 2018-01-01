package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public Server() throws IOException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;

        try {
            System.out.println("Server started!");
            while (running) {
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                String received
                        = new String(packet.getData(), 0, packet.getLength());

                if (received.equals("end")) {
                    running = false;
                    continue;
                }

                buf = handleMessage(received).getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);



                socket.send(packet);
            }
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String handleMessage(String message) {
        String[] array = message.split(" ");
        String result = count(Double.parseDouble(array[0]), Double.parseDouble(array[1])).toString();
        saveToFile(array[0], array[1], result);
        return result;
    }

    public Double count(Double x, Double y) {
        return (Math.sqrt(x) / (2 * y + 10)) * ((2 * x * Math.pow((1 + Math.pow(x, 2)), 2)) / (x + Math.cbrt(Math.abs(1 + Math.pow(x, 5)))));
    }

    public void saveToFile(String x, String y, String result) {
        File file = new File("db.txt");

        String toFile = "x: " + x + " y: " + y + " result: " + result + "\n";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(toFile);

            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
