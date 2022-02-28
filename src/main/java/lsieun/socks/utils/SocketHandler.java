package lsieun.socks.utils;

import lsieun.socks.DataRelay;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

import static lsieun.socks.utils.SocksConst.CONNECT_TIMEOUT;
import static lsieun.socks.utils.SocksConst.READ_TIMEOUT;

public class SocketHandler {
    public static void process(Socket src_socket, Socket dest_socket, Class<? extends DataRelay> clazz) {
        try {


            // (*) InputStream and OutputStream
            InputStream src_in = src_socket.getInputStream();
            src_in = new BufferedInputStream(src_in);
            OutputStream src_out = src_socket.getOutputStream();
            src_out = new BufferedOutputStream(src_out);

            // (1) authenticate
            SocksUtils.authenticate(src_in, src_out);

            // (2) connect
            SocketAddress addr = SocksUtils.connect(src_in, src_out);

            // (*) InputStream and OutputStream
            dest_socket.setSoTimeout(READ_TIMEOUT);
            dest_socket.connect(addr, CONNECT_TIMEOUT);

            InputStream dest_in = dest_socket.getInputStream();
            dest_in = new BufferedInputStream(dest_in);
            OutputStream dest_out = dest_socket.getOutputStream();
            dest_out = new BufferedOutputStream(dest_out);

            // (3) transfer
            DataRelay dataRelay = clazz.newInstance();
            dataRelay.transfer(src_in, src_out, dest_in, dest_out);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeSocket(src_socket);
            closeSocket(dest_socket);
        }
    }

    public static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            // do nothing
        }
    }
}
