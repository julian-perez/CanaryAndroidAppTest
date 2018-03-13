package is.yranac.canary.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import is.yranac.canary.contentproviders.CanaryReadingContentProvider;
import is.yranac.canary.util.StringUtils;

/**
 * Created by Schroeder on 10/21/14.
 */
public class PastReadingsUpdated {
    private HashMap<String, Set<Integer>> updatedTypesForDevices;

    public PastReadingsUpdated(HashMap<String, Set<Integer>> updatedTypesForDevices) {
        this.updatedTypesForDevices = updatedTypesForDevices;
    }

    public List<Integer> getUpdatedReadingTypesForDevice(String deviceUUID) {
        ArrayList<Integer> readingTypes = new ArrayList<>();

        if (StringUtils.isNullOrEmpty(deviceUUID) || this.updatedTypesForDevices == null)
            return readingTypes;

        for (String uuid : this.updatedTypesForDevices.keySet()) {
            if (uuid.equals(deviceUUID))
                readingTypes.addAll(this.updatedTypesForDevices.get(uuid));
        }

        return readingTypes;
    }

    public boolean isNewReadingTypeAvailableForDevice(String deviceUUID, int readingType) {
        if (StringUtils.isNullOrEmpty(deviceUUID)
                || !CanaryReadingContentProvider.isValidReadingType(readingType))
            return false;

        List<Integer> readingTypes = getUpdatedReadingTypesForDevice(deviceUUID);

        for (Integer type : readingTypes) {
            if (type == readingType)
                return true;
        }

        return false;
    }
}
