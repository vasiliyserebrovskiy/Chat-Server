package ait.socket.chat.task;

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
        // TODO
    }
}
