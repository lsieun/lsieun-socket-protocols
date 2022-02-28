package lsieun.time;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;

/**
 * When reading data from the network, it’s important to keep in mind that not all pro‐
 * tocols use ASCII or even text. For example, the time protocol specified in RFC 868
 * specifies that the time be sent as the number of seconds since midnight, January 1, 1900,
 * Greenwich Mean Time. However, this is not sent as an ASCII string like 2,524,521,600
 * or –1297728000. Rather, it is sent as a 32-bit, unsigned, big-endian binary number.
 */
public class Time {
    private static final String HOSTNAME = "time.nist.gov";
    private static final int PORT = 37;

    public static void main(String[] args) throws IOException {
        Date d = Time.getDateFromNetwork();
        System.out.println("It is " + d);
    }

    public static Date getDateFromNetwork() throws IOException {
        // The time protocol sets the epoch at 1900,
        // the Java Date class at 1970. This number
        // converts between them.
        long differenceBetweenEpochs = 2208988800L;

        try (
                Socket socket = new Socket(HOSTNAME, PORT);
        ) {
            socket.setSoTimeout(15000);
            InputStream raw = socket.getInputStream();
            long secondsSince1900 = 0;
            for (int i = 0; i < 4; i++) {
                secondsSince1900 = (secondsSince1900 << 8) | raw.read();
            }
            long secondsSince1970 = secondsSince1900 - differenceBetweenEpochs;
            long msSince1970 = secondsSince1970 * 1000;
            Date time = new Date(msSince1970);
            return time;
        }
    }
}
