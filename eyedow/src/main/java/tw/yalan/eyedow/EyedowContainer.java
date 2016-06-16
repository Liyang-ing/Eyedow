package tw.yalan.eyedow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import tw.yalan.eyedow.util.ViewHelper;

/**
 * Created by Alan Ding on 2016/6/15.
 */
public class EyedowContainer extends FrameLayout implements Eyedow {


    public interface onScreenSizeChangeListener {
        void onFullScreen();

        void onResize(int x, int y, int width, int height, boolean canDrag);

        void onRevert(int x, int y, int width, int height);
    }

    private onScreenSizeChangeListener onScreenSizeChangeListener;
    private View view;
    private WindowManager windowManager;
    private int containerWidth = -1;
    private int containerHeight = -1;
    private boolean canDrag;
    private boolean isAttachToWindow = false;
    private int lastX, lastY;
    private boolean isFullScreen = false;
    private boolean isDefaultSize = true;
    private boolean lastCanDragTemp;

    public EyedowContainer(Context context, View view, boolean canDrag) {
        super(context);
        this.view = view;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        addView(this.view, new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        this.canDrag = canDrag;
        if (canDrag) {
            setOnTouchListener(new EyedowDragTouchListener(windowManager));
        }
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
        if (canDrag) {
            setOnTouchListener(new EyedowDragTouchListener(windowManager));
        } else {
            setOnTouchListener(null);
        }
    }

    public int getContainerWidth() {
        return containerWidth;
    }

    public int getContainerHeight() {
        return containerHeight;
    }

    public void setContainerWidth(int containerWidth) {
        this.containerWidth = containerWidth;
    }

    public void setContainerHeight(int containerHeight) {
        this.containerHeight = containerHeight;
    }

    public EyedowContainer.onScreenSizeChangeListener getOnScreenSizeChangeListener() {
        return onScreenSizeChangeListener;
    }

    public void setOnScreenSizeChangeListener(EyedowContainer.onScreenSizeChangeListener onScreenSizeChangeListener) {
        this.onScreenSizeChangeListener = onScreenSizeChangeListener;
    }

    @Override
    public void fullScreen() {
        isDefaultSize = false;
        isFullScreen = true;
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lastX = layoutParams.x;
        lastY = layoutParams.y;
        layoutParams.x = 0;
        layoutParams.y = 0;
        lastCanDragTemp = this.canDrag;
        windowManager.removeView(this);
        windowManager.addView(this, layoutParams);
        if (onScreenSizeChangeListener != null) {
            onScreenSizeChangeListener.onFullScreen();
        }
    }

    @Override
    public void resizeScreen(int x, int y, int width, int height, boolean canDrag) {
        isDefaultSize = false;
        isFullScreen = false;

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
        layoutParams.height = width;
        layoutParams.width = height;
        lastX = layoutParams.x;
        lastY = layoutParams.y;
        layoutParams.x = x;
        layoutParams.y = y;
        lastCanDragTemp = this.canDrag;
        setCanDrag(canDrag);
        windowManager.removeView(this);
        windowManager.addView(this, layoutParams);
        if (onScreenSizeChangeListener != null) {
            onScreenSizeChangeListener.onResize(x, y, width, height, canDrag);
        }
    }

    @Override
    public void revertScreen() {
        isDefaultSize = true;
        isFullScreen = false;

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
        layoutParams.height = ViewHelper.dpToPx(getContext(), containerHeight);
        layoutParams.width = ViewHelper.dpToPx(getContext(), containerWidth);
        layoutParams.x = lastX;
        layoutParams.y = lastY;
        setCanDrag(lastCanDragTemp);
        windowManager.removeView(this);
        windowManager.addView(this, layoutParams);
        if (onScreenSizeChangeListener != null) {
            onScreenSizeChangeListener.onRevert(lastX, lastY, layoutParams.width, layoutParams.height);
        }
    }

    @Override
    public void show(final Animation animation) {
        Log.e("eyedow", "show [" + this.getClass().getSimpleName() + "]");
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!isAttachedToWindow()) {
                    Log.e("eyedow", "add view [" + this.getClass().getSimpleName() + "]");
                    windowManager.addView(this, createLayoutParams(10, 100));
                }
            } else {
                if (!isAttachToWindow()) {
                    Log.e("eyedow", "add view [" + this.getClass().getSimpleName() + "]");
                    windowManager.addView(this, createLayoutParams(10, 100));
                }
            }
            Log.e("eyedow", "set Visible [" + this.getClass().getSimpleName() + "]");
            setVisibility(View.VISIBLE);
            getChildAt(0).startAnimation(animation);
            isAttachToWindow = true;
        }
    }

    public boolean isAttachToWindow() {
        return isAttachToWindow;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public boolean isDefaultSize() {
        return isDefaultSize;
    }

    @Override
    public void hide(Animation animation) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (isAttachedToWindow()) {
                        setVisibility(View.GONE);
                    }
                } else {
                    if (isAttachToWindow()) {
                        setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isAttachedToWindow()) {
                getChildAt(0).startAnimation(animation);
            }
        } else {
            if (isAttachToWindow()) {
                getChildAt(0).startAnimation(animation);
            }
        }
    }

    @Override
    public void remove(Animation animation) {
        isAttachToWindow = false;
        getChildAt(0).startAnimation(animation);
    }


    public WindowManager.LayoutParams createLayoutParams(int x, int y) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                containerWidth == -1 ? WindowManager.LayoutParams.WRAP_CONTENT : ViewHelper.dpToPx(getContext(), containerWidth),
                containerHeight == -1 ? WindowManager.LayoutParams.WRAP_CONTENT : ViewHelper.dpToPx(getContext(), containerHeight),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = x;
        params.y = y;
        return params;
    }
}
