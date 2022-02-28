package lsieun.socks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface DataRelay {
    void transfer(InputStream src_in, OutputStream src_out,
                  InputStream dest_in, OutputStream dest_out) throws IOException;
}
