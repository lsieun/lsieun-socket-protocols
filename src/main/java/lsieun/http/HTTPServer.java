package lsieun.http;

import lsieun.bean.HttpBean;
import lsieun.socks.utils.SocksConst;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class HTTPServer {
    public static void main(String[] args) {
        try (
                ServerSocket serverSocket = new ServerSocket();
        ) {
            SocketAddress http = new InetSocketAddress(SocksConst.LISTEN_PORT);
            serverSocket.bind(http);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    InputStream in = socket.getInputStream();
                    HttpBean bean = new HttpBean(in);
                    System.out.println(bean.toString());
                    socket.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
