package lsieun.socks.simple;

import lsieun.socks.utils.IOUtils;
import lsieun.socks.utils.SocksUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SOCKS_Simple_Client {
    private static final String proxy_server = "127.0.0.1";
    private static final int proxy_port = 9999;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket();
        ) {
            socket.setSoTimeout(30000);
            SocketAddress addr = new InetSocketAddress(proxy_server, proxy_port);
            socket.connect(addr, 5000);

            InputStream in = socket.getInputStream();
            in = new BufferedInputStream(in);
            OutputStream out = socket.getOutputStream();
            out = new BufferedOutputStream(out);

            authenticate(in, out);

            connect(in, out);

            communicate(in, out);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void authenticate(InputStream in, OutputStream out) throws IOException {
        // (1) 向服务器发送数据
        int ver = 5;
        int n_methods = 1;
        byte[] methods_bytes = new byte[]{0};

        out.write(ver);
        out.write(n_methods);
        out.write(methods_bytes);
        out.flush();

        String request_info = SocksUtils.getFirstRequestInfo(ver, n_methods, methods_bytes);
        System.out.println(request_info);

        // (2) 从服务器接收数据
        int socks_version = in.read();
        int method = in.read();

        String response_info = SocksUtils.getFirstResponseInfo(socks_version, method);
        System.out.println(response_info);

    }

    public static void connect(InputStream in, OutputStream out) throws IOException {
        // (1) 向服务器发送数据
        int ver = 5;
        int cmd = 1;
        int rsv = 0;
        int type = 1;
        byte[] addr_bytes = new byte[]{93, (byte) 184, (byte) 216, 34};
        byte[] port_bytes = new byte[]{0, 80};

        out.write(ver);
        out.write(cmd);
        out.write(rsv);
        out.write(type);
        out.write(addr_bytes);
        out.write(port_bytes);
        out.flush();

        String request_info = SocksUtils.getSecondRequestInfo(ver, cmd, rsv, type, addr_bytes, port_bytes);
        System.out.println(request_info);

        // (2) 从服务器接收数据
        int socks_version = in.read();
        int reply = in.read();
        int reserved = in.read();
        int addr_type = in.read();
        byte[] server_ip_bytes = IOUtils.readBytes(in, 4);
        byte[] server_port_bytes = IOUtils.readBytes(in, 2);

        String response_info = SocksUtils.getSecondResponseInfo(socks_version, reply, reserved, addr_type, server_ip_bytes, server_port_bytes);
        System.out.println(response_info);
    }

    public static void communicate(InputStream in, OutputStream out) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("GET / HTTP/1.1");
        lines.add("Host: www.example.com");
        lines.add("Connection: close");
        lines.add("User-Agent: Mozilla/5.0");
        lines.add("Accept: text/html");
        lines.add("Accept-Language: en-US");
        sendHttpRequest(out, lines);

        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println();

        for (int value = in.read(); value != -1; value = in.read()) {
            System.out.printf("%c", value);
        }
    }

    public static void sendHttpRequest(OutputStream out, List<String> lines) throws IOException {
        for (String line : lines) {
            byte[] bytes = (line + "\r\n").getBytes(StandardCharsets.US_ASCII);
            out.write(bytes);
        }
        out.write("\r\n".getBytes());
        out.flush();
    }

}
