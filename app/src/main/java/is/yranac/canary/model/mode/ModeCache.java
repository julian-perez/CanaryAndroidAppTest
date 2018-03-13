package is.yranac.canary.model.mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.yranac.canary.services.database.ModeDatabaseService;
import is.yranac.canary.util.Log;

/**
 * Created by michaelschroeder on 8/31/17.
 */

public class ModeCache {

    public static final String armed = "armed";
    public static final String disarmed = "disarmed";
    public static final String privacy = "privacy";
    public static final String home = "home";
    public static final String away = "away";
    public static final String night = "night";
    public static final String standby = "standby";
    private static final String LOG_TAG = "ModeCache";

    private static ModeCache modeCache;

    public static ModeCache geModeCache() {
        if (modeCache == null) {
            modeCache = new ModeCache();
        }
        return modeCache;
    }

    public static Mode getMode(String modeName) {
        if (geModeCache().modeMap.isEmpty()) {
            geModeCache().updateModeCache();
        }
        Mode mode = geModeCache().modeMap.get(modeName);

        if (mode == null) {
            return Mode.Error();
        }

        return mode;
    }

    private Map<String, Mode> modeMap = new HashMap<>();

    public void updateModeCache() {

        List<Mode> modes = ModeDatabaseService.getAllModes();

        for (Mode mode : modes) {
            modeMap.put(mode.name, mode);
        }
    }
}
