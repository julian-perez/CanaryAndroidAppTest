package is.yranac.canary.messages;

public class EntryTableUpdated {
    public int location = 0;
    public boolean someReturn = false;

    public EntryTableUpdated(boolean someReturn, int location) {
        this.location = location;
        this.someReturn = someReturn;
    }
}
