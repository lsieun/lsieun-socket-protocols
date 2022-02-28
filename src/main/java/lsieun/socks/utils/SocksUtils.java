package lsieun.socks.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import static lsieun.socks.utils.SocksConst.*;

public class SocksUtils {

    public static void authenticate(InputStream in, OutputStream out) throws IOException {
        // (1) 从浏览器端读取数据
        int ver = in.read();
        int n_methods = in.read();
        byte[] methods_bytes = new byte[n_methods];
        for (int i = 0; i < n_methods; i++) {
            int value = in.read();
            methods_bytes[i] = (byte) value;
        }

        String request_info = getFirstRequestInfo(ver, n_methods, methods_bytes);
        System.out.println(request_info);

        // (2) 向浏览器返回数据
        int socks_version = SOCKS_VERSION_5;
        int method = 0;
        out.write(socks_version);
        out.write(0);
        out.flush();
        String response_info = getFirstResponseInfo(socks_version, method);
        System.out.println(response_info);

    }

    public static SocketAddress connect(InputStream in, OutputStream out) throws IOException {
        // (1) 从浏览器端读取数据
        int ver = in.read();
        int cmd = in.read();
        int rsv = in.read();
        int type = in.read();
        byte[] addr_bytes = IOUtils.readBytes(in, 4);
        byte[] port_bytes = IOUtils.readBytes(in, 2);

        String request_info = getSecondRequestInfo(ver, cmd, rsv, type, addr_bytes, port_bytes);
        System.out.println(request_info);

        // (2) 向浏览器返回数据
        int socks_version = SOCKS_VERSION_5;
        int reply = 0;
        int reserved = 0;
        int addr_type = 1;
        byte[] fake_ip_bytes = new byte[]{0, 0, 0, 0};
        byte[] fake_port_bytes = new byte[]{0, 0};

        out.write(socks_version);
        out.write(reply);
        out.write(reserved);
        out.write(addr_type);
        out.write(fake_ip_bytes);
        out.write(fake_port_bytes);
        out.flush();

        String response_info = getSecondResponseInfo(socks_version, reply, reserved, addr_type, fake_ip_bytes, fake_port_bytes);
        System.out.println(response_info);

        // (3) 构建SocketAddress信息
        String ipv4 = ByteUtils.toIPV4(addr_bytes);
        int port = ByteUtils.toPort(port_bytes);
        return new InetSocketAddress(ipv4, port);

    }

    public static String getFirstRequestInfo(int ver, int n_methods, byte[] methods) {
        String line = String.format("| %-4s | %-8s | %-8s |", ver, n_methods, ByteUtils.toHex(methods));
        return new StringBuilder()
                .append("| VER  | NMETHODS | METHODS  |" + EOL)
                .append("| ---- | -------- | -------- |" + EOL)
                .append(line + EOL)
                .toString();
    }

    public static String getFirstResponseInfo(int ver, int method) {
        String line = String.format("| %-4s | %-6s |", ver, method);
        return new StringBuilder()
                .append("| VER  | METHOD |" + EOL)
                .append("| ---- | ------ |" + EOL)
                .append(line + EOL)
                .toString();
    }

    public static String getSecondRequestInfo(int ver, int cmd, int rsv, int type, byte[] addr_bytes, byte[] port_bytes) {
        String line = String.format("| %-4s | %-4s | %-5s | %-4s | %-15s | %-8s |", ver, cmd, rsv, type, ByteUtils.toIPV4(addr_bytes), ByteUtils.toPort(port_bytes));
        return new StringBuilder()
                .append("| VER  | CMD  | RSV   | ATYP | DST.ADDR        | DST.PORT |" + EOL)
                .append("| ---- | ---- | ----- | ---- | --------------- | -------- |" + EOL)
                .append(line + EOL)
                .toString();
    }

    public static String getSecondResponseInfo(int ver, int rep, int rsv, int type, byte[] addr_bytes, byte[] port_bytes) {
        String line = String.format("| %-4s | %-4s | %-5s | %-4s | %-15s | %-8s |", ver, rep, rsv, type, ByteUtils.toIPV4(addr_bytes), ByteUtils.toPort(port_bytes));
        return new StringBuilder()
                .append("| VER  | REP  | RSV   | ATYP | BND.ADDR        | BND.PORT |" + EOL)
                .append("| ---- | ---- | ----- | ---- | --------------- | -------- |" + EOL)
                .append(line + EOL)
                .toString();
    }

    public static byte[] getPortBytes(int port) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.putInt(port);
        byte[] four_bytes = byteBuffer.array();
        byte[] bytes = new byte[2];
        bytes[0] = four_bytes[2];
        bytes[1] = four_bytes[3];
        return bytes;
    }
}
