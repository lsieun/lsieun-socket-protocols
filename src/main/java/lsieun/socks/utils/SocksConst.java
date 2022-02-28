package lsieun.socks.utils;

public class SocksConst {
    public static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final byte[] LOCAL_LOOPBACK_ADDR = new byte[]{127, 0, 0, 1};

    public static final int SOCKS_VERSION_4 = 4;
    public static final int SOCKS_VERSION_5 = 5;
    public static final int SOCKS_VERSION_6 = 6;

    public static final int LISTEN_PORT = 8999;
    public static final int BUFFER_SIZE = 1024 * 256;

    public static final int CONNECT_TIMEOUT = 15 * 1000;
    public static final int READ_TIMEOUT = 30 * 1000;
    public static final int LESSER_READ_TIMEOUT = 10 * 1000;

    public static final String EOL = System.lineSeparator();
}
