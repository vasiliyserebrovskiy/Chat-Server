package ait.chat.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (16.06.2025)
 */
public class ChatServerReceiver implements Runnable{
    private final Socket socket;
    private final BlockingQueue<String> messageBox;

    public ChatServerReceiver(Socket socket, BlockingQueue<String> messageBox) {
        this.socket = socket;
        this.messageBox = messageBox;
    }

    @Override
    public void run() {
        //Runnable logic for receiver thread
        try (Socket socket = this.socket){
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String message = socketReader.readLine();
                if (message == null) {
                    System.out.println("Connection: " + socket.getInetAddress() + " : " + socket.getPort() + ", closed");
                    break;
                }
                System.out.println("Server receive: " + message);
                //Add message to the messageBox
                messageBox.add(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
