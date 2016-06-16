package tw.yalan.eyedow;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Alan Ding on 2016/6/15.
 */
public class EyedowServiceManager implements EyedowGroup {
    private static EyedowServiceManager ourInstance = new EyedowServiceManager();
    private HashMap<Class<? extends EyedowService>, EyedowService> eyedowsMap = new HashMap<>();

    public static EyedowServiceManager get() {
        return ourInstance;
    }

    public static void show(Context context, Class<? extends EyedowService> cls) {
        get().showEyedow(context, cls);
    }

    private void showEyedow(Context context, Class<? extends EyedowService> cls) {
        if (eyedowsMap.containsKey(cls)) {
            get(cls).show();
        } else {
            context.startService(EyedowService.getShowIntent(context, cls));
        }
    }

    private EyedowServiceManager() {
    }


    @Override
    public void add(EyedowService service) {
        eyedowsMap.put(service.getClass(), service);
    }

    @Override
    public EyedowService get(Class<? extends EyedowService> cls) {
        return eyedowsMap.get(cls);
    }

    @Override
    public void remove(Class<? extends EyedowService> cls) {
        eyedowsMap.remove(cls);
    }

    public boolean isExist(Class<? extends EyedowService> cls) {
        return eyedowsMap.containsKey(cls);
    }
}
