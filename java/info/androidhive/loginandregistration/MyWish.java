//위시리스트
package info.androidhive.loginandregistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.manager.ParentActivity;



public class MyWish extends ParentActivity implements OnClickListener,AbsListView.OnItemClickListener,AbsListView.OnScrollListener{

    //private static final String TAG = "StaggeredGridActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";

    // LogCat tag
    private static final String TAG = MyWish.class.getSimpleName();
    private ProgressDialog pDialog;//다이얼로그


    private boolean mHasRequestedMore;//스크롤 내렸을 때 더보기

    private StaggeredGridView mGridView;
    //어댑터 부분
    private SampleAdapter mAdapter;
    private ArrayList<String> mData;

    private ArrayList<String> mListData = new ArrayList<>();//ArrayList

    //설정 버튼 변수
    private ImageView setting_intent;
    private TextView recent_intent1;
    private TextView review_intent1;
    private TextView nickname;
    private ImageView profileImg;

    private SQLiteHandler db;//디비 핸들러
    private String uid;//사용자 아이디

    //꽃이름에 해당하는 사진을 가져오기 위해서
    CheckProfile pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish);

        //설정,탭 버튼 관련
        setting_intent = (ImageView)findViewById(R.id.setting_img1);
        setting_intent.setOnClickListener(this);
        recent_intent1 = (TextView)findViewById(R.id.my_recent1);
        recent_intent1.setOnClickListener(this);

        review_intent1 = (TextView)findViewById(R.id.my_review1);
        review_intent1.setOnClickListener(this);
        //여기까지

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();

        //다이얼로그
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //닉네임 연결하기
        nickname = (TextView)findViewById(R.id.nickname1);
        // sqllite에서 사용자 정보 가지고 오기
        String name = user.get("name");
        uid = user.get("uid");//사용자 uid갖고 오기, 마이위시 서버 DB검색시 필요

        nickname.setText(name);//sqllite db에서 가져온 name 내용으로 set
        //닉네임가지고오기끝

        //프로필 연결하기
        profileImg = (ImageView)findViewById(R.id.profile_img);

        //꽃이름 사진에 해당하는 id 가져오기
        String flowerName = user.get("profile");
        pro = new CheckProfile();
        int i = pro.isYourFlower(flowerName);
        profileImg.setImageResource(i);



        //핀터레스트 리스트 뷰 위한 거
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view1);
        LayoutInflater layoutInflater = getLayoutInflater();

        //상단의 안내문구
        View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.text1);
        txtHeaderTitle.setText("위시리스트");
        TextView txtHeaderTitle2 = (TextView) header.findViewById(R.id.text2);
        txtHeaderTitle2.setText("1개");
        mGridView.addHeaderView(header);
        //안내문구 끝

        //어댑터 부분
        //만약에 SampleAdapter.java로 넘겨서
        //예제에서의 TextView와 같이 해당 위치를 출력하고 싶다던가 할때
        //이런식으로 넘겨서  SampleAdapter.java에서 작업을 해주는거지
        mAdapter = new SampleAdapter(this,android.R.layout.simple_list_item_1, generateData());//일단 아무 작업을 하지않을 것이므로 0을 넘김니다
        //어댑터 끝//simple_list_item_1??이거 뭐지???


        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);

        // do we have saved data?
        if (savedInstanceState != null) {
            mData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
        }

        if (mData  == null) {
            mData = SampleData.generateSampleData();
        }

        for (String data :mData) {
            mAdapter.add(data);

        }


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


    }//onCreate();
    //핀터레스트 리스트 뷰
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DATA_KEY, mData);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        final ArrayList<String> sampleData = SampleData.generateSampleData();
        for (String data : sampleData) {
            mAdapter.add(data);

        }
        // stash all the data in our backing store
        mData.addAll(sampleData);
        // notify the adapter that we can update now
        mAdapter.notifyDataSetChanged();

        mHasRequestedMore = false;
    }

    private ArrayList<String> generateData() {
        ArrayList<String> listData = new ArrayList<String>();
        listData.add("http://i62.tinypic.com/2iitkhx.jpg");
        listData.add("http://i61.tinypic.com/w0omeb.jpg");

        return listData;
    }

    //아이템을 눌렀을 때 일어나는 행위
    //구현할 껀 눌렀을때 해당 제품의 상세페이지로
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }
    //여기까지 핀터레스트 리스트뷰

    //설정 및 탭 구현
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_img1:
                // TODO Auto-generated method stub
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.my_recent1:
                // TODO Auto-generated method stub
                Intent intent2 = new Intent(this, MyRecent.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.my_review1:
                // TODO Auto-generated method stub
                Intent intent3 = new Intent(this, MyReview.class);
                startActivity(intent3);
                finish();
                break;
            default:
                break;
        }
    }//onClick

    //----------------------------------------------------------------------------------마이위시들어오자마자, 서버에서 정보들을 갖고온다
    //onCreate에서 함수로 호출
    //넘길정보는 사용자 uid , 받아오는거는 imgUrl,brandName ,productName
   //  ImageLoader.getInstance().displayImage(getItem(position), vh.productImage); 이 라이브러리 쓰니까 바로 bitmap 변환되던데 응용해봐야지
    private void downMyWish() {
        // Tag used to cancel the request
        String tag_string_req = "req_mywish";
        //listdata = new ArrayList<SearchResultItem>();

        //pDialog.setMessage("로딩중...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_MYWISH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "mywish Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //JSONObject product = jObj.getJSONObject("results");//user 에 해당하는 JSONObject정보를 가져옴
                    JSONArray ja = jObj.getJSONArray("results");
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        //jObj와 jObj.user로 부터 정보를 읽어옴
                    /*    String imgUrl = product.getString("imgUrl");
                        String brandName = product.getString("brandName");
                        String productName = product.getString("productName");*/
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            String a = jo.getString("imgUrl");
                            String b = jo.getString("brandName");
                            String c = jo.getString("productName");

                            //추가로 넣야되는 코드들 여기에 작성
                            //item = new SearchResultItem(a, b, c);
                            //listdata.add(item);
                        }


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "나만의 위시리스트를 만드세요!", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

                //어댑터에 붙이는 코드
                //HashMap<Integer, Object> imgs = new HashMap<Integer, Object>();
                //lad = new ListViewAdapter(Search.this, R.layout.search_result_item, listdata, imgs);
                //searchPost(lad);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "mywish Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "mywish");
                params.put("uid",uid);//uid날림
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

//--------------------------------------------------------------------------------------------------여기까지



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
