package ait.chat.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
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

    // add and send operation must be synchronized
    public synchronized boolean addClient(Socket socket) throws IOException {
        return clients.add(new PrintWriter(socket.getOutputStream(), true));
    }

    @Override
    public void run() {
        try {
            while(true) {
                String message = messageBox.take();
                //Use iterator to work with old PrintWriters for disconnected clients
                //We need bad PrintWriter remove from clients HashSet
                synchronized (this) {
                    Iterator<PrintWriter> iterator = clients.iterator();
                    while (iterator.hasNext()) {
                        PrintWriter clientWriter = iterator.next();
                        if (clientWriter.checkError()) {
                            iterator.remove();
                        } else {
                            clientWriter.println(message);
                        }
                    }
                    System.out.println("Size = " + clients.size());
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
