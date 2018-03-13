package is.yranac.canary.messages.scheduler;

/**
 * Created by sergeymorozov on 10/20/15.
 */
public class SchedulerCallback {
    private SchedulerType schedulerType;

    public SchedulerCallback(SchedulerType schedulerType){
        this.schedulerType = schedulerType;
    }

    public enum SchedulerType{
        ChirpTimeout
    }

    public SchedulerType getSchedulerType(){
        return schedulerType;
    }
}
