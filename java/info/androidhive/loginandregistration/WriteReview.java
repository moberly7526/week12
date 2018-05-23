package info.androidhive.loginandregistration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.WriteReviewHandler;

public class WriteReview extends Activity {
    private static final String TAG = WriteReview.class.getSimpleName();
    RatingBar rbar;
    EditText comment;
    TextView register;
    float rating;
    String contents, productname, brandname;
    byte[] pimgbyte;
    Bitmap bbb;
    String s;
    String id;


    private SQLiteHandler db1;//사용자 uid갖고 오기기
    private WriteReviewHandler db2;
    private ProgressDialog pDialog;//진행다이얼로그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int)(display.getWidth()*0.95);
        getWindow().getAttributes().height = (int)(display.getHeight()*0.4);

        rbar = (RatingBar)findViewById(R.id.reviewrating);
        comment = (EditText)findViewById(R.id.rcomm);
        register = (TextView)findViewById(R.id.register);

        comment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});    //입력 글자수 제한

        Intent intent = getIntent();
        brandname = intent.getStringExtra("brandname"); //브랜드명
        productname = intent.getStringExtra("productname"); //제품명
        //pimgbyte = intent.getByteArrayExtra("pimgbyte"); //제품이미지
        bbb =(Bitmap)intent.getParcelableExtra("productimg");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bbb.compress(Bitmap.CompressFormat.PNG, 100,stream);
        pimgbyte = stream.toByteArray();

        db1 = new SQLiteHandler(getApplicationContext());
        // sqllite에서 사용자 정보 가지고 오기
        HashMap<String, String> user = db1.getUserDetails();
        id = user.get("uid");    //사용자의 uid을 sqlite에서 가져온다

        db2 = new WriteReviewHandler(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = rbar.getRating();
                contents = comment.getText().toString();
                s = Float.toString(rating);
                //db2.addReview(brandname, productname, pimgbyte, id, rating, contents);
                //Toast.makeText(getBaseContext(), s , Toast.LENGTH_SHORT).show();
                insertReviewToServer(id, productname, s, contents);    //서버 디비에 저장___제품 상세 페이지의 리뷰 리스트에 뿌리기 위해서
            }
        });

    }//onCreate();

    private void insertReviewToServer(final String uid1, final String productname1,
                              final String rating1, final String contents1) {
        // Tag used to cancel the request
        String tag_string_req = "req_review";

        pDialog.setMessage("등록 중...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Review Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject review = jObj.getJSONObject("review");
                        //String uid =  review.getString("uid");
                        String productName =  review.getString("productName");
                        String rating =  review.getString("rating");
                        String contents =  review.getString("contents");

                        Intent i = new Intent(WriteReview.this, Product.class);

                        setResult(RESULT_OK);
                        finish();


                    } else {


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
                params.put("tag", "review");
                params.put("uid", id);
                params.put("productname", productname);
                params.put("rating", s);
                params.put("contents",contents);

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


    //insertReviewToServer 구현하기
    // 4가지 데이터를 서버의 reviews 테이블에 저장한다


}

/*
★★★서버에 보내 저장해야할 변수들은★★★
1. sqlite에서 가져온 사용자 아이디 uid (int)
2. 제품명 (string)
3. 평점값 (float)
4. 내용 (string)
*/
