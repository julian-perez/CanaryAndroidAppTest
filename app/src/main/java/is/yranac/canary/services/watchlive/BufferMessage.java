package is.yranac.canary.services.watchlive;

import com.google.protobuf.InvalidProtocolBufferException;

import WL.Wl2;


/**
 * Created by michaelschroeder on 3/20/17.
 */

public class BufferMessage {

    public final int channelValue;

    public final byte[] packet;

    public final int meta;
    public final int customer;

    public BufferMessage(int channelValue, byte[] packet, int meta, int customer) {

        this.channelValue = channelValue;
        this.packet = packet;
        this.meta = meta;

        this.customer = customer;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) + ", channel " +
                getChannel().toString() + ", packet size " + String.valueOf(packet.length);
    }

    public Wl2.Channel getChannel() {
        if (channelValue >= Wl2.Channel.values().length) {
            return Wl2.Channel.CMD;
        }
        return Wl2.Channel.values()[channelValue];
    }

    public Wl2.CommandMessage getCommandMessage() {
        try {
            return Wl2.CommandMessage.parseFrom(packet);
        } catch (InvalidProtocolBufferException e) {
            return null;
        }
    }

}
