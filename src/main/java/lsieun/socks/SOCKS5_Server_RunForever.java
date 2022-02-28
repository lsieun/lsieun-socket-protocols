package lsieun.socks;

import lsieun.socks.utils.SocksConst;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * http://photo.china.com.cn
 */
public class SOCKS5_Server_RunForever {

    public static void main(String[] args) {
        try (
                ServerSocket serverSocket = new ServerSocket();
        ) {
            SocketAddress http = new InetSocketAddress(SocksConst.LISTEN_PORT);
            serverSocket.bind(http);
            while (true) {
                try {
                    /**
                     * 问题：下面的socket应不应该在当前main方法中关闭呢？
                     * 回答：不能。
                     * 原因：如果在main方法里调用了socket.close()方法，那么在ErrorRelayThread中就无法使用了。
                     * 解决：只能在ErrorRelayThread结束时进行关闭。
                     */
                    Socket socket = serverSocket.accept();
                    Thread t = new ConnectionPerThread(socket);
                    t.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
