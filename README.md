
#Eyedow

![Demo gif](http://i.imgur.com/DUI3jr8.gif)



###功能 Features

Eyedow用於快速設計視窗型View.

## 如何使用 Usage
 - Add Repository in build.gradle: 
```
repositories {
    maven { url 'https://dl.bintray.com/ch8154/maven' }
}
```
  

 - Add dependency in buidle.gradle:

```
dependencies {
    compile 'tw.yalan:eyedow:1.0.0'
}
```
- Add permission in AndroidManifest.xml:
```
 <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
  ```

- Setup eyedow service's layout:
  ```
@Layout(id = R.layout.demo_body_1, width = 200, height = 300)
public class EyedowDemoResize extends EyedowService {
	    @Override
	    public void onViewBind(final EyedowContainer eyedowContainer) {
	        View btn1 = eyedowContainer.findViewById(R.id.btn_1);
	        btn1.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Toast.makeText(EyedowDemoResize.this, "hide", Toast.LENGTH_LONG).show();
	                hide();
	            }
	        });
			//......
	    }
}
 ```
 
- Setup eyedow service in AndroidManifest.xml:
   ```
<service android:name=".service.EyedowDemoResize"></service>
<service android:name=".service.EyedowDemoFullScreen"></service>
 ```
- Control Eyedow

   ```
  //Show
 EyedowServiceManager.show(MainActivity.this, EyedowDemoResize.class);
 //Hide
 EyedowServiceManager.get().get(EyedowDemoResize.class).hide();
 //FullScreen
 EyedowServiceManager.get().get(EyedowDemoResize.class).fullScreen();
 //Resize
 EyedowServiceManager.get().get(EyedowDemoResize.class).resizeScreen(x,y,width,height,canDrag);
 //Revert
 EyedowServiceManager.get().get(EyedowDemoResize.class).revert();
 //Close
 EyedowServiceManager.get().get(EyedowDemoResize.class).close();
 ```