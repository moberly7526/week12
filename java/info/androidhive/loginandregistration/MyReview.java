//내가 쓴 리뷰
package info.androidhive.loginandregistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.manager.ParentActivity;


public class MyReview extends ParentActivity implements OnClickListener{

    //설정 버튼 변수
    private ImageView setting_intent;
    private TextView wish_intent3;
    private TextView recent_intent3;
    private TextView nickname;

    private SQLiteHandler db;//디비 핸들러
    private String uid;

    private static final String TAG = MyReview.class.getSimpleName();   // LogCat tag
    private ProgressDialog pDialog;//다이얼로그

    CheckProfile pro;
    private String name;
    private int userimg;
    private ImageView profileImg;

    private ListView listView;
    TextView reviewcount;
    private ArrayList<MyReviewItem> listdata = new ArrayList<>();
    private ListViewAdapter lad = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        listView = (ListView) findViewById(R.id.mr_list_view);
        reviewcount = (TextView)findViewById(R.id.reviewcount);

        pagechange();

        //다이얼로그
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        profile();

        actionbar();

        downMyReview();


        reviewcount.setText(listdata.size()+"개");


    }   //onCreate();


    private class ViewHolder{
        ImageView productImage, btn_delete, profileImage, line;  //제품 이미지,삭제버튼,프로필이미지,화살표,선
        TextView brandName, perfumeName, userName, review;//브랜드명,향수이름,사용자,별점,리뷰
        RatingBar ratingBar;
    }


    private class ListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Context mContext = null;
        private ArrayList<MyReviewItem> listdata2;

        public ListViewAdapter(Context context, ArrayList<MyReviewItem> arr){
            super();
            this.mContext = context;
            this.listdata2 = arr;
            this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() { return listdata2.size(); }

        @Override
        public Object getItem(int i) { return listdata2.get(i); }

        @Override
        public long getItemId(int i) { return i; }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item_sample2, null);
                vh.productImage = (ImageView) convertView.findViewById(R.id.productImage);
                vh.brandName = (TextView)convertView.findViewById(R.id.brandName);
                vh.perfumeName = (TextView)convertView.findViewById(R.id.perfumeName);
                vh.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
                vh.userName = (TextView)convertView.findViewById(R.id.userName );
                vh.ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);
                vh.review = (TextView)convertView.findViewById(R.id.review);
                vh.btn_delete = (ImageView) convertView.findViewById(R.id.btn_delete);
                vh.line = (ImageView) convertView.findViewById(R.id.line);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            MyReviewItem item = (MyReviewItem) listdata2.get(i);
            ImageLoader.getInstance().displayImage(item.getProductimg(), vh.productImage);
            vh.brandName.setText(item.getBrandname());
            vh.perfumeName.setText(item.getProductname());
            vh.profileImage.setImageResource(userimg);
            vh.userName.setText(name);
            vh.ratingBar.setRating(item.getRating());
            vh.review.setText(item.getContents());

            return convertView;
        }
    }



    //------------------------------------------------------------------------내가 쓴 리뷰 가지고 오기
    //넘길껀 uid
    //가지고 올껀 사용자테이블의 사용자이미지, 닉네임 /리뷰테이블의 제품이름, 평점, 리뷰내용(uid & 제품이름으로 검색) /제품테이블의 제품사진, 브랜드이름(제품이름으로 검색)
    private void downMyReview() {
        // Tag used to cancel the request
        String tag_string_req = "req_mywish";
        //listdata = new ArrayList<SearchResultItem>();

        //pDialog.setMessage("로딩중...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_MYREVIEW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "myreview Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray ja = jObj.getJSONArray("results");
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            //리뷰리스트뷰 라인 상단부분
                            String a = jo.getString("imgUrl");
                            String b = jo.getString("brandName");
                            String c = jo.getString("productName");
                            //리뷰리스트뷰 라인 하단부분
                            String d = jo.getString("profile");
                            String e = jo.getString("name");
                            float f =(float)(jo.getDouble("rating"));
                            String g = jo.getString("contetns");

                            MyReviewItem item = new MyReviewItem(a, b, c, f, g);
                            listdata.add(item);
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "리뷰를 작성해보세요!", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

                lad = new ListViewAdapter(MyReview.this, listdata);
                listView.setAdapter(lad);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "myreview Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "myreview");
                params.put("uid",uid);//uid날림
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    //화면 넘김 위함
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_img3:
                // TODO Auto-generated method stub
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.my_wish3:
                // TODO Auto-generated method stub
                Intent intent2 = new Intent(this, MyWish.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.my_recent3:
                // TODO Auto-generated method stub
                Intent intent3 = new Intent(this, MyRecent.class);
                startActivity(intent3);
                finish();
                break;
            default:
                break;
        }
    }//onClick


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void pagechange(){
        // 눌렀을때 페이지 전환되는 거 관련
        setting_intent = (ImageView)findViewById(R.id.setting_img3);
        setting_intent.setOnClickListener(this);

        wish_intent3 = (TextView)findViewById(R.id.my_wish3);
        wish_intent3.setOnClickListener(this);

        recent_intent3 = (TextView)findViewById(R.id.my_recent3);
        recent_intent3.setOnClickListener(this);
        //여기까지
    }

    private void profile(){
        nickname = (TextView)findViewById(R.id.nickname3);

        db = new SQLiteHandler(getApplicationContext());
        // sqllite에서 사용자 정보 가지고 오기
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        uid = user.get("uid");
        // Displaying the user details on the screen
        nickname.setText(name);//sqllite db에서 가져온 name 내용으로 set

        //프로필 연결하기
        profileImg = (ImageView)findViewById(R.id.profile_img);

        //꽃이름 사진에 해당하는 id 가져오기
        String flowerName = user.get("profile");
        pro = new CheckProfile();
        userimg = pro.isYourFlower(flowerName);
        profileImg.setImageResource(userimg);
    }

    private void actionbar(){
        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_mypage, null),
                new android.support.v7.app.ActionBar.LayoutParams(
                        android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
