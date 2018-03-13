package is.yranac.canary.messages.watchlive;

import WL.Wl2;

import static is.yranac.canary.services.watchlive.TCPClient.HEADER_SIZE;
import static is.yranac.canary.services.watchlive.TCPClient.concatenateByteArrays;
import static is.yranac.canary.services.watchlive.TCPClient.toBytes;

/**
 * Created by michaelschroeder on 5/26/17.
 */

public class ByteMessage {
    public final Wl2.Channel cmd;
    public final boolean close;
    public byte[] commandMessageBytes;

    public ByteMessage(byte[] commandMessageBytes, Wl2.Channel cmd, boolean close) {

        this.commandMessageBytes = commandMessageBytes;
        this.cmd = cmd;
        this.close = close;
    }

    public byte[] getFullMessage() {
        byte[] packet = commandMessageBytes;

        byte[] length = toBytes(packet.length);
        byte[] header = new byte[HEADER_SIZE];
        System.arraycopy(length, 0, header, 0, length.length);

        header[2] = (byte) cmd.getNumber();

        return concatenateByteArrays(header, packet);
    }
}
