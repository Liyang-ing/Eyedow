package tw.yalan.eyedow;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Alan Ding on 2016/6/15.
 */
public class EyedowDragTouchListener implements View.OnTouchListener {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private WindowManager windowManager;

    public EyedowDragTouchListener(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) v.getLayoutParams();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = params.x;
                initialY = params.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            case MotionEvent.ACTION_MOVE:
                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                params.y = initialY + (int) (event.getRawY() - initialTouchY);
                windowManager.updateViewLayout(v, params);
                return true;
        }
        return false;
    }
}
