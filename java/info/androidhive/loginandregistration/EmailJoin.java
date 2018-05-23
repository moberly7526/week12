package info.androidhive.loginandregistration;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.androidhive.loginandregistration.manager.ParentActivity;

public class EmailJoin extends ParentActivity{

    ImageView imView2;
    public Context mContext2 = null;
    //비밀번호 유효성 검사 영문+숫자조합 6-16자리 사이
    private static final String Passwrod_PATTERN = "(?=\\w{6,16}$)(?=.*[0-9])(?=.*[a-zA-Z]).*";//특수문자가 오면 처리가 안되넹
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_join);

        mContext2 = this;

        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_email_join, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //이메일 & 비밀번호
        final EditText text1;
        final EditText text2;
        text1 = (EditText)findViewById(R.id.txt_email);
        text2 = (EditText)findViewById(R.id.txt_password);


        //'다음' 버튼
        imView2 = (ImageView)findViewById(R.id.btn_next1);

        imView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st =  text1.getText().toString();
                String st2 = text2.getText().toString();

                if(isEmailPattern(st)==false){
                    Toast.makeText(getApplicationContext(), "이메일을 확인해주세요", Toast.LENGTH_SHORT).show();
                }else if(Passwrodvalidate(st2)==false){
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(mContext2, MakeNickName.class);
                    intent.putExtra("Email", st);
                    intent.putExtra("Password", st2);
                    startActivity(intent);//'다음'버튼을 누르면 페이지 전환과 동시에 putExtra로 데이터 저장
                }
            }
        });
    }//onCreate();
    //이메일 유효성
    public static boolean isEmailPattern(String email){
        Pattern pattern= Pattern.compile("\\w+[@]\\w+\\.\\w+");
        Matcher match=pattern.matcher(email);
        return match.find();
    }
    //비밀번호 유효성 검사
    public boolean Passwrodvalidate(final String hex) {
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);//누르게되면 아예 첫화면으로 가게되버림
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

}
