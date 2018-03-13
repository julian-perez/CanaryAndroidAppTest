package is.yranac.canary.nativelibs;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.nativelibs.models.messages.BLEMessage;
import is.yranac.canary.nativelibs.models.messages.BLEMessageRequest;
import is.yranac.canary.nativelibs.models.messages.BLEMessageResponse;
import is.yranac.canary.nativelibs.models.messages.BLEResentRequest;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.TinyMessageBus;

/**
 * Created by sergeymorozov on 10/28/15.
 * We're gonna implement the actual queue when we need it.
 */
public class BTLEMessageQueue {

    private static final String LOG_TAG = "BTLEMessageQueue";

    private static BTLEMessageQueue queue;
    private static ConcurrentLinkedQueue<BLEMessageRequest> messageQueue;

    static Queue<BLEMessageRequest> getMessageQueue(){
        if(messageQueue == null)
            messageQueue = new ConcurrentLinkedQueue<>();
        return messageQueue;
    }

    static BTLEMessageQueue getInstance(){
        if(queue == null){
            queue = new BTLEMessageQueue();
        }
        return queue;
    }

    @Subscribe(mode = Subscribe.Mode.Main)
    public void sendMessageToDevice(BLEMessageRequest request){
        Log.i(LOG_TAG, "send message to device");
        clearMessageQueue();
        getMessageQueue().add(request);
        TinyMessageBus.post(new BLEMessage(request.getJsonString()));
    }

    @Subscribe(mode = Subscribe.Mode.Main)
    public void deviceResponseRecieved(BLEMessageResponse response){
        getMessageQueue().poll();
        TinyMessageBus.post(response.getMessage());
    }

    @Subscribe(mode = Subscribe.Mode.Main)
    public void resendLastMessage(BLEResentRequest request){
        if(!getMessageQueue().isEmpty()){
            TinyMessageBus.post(new BLEMessage(getMessageQueue().peek().getJsonString()));
        }
    }

    public static void clearMessageQueue(){
        getMessageQueue().clear();
    }

    public static void register(){
        TinyMessageBus.register(getInstance());
        BTLEMessageBus.register();
    }

    public static void unregister(){
        TinyMessageBus.unregister(getInstance());
        BTLEMessageBus.unregister();
    }
}
