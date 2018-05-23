package info.androidhive.loginandregistration;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import info.androidhive.loginandregistration.manager.ActivityManager;
import info.androidhive.loginandregistration.manager.ParentActivity;

//뒤로가기 버튼을 두번 누르면 앱종료
public class BackPressCloseSystem extends ParentActivity {

    private  long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    //private Context context;
    private ActivityManager am = ActivityManager.getInstance();

    //생성자
    public BackPressCloseSystem(Activity activity){
        this.activity = activity;
        //this.context=context;
    }

    public void onBackPressed() {

        if (isAfter2Seconds()) {
            backKeyPressedTime = System.currentTimeMillis();
            //현재시간을 다시 초기화

            toast = Toast.makeText(activity, "\'뒤로가기\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }
        if (isBefore2Seconds()) {
            programShutdown();
            toast.cancel();
        }
    }//BackPressed();
        private Boolean isAfter2Seconds(){ //2초가 지났을 경우
            return System.currentTimeMillis() > backKeyPressedTime + 2000;
        }

        private  Boolean isBefore2Seconds(){//2초가 지나지 않았을 경우
            return System.currentTimeMillis() <= backKeyPressedTime + 2000;

        }

        private  void programShutdown(){
           // ActivityManager am = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
            //am.killBackgroundProcesses(context.getPackageName());

            am.finishAllActivity();
            activity .moveTaskToBack(true);
            activity .finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);


        }
    }



