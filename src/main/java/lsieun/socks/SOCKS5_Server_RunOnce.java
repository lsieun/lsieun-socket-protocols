package lsieun.socks;

import lsieun.socks.utils.SocketHandler;
import lsieun.socks.utils.SocksConst;

import java.net.ServerSocket;
import java.net.Socket;


/**
 * NOTE: 1不能阻塞
 * NOTE: 2必须要flush
 * NOTE: 3不能访问https，而只能访问http网站
 * NOTE: 4写一个最简单的，最接近SOCKS规范的程序
 */
public class SOCKS5_Server_RunOnce {
    public static void main(String[] args) {
        try (
                ServerSocket ss = new ServerSocket(SocksConst.LISTEN_PORT);
                Socket src_socket = ss.accept();
                Socket dest_socket = new Socket();
        ) {
            // NOTE: 这让我学到了，即使通过accept()方法获取的Socket也可以设置read timeout
            // 如果不设置，可能要一直等待下去
            src_socket.setSoTimeout(SocksConst.LESSER_READ_TIMEOUT);

            SocketHandler.process(src_socket, dest_socket, DataRelay_SingleThread.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
