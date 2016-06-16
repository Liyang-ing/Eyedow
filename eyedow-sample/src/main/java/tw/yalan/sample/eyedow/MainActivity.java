package tw.yalan.sample.eyedow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import tw.yalan.eyedow.EyedowServiceManager;
import tw.yalan.sample.eyedow.service.EyedowDemoResize;
import tw.yalan.sample.eyedow.service.EyedowDemoFullScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnShow1 = (Button) findViewById(R.id.btn_test);
        btnShow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EyedowServiceManager.show(MainActivity.this, EyedowDemoResize.class);
            }
        });

        Button btnShow2 = (Button) findViewById(R.id.btn_test_2);
        btnShow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EyedowServiceManager.show(MainActivity.this, EyedowDemoFullScreen.class);
            }
        });
    }
}
