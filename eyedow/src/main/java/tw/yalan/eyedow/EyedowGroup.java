package tw.yalan.eyedow;

/**
 * Created by Alan Ding on 2016/6/15.
 */
public interface EyedowGroup {
    void add(EyedowService service);

    EyedowService get(Class<? extends EyedowService> cls);

    void remove(Class<? extends EyedowService> cls);

}
