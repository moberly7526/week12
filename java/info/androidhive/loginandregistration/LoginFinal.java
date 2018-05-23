package info.androidhive.loginandregistration;

import com.android.volley.Request;
import  info.androidhive.loginandregistration.app.AppConfig;
import  info.androidhive.loginandregistration.app.AppController;
import  info.androidhive.loginandregistration.helper.SQLiteHandler;
import  info.androidhive.loginandregistration.helper.SessionManager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import info.androidhive.loginandregistration.manager.ActivityManager;
import info.androidhive.loginandregistration.manager.ParentActivity;


public class LoginFinal extends ParentActivity{//이부분 에러

    private static final String TAG = LoginFinal.class.getSimpleName();
    private String str1;//이메일
    private String str2;//비밀번호
    private String str3;//꽃이름
    private String str4;//닉네임
    private String nickname; //꽃이름 + 닉네임
    public Context mContext= null;

    private ActivityManager am = ActivityManager.getInstance();

    //꽃이름이랑 닉네임이 합쳐져서 하나의 name

    private int num = 0;//꽃 위치

    private ProgressDialog pDialog;//진행다이얼로그
    private SessionManager session;
    private SQLiteHandler db;

    private ImageView imgStart;//시작하기 버튼//등록하기 버튼

    int img[] ={
            R.mipmap.default_flower,
            R.mipmap.rose,
            R.mipmap.lily,
            R.mipmap.babybreath,
            R.mipmap.narcissus,
            R.mipmap.daisy,
            R.mipmap.sunflower
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_final);
        mContext = this;

        Intent intent = getIntent();

        str1 = intent.getExtras().getString("Email");
        str2 = intent.getExtras().getString("Password");
        str3 = intent.getExtras().getString("FlowerName");
        str4 = intent.getExtras().getString("Title");
        num = intent.getExtras().getInt("FlowerNum");
        //가지온 값을 해당 xml id정보와 일치하는 부분에 출력하기
        /* 값이 잘 가져와지는지 확인하기 위함
        TextView txt1 = (TextView)findViewById(R.id.txt1);
        TextView txt2 = (TextView)findViewById(R.id.txt2);
        TextView txt3 = (TextView)findViewById(R.id.txt3);
        TextView txt4 = (TextView)findViewById(R.id.txt4);
         */
        //실제론 닉네임만 가져와도 됨 xml부분도 삭제하겠음
        TextView txt5 = (TextView)findViewById(R.id.txt_nickname);
        nickname =str4 +" "+str3;

        ImageView image = (ImageView)findViewById(R.id.flower_img2);
        image.setImageResource(img[num]);
        /*
        txt1.append(str1);
        txt2.append(str2);
        txt3.append(str3);
        txt4.append(str4);
         */
        txt5.setText(nickname);

        // 프로그래스 다이얼 로그
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // 세션 매니저
        session = new SessionManager(getApplicationContext());

        // SQLite 디비 핸들러
        db = new SQLiteHandler(getApplicationContext());


        /*
        // 사용자가 이미 로그인 했는지 안했는지를 확인
        if (session.isLoggedIn()) {
            // 이미 로그인되어 있으면, 메인화면으로 보내기
            Intent intent = new Intent(LoginFinal.this,
                   MainActivity.class);
            startActivity(intent);
            finish();
        }
        */

        //'시작하기 버튼'을 눌렀을때
        imgStart = (ImageView)findViewById(R.id.btn_start);
        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nickname;
                String email = str1;
                String password = str2;
                String english = changeEnglish(str3);
                //String profile = str3;

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()&& !english.isEmpty()) {
                    registerUser(name, email, password,english);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "이메일 혹은 비밀번호를 확인해주세요!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        //액션바 부분
        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_login_final, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }//onCreate();

    public String changeEnglish(String flowerName){

        String eng = null;
        switch(flowerName){

            case "장미":
                eng = "rose";
                break;
            case "백합":
                eng = "lily";
                break;
            case "안개꽃":
                eng = "babybreath";
                break;
            case "수선화":
                eng = "narcissus";
                break;
            case "데이지":
                eng = "daisy";
                break;
            case "해바라기":
                eng = "sunflower";
                break;
        }
        return eng;
    }

    /**
     * MySQL디비에 사용자 정보를 넣는 함수 will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password, final String profile) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("사용자 등록 중...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        String profile = user.getString("profile");//꽃이름 추가

                        //사용자 테이블에 행 삽입


                        // 가입하기 끝났으므로 로그인 페이지로
                        Intent intent = new Intent(
                                LoginFinal.this,
                                Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //finish();
                        am.finishAllActivity();	// 모든 액티비티 종료.
                    } else {

                        // 가입 중 문제가 생기면 오류 찾아내기
                        // 메시지
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("profile", profile);

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


    //액션바 뒤로가기 구현
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
