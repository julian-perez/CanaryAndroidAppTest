package is.yranac.canary.model.emergencycontacts;

/**
 * Created by Schroeder on 10/7/15.
 */
public class ReplaceEmergencyNumbers {
    public final String policeNumber;
    public final String fireNumber;
    public final String emsNumber;

    public ReplaceEmergencyNumbers() {
        policeNumber = "";
        fireNumber = "";
        emsNumber = "";
    }

    public ReplaceEmergencyNumbers(String policeNumber, String fireNumber, String emsNumber) {
        this.policeNumber = policeNumber;
        this.fireNumber = fireNumber;
        this.emsNumber = emsNumber;

    }


}
