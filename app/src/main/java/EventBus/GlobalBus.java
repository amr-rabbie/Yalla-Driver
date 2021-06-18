package EventBus;

/**
 * Created by m.khalid on 5/10/2017.
 */

import org.greenrobot.eventbus.EventBus;

public class GlobalBus {
    public static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }
}

