//가입완료후 로그인페이지 나오는 그 로그인
package info.androidhive.loginandregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

import info.androidhive.loginandregistration.manager.ParentActivity;
public class Login extends ParentActivity {

    // LogCat tag
    private static final String TAG = LoginFinal.class.getSimpleName();
    private TextView tx;//비밀번호를 잊어버리셨나요?
    private ImageView im;//로그인버튼
    private EditText em,pw;//이메일, 비밀번호

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;//sqllite db 핸들러

    private ImageView facebookLogin, googleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //tx = (TextView)findViewById(R.id.txt);//비밀번호 찾기
        //tx.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);//글자에 밑줄긋기 코드

        //이메일, 비밀번호를 위한 EditText랑 로그인 버튼
        em = (EditText)findViewById(R.id.txt_email2);
        pw = (EditText)findViewById(R.id.txt_password2);
        im = (ImageView)findViewById(R.id.btn_login);

        facebookLogin = (ImageView)findViewById(R.id.btn_facebook_login);
        googleLogin = (ImageView)findViewById(R.id.btn_google_login);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Session manager
        session = new SessionManager(getApplicationContext());//세션 매니저
        db = new SQLiteHandler(getApplicationContext());//디비 핸들러

        //로그인 버튼을 누르면 이메일과 비밀번호를 보내서 확인
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = em.getText().toString();//이메일 에디트 텍스트의 String가져오기
                String password = pw.getText().toString();//비밀번호 에디트 텍스트의 String가져오기

                // 형식에 맞게 잘 썼는지 확인
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // 제대로 안썼다면 Toast메시지
                    Toast.makeText(getApplicationContext(),
                            "이메일 혹은 비밀번호를 확인해주세요", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });//클릭리스너 끝


        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Toast.makeText(getApplicationContext(),"준비중",Toast.LENGTH_SHORT).show();
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"준비중",Toast.LENGTH_SHORT).show();

            }
        });

        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_login, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 안보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }//onCreate();
    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("로딩 중..");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject user = jObj.getJSONObject("user");//user 에 해당하는 JSONObject정보를 가져옴
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        //jObj와 jObj.user로 부터 정보를 읽어옴
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        String uid = jObj.getString("uid");
                        String profile = user.getString("profile");

                        //디비에 저장
                        db.addUser(name, email, uid, created_at, profile);

                        session.setLogin(true);
                        // Launch main activity
                        Intent intent = new Intent(Login.this,
                                MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);

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
}