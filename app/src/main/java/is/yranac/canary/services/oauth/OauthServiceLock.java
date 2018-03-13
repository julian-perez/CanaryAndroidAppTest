package is.yranac.canary.services.oauth;

import android.net.LocalServerSocket;

import java.io.IOException;

/**
 * Created by michael on 6/14/17.
 */

public class OauthServiceLock  {

    private final String name;

    private LocalServerSocket server;

    public OauthServiceLock(String name) {
        this.name = name;
    }

    public final synchronized void tryLock() throws IOException {
        if (server == null) {
            server = new LocalServerSocket(name);
        } else {
            throw new IllegalStateException("tryLock but has locked");
        }
    }

    public final synchronized boolean timedLock(int ms) {
        long expiredTime = System.currentTimeMillis() + ms;

        while (true) {
            if (System.currentTimeMillis() > expiredTime) {
                return false;
            }
            try {
                try {
                    tryLock();
                    return true;
                } catch (IOException e) {
                    // ignore the exception
                }
                Thread.sleep(10, 0);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    public final synchronized void lock() {
        while (true) {
            try {
                try {
                    tryLock();
                    return;
                } catch (IOException e) {
                    // ignore the exception
                }
                Thread.sleep(10, 0);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    public final synchronized void release() {
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                // ignore the exception
            }
        }
    }

}
