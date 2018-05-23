package info.androidhive.loginandregistration;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import info.androidhive.loginandregistration.manager.ParentActivity;

public class LoginIntro extends ParentActivity {
    private ImageView imView1;
    private TextView txtView1;
    private ImageView facebook, google;

    public Context mContext = null;
    private BackPressCloseSystem backPressCloseSystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_intro);
        //액션바 숨기기
        getSupportActionBar().hide();

        //이메일로 가입하기 액티비티로
        mContext = this;

        //뒤로가기 버튼 이벤트
        backPressCloseSystem = new BackPressCloseSystem(this);

        imView1 = (ImageView)findViewById(R.id.btn_email);
        imView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EmailJoin.class);
                startActivity(intent);
            }
        });
        //로그인 버튼을 누르면 로그인 할 수있는 액티비티로
        txtView1 = (TextView)findViewById(R.id.btn_login);
        txtView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(mContext, Login2.class);
                startActivity(intent2);

            }
        });


       facebook = (ImageView)findViewById(R.id.btn_facebook);
        google = (ImageView)findViewById(R.id.btn_google);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast toast = Toast.makeText(getApplicationContext(), "준비중",Toast.LENGTH_SHORT);
                toast.show();

            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast toast2 = Toast.makeText(getApplicationContext(), "준비중",Toast.LENGTH_SHORT);
                toast2.show();

            }
        });

    }//onCreate();

    @Override
    public void onBackPressed(){
        backPressCloseSystem.onBackPressed();
    }



}
