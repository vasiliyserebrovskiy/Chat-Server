package ait.socket.server.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (16.06.2025)
 */
public class ClientHandler implements Runnable{

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }



    @Override
    public void run() {
        try (Socket socket = this.socket) {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true); //авто autoFlash by setting true
            while (true) {
                String message = socketReader.readLine();
                if (message == null) {
                    System.out.println("Connection: " + socket.getInetAddress() + " : " + socket.getPort() + ", closed");
                    break;
                }
                System.out.println("Server received: " + message);
                socketWriter.println(message + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
//                    socketWriter.flush(); // тут тоже можно мы указали в 29 строке
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
