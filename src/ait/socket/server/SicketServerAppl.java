package ait.socket.server;



import ait.socket.server.task.ClientHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (16.06.2025)
 */
public class SicketServerAppl {
    public static void main(String[] args) throws InterruptedException {

        int port = 9000; // кого слушаем
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //сервер должен быть постоянно запущен и ждать, что кто-то к нему обратиться
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while(true) {
                System.out.println("Server waiting...");
                Socket socket = serverSocket.accept(); // Главная функция сервера. Когда клиент подключается, сервер должен быть
                // в состоянии accept
                System.out.println("Connection established");
                System.out.println("Client host:" + socket.getInetAddress() + " : " + socket.getPort());
                executorService.execute(new ClientHandler(socket));
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
