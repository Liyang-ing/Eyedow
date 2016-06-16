package tw.yalan.eyedow;

import android.view.animation.Animation;

/**
 * Created by Alan Ding on 2016/6/15.
 */
public interface Eyedow {
    void fullScreen();

    void resizeScreen(int x, int y, int width, int height, boolean canDrag);

    void revertScreen();

    void show(Animation animation);

    void hide(Animation animation);

    void remove(Animation animation);

}
