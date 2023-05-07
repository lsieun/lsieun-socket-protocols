package lsieun.mqtt;

import lsieun.utils.HexFormat;
import lsieun.utils.HexUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class MQTTServer {
    public static void main(String[] args) {
        try (
                ServerSocket serverSocket = new ServerSocket();
        ) {
            SocketAddress http = new InetSocketAddress(MQTTConst.LISTEN_PORT);
            serverSocket.bind(http);


            while (true) {
                try {
                    Socket socket = serverSocket.accept();

                    readBytes(socket);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readBytes(Socket socket) throws IOException {
        System.out.println("read bytes");
        InputStream in = socket.getInputStream();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        byte[] buff = new byte[1024];
        int length = -1;
        while ((length = in.read(buff)) > 0) {
            bao.write(buff, 0, length);
            break;
        }

        System.out.println("length = " + length);

        byte[] bytes = bao.toByteArray();
        String str = HexUtils.format(bytes, HexFormat.FORMAT_FF_SPACE_FF_16);
        System.out.println(str);

        MQTTControlPacketFormat.interpret(bytes);

        in.close();
        socket.close();
    }
}
