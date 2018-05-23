package info.androidhive.loginandregistration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by 삼성찡 on 2015-08-23.
 */
public class InquireActivity extends ActionBarActivity {

    private EditText t;
    private EditText c;

    private Button inquire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);

        t = (EditText)findViewById(R.id.title);
        c = (EditText)findViewById(R.id.mail);
        inquire = (Button)findViewById(R.id.inquire);

        inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        //홈버튼은 안보이고 뒤로가기 보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }//onCreate();




    private void sendEmail() {


                String[] to = {"star9948@naver.com"};   //개발자 이메일 주소
                String title = t.getText().toString();  //제목
                String contents = c.getText().toString();   //내용

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, to);
                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_TEXT, contents);
                intent.setType("message/rfc822");   //메일타입
                startActivity(Intent.createChooser(intent, "이메일을 선택해주세요"));

            }

    } //찾는 향수가 없다면

