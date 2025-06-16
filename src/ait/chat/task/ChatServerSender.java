package ait.chat.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (16.06.2025)
 */
public class ChatServerSender implements Runnable{
    private final BlockingQueue<String> messageBox;
    //We need PrintWriter for each client
    private final Set<PrintWriter> clients;

    public ChatServerSender(BlockingQueue<String> messageBox) {
        this.messageBox = messageBox;
        clients = new HashSet<>(); // At starting we have 0 clients, connected to out server
    }

    public boolean addClient(Socket socket) throws IOException {
        return clients.add(new PrintWriter(socket.getOutputStream(), true));
    }

    @Override
    public void run() {
        try {
            while(true) {
                String message = messageBox.take();
                clients.forEach(c -> c.println(message));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
