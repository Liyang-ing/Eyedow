package tw.yalan.eyedow;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Alan Ding on 2016/6/14.
 */
public abstract class EyedowService extends Service {
    public final static String ACTION_CLOSE = "tw.yalan.eyedow.CLOSE";
    public final static String ACTION_HIDE = "tw.yalan.eyedow.HIDE";
    public final static String ACTION_SHOW = "tw.yalan.eyedow.SHOW";

    private WindowManager windowManager;
    private EyedowContainer eyedowContainer;
    private boolean startForeground = true;
    private BroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            String action = intent.getAction();
            Log.e("eyedow", "onStartCommand  :" + intent.getAction());
            switch (action) {
                case ACTION_SHOW:
                    show();
                    break;
                case ACTION_HIDE:
                    hide();
                    break;
                case ACTION_CLOSE:
                    close();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Window", "OnCreate.");
        Layout annotation = this.getClass().getAnnotation(Layout.class);
        if (annotation == null) {
            Log.e("Window", "Layout annotation not found.");
            return;
        }
        int id = annotation.id();
        int containerWidth = annotation.width();
        int containerHeight = annotation.height();
        if (id == 0) {
            Log.e("Window", "Layout resId is 0.");
            return;
        }
        View view = LayoutInflater.from(this).inflate(id, null);
        if (view == null) {
            Log.e("Window", "ResId : " + id + " not found.");
            return;
        }
        windowManager = getWindowManager();

        eyedowContainer = new EyedowContainer(this, view, true);
        eyedowContainer.setContainerHeight(containerHeight);
        eyedowContainer.setContainerWidth(containerWidth);
        onViewBind(eyedowContainer);
        Log.i("Window", "Add view.");
//        eyedowContainer.show(getShowAnimation());
        EyedowServiceManager.get().add(this);

    }

    public abstract void onViewBind(EyedowContainer eyedowContainer);

    public WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
        return windowManager;
    }

    public boolean isStartForeground() {
        return startForeground;
    }

    public void removeEyedow() {
        if (eyedowContainer != null) {
            eyedowContainer.remove(getCloseAnimation());
            windowManager.removeView(eyedowContainer);
        }
    }

    @Override
    public void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        removeEyedow();
        super.onDestroy();
    }

    public void fullScreen() {
        eyedowContainer.fullScreen();
    }

    public void resizeScreen(int x, int y, int width, int height, boolean canDrag) {
        eyedowContainer.resizeScreen(x, y, width, height, canDrag);
    }

    public void revertScreen() {
        eyedowContainer.revertScreen();
    }

    public void show() {
        if (eyedowContainer != null) {
            eyedowContainer.show(getShowAnimation());
            if (isStartForeground()) {
                startForeground(this.getClass().hashCode(), getHiddenNotification());
            }
        }
    }

    public void hide() {
        if (eyedowContainer != null) {
            eyedowContainer.hide(getHideAnimation());
            if (isStartForeground()) {
                startForeground(this.getClass().hashCode(), getShowNotification());
            }
        }
    }

    public void close() {
        if (isStartForeground()) {
            stopForeground(true);
        }
        stopSelf();
    }

    public static Intent getShowIntent(Context context,
                                       Class<? extends EyedowService> cls) {
        String action = ACTION_SHOW;
        return new Intent(context, cls).setAction(action);
    }

    public static Intent getHideIntent(Context context,
                                       Class<? extends EyedowService> cls) {
        return new Intent(context, cls).setAction(
                ACTION_HIDE);
    }

    public Notification getShowNotification() {
        Intent showIntent = getShowIntent(this, getClass());
        PendingIntent contentIntent = PendingIntent.getService(
                getApplicationContext(),
                0,
                showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(contentIntent)
                .setContentTitle("Window is Running")
                .setContentText("點擊開啟")
                .setWhen(System.currentTimeMillis())
                .build();
        return notification;
    }

    public Notification getHiddenNotification() {
        Intent showIntent = getHideIntent(this, getClass());
        PendingIntent contentIntent = PendingIntent.getService(
                getApplicationContext(),
                0,
                showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(contentIntent)
                .setContentTitle("Window is Running")
                .setContentText("點擊關閉")
                .setWhen(System.currentTimeMillis())
                .build();
        return notification;

    }

    public Animation getShowAnimation() {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
    }

    public Animation getHideAnimation() {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
    }

    public Animation getCloseAnimation() {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
    }

}
