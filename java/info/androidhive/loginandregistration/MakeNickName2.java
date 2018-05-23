package info.androidhive.loginandregistration;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.manager.ParentActivity;

public class MakeNickName2 extends ParentActivity implements OnClickListener {

    private String str1;//이메일
    private String str2;//비밀번호
    private String str3;//꽃이름
    int num = 0;//꽃 위치
    ///뒤로가기 버튼 이벤트

    private static final String TAG = MakeNickName2.class.getSimpleName();
    private ProgressDialog pDialog;//다이얼로그


    private EditText title;//닉네임 입력
    //1자 이상 5자 이하의 형용사형 타입의 타이틀
    private String string = null;
    private static final String title_PATTERN = "(?=\\w{1,5}$).*";

    //ImageView img1;//랜덤 버튼
    private ImageView img2;//중복확인 버튼
    private ImageView img3;//다음버튼
    private Boolean check = false;//닉네임 확인

    private String nick = null;


    public Context mContext4 = null;

    //사진을 담을 배열을 만듦
    int image[]={
            R.mipmap.random2,
            R.mipmap.sucess
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_nickname2);

        mContext4 = this;
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        setLayout();
        //getData();

        Intent intent = getIntent();

        str1 = intent.getExtras().getString("Email");
        str2 = intent.getExtras().getString("Password");
        str3 = intent.getExtras().getString("FlowerName");
        num = intent.getExtras().getInt("FlowerNum");


        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_make_nickname, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//액션바 끝

        //버튼 클릭시 색상 변경
        //중복확인 버튼의 경우 나중에 중복확인이 되면 -> 색상변경으로

    }//onCreate()


    @Override
    public void onClick(View v){//오 원하는 데로 작동 잘됨
        switch(v.getId()){
            case R.id.btn_confirm:
                //쿼리를 날려서 해당 닉네임이 있으면, toast날리고 bool변수를 false, 사용할 수있으면 true
                //아니면 색 변경
                string = title.getText().toString();
                if(Titlevalidate(string)==true){
                nick = string + " " +str3;
                    //Toast.makeText(this,nick,Toast.LENGTH_SHORT).show();
                checkNickname(nick);
                }else{
                    Toast.makeText(this,"타이틀을 지정해주세요",Toast.LENGTH_SHORT).show();
                 }
                //img2.setImageResource(image[1]);
                break;
            case R.id.btn_next3:
                string = title.getText().toString();
                if( (Titlevalidate(string)==true) && (check == true)) {//타이틀을 입력했으면//중복확인도 받아 성공했다면
                    Intent intent = new Intent(mContext4, LoginFinal.class);
                    intent.putExtra("Email", str1);
                    intent.putExtra("Password", str2);
                    intent.putExtra("FlowerName", str3);
                    intent.putExtra("Title", title.getText().toString());
                    intent.putExtra("FlowerNum",num);
                    startActivity(intent);
                    break;
                }else{
                    Toast.makeText(this,"타이틀을 확인해주세요",Toast.LENGTH_SHORT).show();
                    break;
                }
            default:
                break;
        }
    }


    private void checkNickname(final String title2) {
        // Tag used to cancel the request
        String tag_string_req = "req_checkNickname";

        pDialog.setMessage("확인 중..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig. URL_CHECK_NAME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "nickname Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (error == false) {
                        check = true;
                        img2.setImageResource(image[1]);

                    } else {
                        //String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "존재하는 닉네임 입니다", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "checkNickname Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "nickname");
                params.put("title", title2);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void setLayout(){
       //onCreate내에서 처리해야하나봄. 함수로 따로 빼서 처리 후 onClick()
        //img1 = (ImageView)findViewById(R.id.btn_random);
        img2 = (ImageView)findViewById(R.id.btn_confirm);
        img3 = (ImageView)findViewById(R.id.btn_next3);

        title = (EditText)findViewById(R.id.txt_title);


        //img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
    }
/*
    private void getData(){
        //EmailJoin으로부터 값 가지고 오기
        Intent intent = getIntent();

        str1 = intent.getExtras().getString("Email");
        str2 = intent.getExtras().getString("Password");
        str3 = intent.getExtras().getString("FlowerName");
        num = intent.getExtras().getInt("FlowerNum");

        //가지온 값을 해당 xml id정보와 일치하는 부분에 출력하기
        *//*임의 부분이라 삭제, xml부분도 삭제
        TextView txt1 = (TextView)findViewById(R.id.txt1);
        TextView txt2 = (TextView)findViewById(R.id.txt2);
        TextView txt3 = (TextView)findViewById(R.id.txt3);

        txt1.append(str1);
        txt2.append(str2);
        txt3.append(str3);
        *//*

    }*/

    //타이틀 유효성 검사
    public boolean Titlevalidate(final String hex) {
        Pattern pattern = Pattern.compile(title_PATTERN);
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
