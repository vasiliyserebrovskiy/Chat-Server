package ait.socket.chat;



import ait.socket.chat.task.ChatServerReceiver;
import ait.socket.chat.task.ChatServerSender;

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

        int port = 9000; // кого слушаем

        BlockingQueue<String> messageBox = new ArrayBlockingQueue<>(10);

        // Daemon for sender
        ChatServerSender sender = new ChatServerSender(messageBox);
        Thread senderThread = new Thread(sender);
        senderThread.setDaemon(true);
        senderThread.start();


        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //сервер должен быть постоянно запущен и ждать, что кто-то к нему обратиться
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while(true) {
                System.out.println("Server waiting...");
                Socket socket = serverSocket.accept(); // Главная функция сервера. Когда клиент подключается, сервер должен быть
                // в состоянии accept
                System.out.println("Connection established");
                System.out.println("Client host:" + socket.getInetAddress() + " : " + socket.getPort());
//                executorService.execute(new ClientHandler(socket));
                sender.addClient(socket); // добавили PrintWriter
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
