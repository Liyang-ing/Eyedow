package tw.yalan.sample.eyedow.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import tw.yalan.eyedow.EyedowContainer;
import tw.yalan.eyedow.EyedowService;
import tw.yalan.eyedow.Layout;
import tw.yalan.sample.eyedow.R;


/**
 * Created by Alan Ding on 2016/6/15.
 */
@Layout(id = R.layout.demo_body_2, width = 200, height = 300)
public class EyedowDemoFullScreen extends EyedowService {

    @Override
    public void onViewBind(final EyedowContainer eyedowContainer) {
        View btn1 = eyedowContainer.findViewById(R.id.btn_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EyedowDemoFullScreen.this, "hide", Toast.LENGTH_SHORT).show();
                hide();
            }
        });
        View btn2 = eyedowContainer.findViewById(R.id.btn_2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        View btn3 = eyedowContainer.findViewById(R.id.btn_full);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eyedowContainer.isFullScreen()) {
                    revertScreen();
                } else {
                    fullScreen();
                }
            }
        });
        eyedowContainer.setOnScreenSizeChangeListener(new EyedowContainer.onScreenSizeChangeListener() {
            @Override
            public void onFullScreen() {
                //After fullScreen
            }

            @Override
            public void onResize(int x, int y, int width, int height, boolean canDrag) {
                //After eyedow resize

            }

            @Override
            public void onRevert(int x, int y, int width, int height) {
                //After eyedow size revert
            }
        });
    }

    @Override
    public Notification getShowNotification() {
        Intent showIntent = getShowIntent(this, EyedowDemoFullScreen.class);
        PendingIntent contentIntent = PendingIntent.getService(
                getApplicationContext(),
                0,
                showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(contentIntent)
                .setContentTitle(getClass().getSimpleName() + " is Hidden")
                .setContentText("Click to show.")
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher).build();
        return notification;
    }

    @Override
    public Notification getHiddenNotification() {
        Intent showIntent = getHideIntent(this, EyedowDemoFullScreen.class);
        PendingIntent contentIntent = PendingIntent.getService(
                getApplicationContext(),
                0,
                showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(contentIntent)
                .setContentTitle(getClass().getSimpleName() + " is Showing")
                .setContentText("Click to Hide.")
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher).build();
        return notification;
    }
}
