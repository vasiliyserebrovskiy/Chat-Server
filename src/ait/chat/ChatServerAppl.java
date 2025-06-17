package ait.chat;



import ait.chat.task.ChatServerReceiver;
import ait.chat.task.ChatServerSender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (16.06.2025)
 */
public class ChatServerAppl {
    public static void main(String[] args) throws InterruptedException {

        int port = 9000;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        BlockingQueue<String> messageBox = new ArrayBlockingQueue<>(10);

        // Daemon for sender
        ChatServerSender sender = new ChatServerSender(messageBox);
        Thread senderThread = new Thread(sender);
        senderThread.setDaemon(true);
        senderThread.start();


        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //The server must be constantly running and waiting for someone to connect to it.
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while(true) {
                System.out.println("Server waiting...");
                Socket socket = serverSocket.accept(); // The main function of the server: when a client connects, the server must be in the accept state.
                System.out.println("Connection established");
                System.out.println("Client host:" + socket.getInetAddress() + " : " + socket.getPort());
                sender.addClient(socket); // addong connected client to the PrintWriter for sending messages.
                ChatServerReceiver receiver = new ChatServerReceiver(socket, messageBox); // create receiver
                executorService.execute(receiver);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
            System.out.println("Server finished");
        }


    }
}
