package is.yranac.canary.services.bluetooth.constants;

import java.util.UUID;

/**
 * Created by shreyashirday on 7/31/15.
 */
public class BluetoothUUIDConstants {

    public static class Service {
        final static public UUID SERIAL_SERVICE           = UUID.fromString("00005500-D102-11E1-9B23-00025B00A5A5");
    };

    public static class Characteristic {

        final static public UUID SERIAL_DATA_TRANSFER     = UUID.fromString("00005501-d102-11e1-9b23-00025b00a5a5");
        final static public UUID SERIAL_NONCE             = UUID.fromString("00005502-d102-11e1-9b23-00025b00a5a5");
        final static public UUID SERIAL_SIG               = UUID.fromString("00005503-d102-11e1-9b23-00025b00a5a5");

    }

    public static class Descriptor {
        final static public UUID CHAR_CLIENT_CONFIG       = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    }

}
