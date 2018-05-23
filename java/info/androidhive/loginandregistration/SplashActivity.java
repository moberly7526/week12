package info.androidhive.loginandregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import info.androidhive.loginandregistration.helper.SessionManager;

public class SplashActivity extends Activity {

    private SessionManager session;//스플레시 화면을 띄우면서, 현재 사용자가 로그인이 되어있는지 안되어있는지 확인하기 위함
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.splash);
        try
        {
            //1.5초간 대기
            Thread.sleep(1500);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        // 세션 매니저 객체 생성
       session = new SessionManager(getApplicationContext());

        // 로그인 유무 확인
        if (session.isLoggedIn()) { //로그인이 되어있으면
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);//로그인이 된 상태라면 메인액티비티로
            startActivity(intent);
            finish();
        }else{//로그인이 되어있지 않으면
            //로그인 인트로 액티비티로 넘어간다
            startActivity(new Intent(this,LoginIntro.class));
            finish();
        }//if else문
    }//onCreate();

}
