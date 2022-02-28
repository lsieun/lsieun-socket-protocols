package lsieun.socks;

import lsieun.socks.utils.SocketHandler;

import java.net.Socket;

public class ConnectionPerThread extends Thread {
    private final Socket src_socket;
    private final Socket dest_socket;

    public ConnectionPerThread(Socket src_socket) {
        this.src_socket = src_socket;
        this.dest_socket = new Socket();
    }

    @Override
    public void run() {
        SocketHandler.process(src_socket, dest_socket, DataRelay_MultiThread.class);
    }
}
