//최종 상세페이지
package info.androidhive.loginandregistration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SQLiteHandler2;
import info.androidhive.loginandregistration.manager.ParentActivity;

public class Product extends ParentActivity {

    // LogCat tag
    private static final String TAG = Product.class.getSimpleName();
    private SQLiteHandler2 db;  //sqllite db 핸들러
    private SQLiteHandler db2;//사용자이름
    private ProgressDialog pDialog;

    //추가
    private String brand, product, id;  //인텐트 받아올 때 사용하는 변수
    private ImageView productImg, chart1, chart2, nongdo, rating;
    private TextView brandName, productName, rates1, sum1, typeName, top, mid, base, rates2, sum2;
    private RatingBar ratingBar1;
    private Button b1, b2;
    private String encode;  //가격정보
    private String[] types;
    private String[] colors  = new String[3];
    private ListView lv;

    private String imgurl, bname, pname, type, c1, c2, c3, depth, t, m, b, age, gender;
    private ArrayList<PRItem> listdata = new ArrayList<>();
    private PRItem pitem;
    private Bitmap bitmap;
    private GridView gview;
    //private byte[] pimgbyte = null;

    private ListView reviewlist;
    private ReviewListViewAdapter rlad = null;
    private ArrayList<ReviewItem> rlistdata = new ArrayList<ReviewItem>();
    private int count_people;//리뷰 작성한 사람 수
    private float rating_aver;//평점
    private float sum; //평점 평균

    View header;

    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler2(getApplicationContext());   //디비 핸들러
        db2 = new SQLiteHandler(getApplicationContext());//사용자 정보
        HashMap<String, String> user = db2.getUserDetails();
        userid = user.get("uid");

        Intent rcv = getIntent();
        product = rcv.getStringExtra("product");

        //리스트뷰 헤더 생성

        downProduct(product);
        downReview(product);

        setLayout();


        //위시리스트 추가
        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.checkWish(pname) == false) {
                    Toast.makeText(Product.this, "이미 위시리스트에 존재합니다", Toast.LENGTH_SHORT).show();
                }
                else if(db.checkWish(pname) == true){
                    //서버.add(사용자아이디, 제품 이미지, 브랜드명, 제품명) //서버에 추가
                    insertWishToServer();
                    db.addWish(bitmap, bname, pname);   //제품이미지, 브랜드명, 제품명 sqlite에 추가
                    Toast.makeText(Product.this, "위시리스트에 추가되었습니다!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //가격 정보
        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    encode = URLEncoder.encode(product, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.parse("http://shopping.naver.com/search/all_search.nhn?query=" + encode);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        actionBar();


        reviewlist.setAdapter(rlad);


        //리뷰 작성
        rating.setOnClickListener(new OnClickListener() {//fake별을 누르면
            @Override
            public void onClick(View v) {

                Intent reviewintent = new Intent(Product.this, WriteReview.class);
                reviewintent.putExtra("brandname", pitem.getBrandName());   //상세페이지의 브랜드명, 제품명, 이미지를 넘긴다
                reviewintent.putExtra("productname", pitem.getProductName());
                //reviewintent.putExtra("productimg", pimgbyte);
                reviewintent.putExtra("productimg", bitmap);
                startActivityForResult(reviewintent, 1);
            }
        });


        nongdo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                switch (depth){
                    case "d1" : vibrator.vibrate(1000);   break;
                    case "d2" : vibrator.vibrate(2000);   break;
                    case "d3" : vibrator.vibrate(3000);   break;
                }


            }
        });

    }

    //xml 아이템 연결
    private void setLayout(){

        header = getLayoutInflater().inflate(R.layout.product_header,null,false);
        reviewlist = (ListView) findViewById(R.id.reviewList);
        reviewlist.addHeaderView(header);

        productImg = (ImageView)findViewById(R.id.pImage);
        brandName = (TextView)findViewById(R.id.brandName);
        productName = (TextView)findViewById(R.id.productName);
        ratingBar1 = (RatingBar)findViewById(R.id.ratingBar1);
        rates1 = (TextView)findViewById(R.id.rates);
        sum1 = (TextView)findViewById(R.id.sum);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);

        //여기는 header에 있는 거
        chart1 = (ImageView) header.findViewById(R.id.chart);
        chart2 = (ImageView) header.findViewById(R.id.notes);
        nongdo = (ImageView) header.findViewById(R.id.depth);
        gview = (GridView) header.findViewById(R.id.gview);
        typeName = (TextView)  header.findViewById(R.id.typeName);
        top = (TextView)  header.findViewById(R.id.top);
        mid = (TextView)  header.findViewById(R.id.mid);
        base = (TextView)  header.findViewById(R.id.base);

        //lv = (ListView)findViewById(R.id.reviewList);
        //sv = (ScrollView)findViewById(R.id.scrollView1);
        rating = (ImageView) header.findViewById(R.id.rating);
    }


    //이미지 서버주소로 비트맵 이미지 세팅하기
    private class getImg extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try{
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            productImg.setImageBitmap(bitmap);
        }
    }


    //서버에서 가져온 정보들 세팅하기
    private void setting(){
        brandName.setText(pitem.getBrandName()); //브랜드명
        productName.setText(pitem.getProductName()); //제품명
        ColorChart(); //대표 색상

        String filename = pitem.getDepth();
        int depthimgsrc = getResources().getIdentifier("@drawable/"+filename, "drawable", getPackageName());
        nongdo.setImageResource(depthimgsrc); //농도

        typeSplit(); //계열, 계열 이미지

        top.setText(pitem.getNotes()[0]);
        mid.setText(pitem.getNotes()[1]);
        base.setText(pitem.getNotes()[2]);

    }


    private void ColorChart(){
        colors = pitem.getColors();
        Bitmap back = BitmapFactory.decodeResource(getResources(), R.drawable.m); //비트맵
        Bitmap pallet = Bitmap.createBitmap(back.getWidth(), back.getHeight(), Bitmap.Config.ARGB_8888); //작업용 비트맵
        Canvas canvas = new Canvas(pallet); //비트맵에서 캔버스를 얻는다
        canvas.drawBitmap(back, 0, 0, null); //비트맵 위에 소스 이미지를 그린다

        int x = pallet.getWidth()/4;
        int y = pallet.getHeight()/2 + 80; //이미지뷰 중앙 좌표

        Paint p1 = new Paint();
        p1.setAntiAlias(true); //도형을 그릴 페인트 객체

        p1.setColor(Color.BLACK);
        p1.setStyle(Paint.Style.STROKE);
        p1.setStrokeWidth(3);
        canvas.drawCircle(x-300, y, 120, p1);//검정 외곽선
        p1.setColor(Color.parseColor(colors[0]));
        p1.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x - 300, y, 120, p1);//색 채우기

        p1.setColor(Color.BLACK);
        p1.setStyle(Paint.Style.STROKE);
        p1.setStrokeWidth(3);
        canvas.drawCircle(x, y, 120, p1);//검정 외곽선
        p1.setColor(Color.parseColor(colors[1]));
        p1.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, 120, p1);//색 채우기

        p1.setColor(Color.BLACK);
        p1.setStyle(Paint.Style.STROKE);
        p1.setStrokeWidth(3);
        canvas.drawCircle(x+300, y, 120, p1);//검정 외곽선
        p1.setColor(Color.parseColor(colors[2]));
        p1.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x+300, y, 120, p1);//색 채우기

        chart1.setImageBitmap(pallet);
    }


    private void typeSplit(){
        String type = pitem.getType();
        types = type.split("/");

        for(int i=0; i<types.length; i++){
            typeName.append(types[i]+" ");
        }
        typeName.append("계열");

        gview.setNumColumns(types.length);
        gview.setAdapter(new ImageAdapter(this));

    }


    private class ImageAdapter extends BaseAdapter {
        Context mContext;

        public ImageAdapter(Context context){
            this.mContext = context;
        }

        @Override
        public int getCount() { return types.length; }

        @Override
        public Object getItem(int position) { return null; }

        @Override
        public long getItemId(int position) { return 0; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));   //이미지뷰 크기 지정
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY); //이미지가 크더라도 고정
            //imageView.setImageResource(imgs[position]);

            StringBuffer b = new StringBuffer();
            b.append("@drawable/");
            b.append(types[position]);
            imageView.setImageResource(getResources().getIdentifier(b.toString(), "drawable", getPackageName()));

            return imageView;
        }
    }




    //------------------------------여기서부터 리뷰리스트------------------------------


    //리뷰용 뷰홀더
    private class ViewHolder{
        public ImageView userImage;
        public TextView userNic;
        public RatingBar userRbar;
        public TextView userReview;
    }

    //해당 제품에 해당하는 리뷰 가지고 오기 //parameter로 제품명을 던져줌
    private void downReview(final String thatReview) {
        // Tag used to cancel the request
        String tag_string_req = "req_thatReview";
        //listdata = new ArrayList<SearchResultItem>();

        //pDialog.setMessage("로딩중...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL3, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "thatReview Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //result에 해당하는 정보를 가지고 온다
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

                            String a = jo.getString("profile"); //사용자 이미지
                            String b = jo.getString("name");    //사용자 닉네임
                            String c = jo.getString("rating");  //평점값
                            String d = jo.getString("contents");//리뷰 내용

                            count_people = ja.length(); //도는 수만큼 사람수
                            rating_aver += Float.parseFloat(c); //평점 다 더하고

                            changeReviews(a, b, c, d);  //이미지, 평점값 변환
                            //item = new SearchResultItem(a, b, c);
                            //listdata.add(item);
                        }


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

                //HashMap<Integer, Object> imgs = new HashMap<Integer, Object>();
                //lad = new ListViewAdapter(Search.this, R.layout.search_result_item, listdata, imgs);
                //searchPost(lad);
                if((count_people == 0)&&(rating_aver == 0.0)){
                    rates1.setText("0.0");
                //평점 평균
                }else{
                    sum = rating_aver / count_people;
                    float d =(float)(Math.ceil(sum));//소수점 둘쨋자리에서 반올림
                    ratingBar1.setRating(d);//평점 별에 set
                    String average = Float.toString(d);//문자로 set
                    rates1.setText(average);
                }
                String people = Integer.toString(count_people);
                sum1.setText("("+ people + "명" + ")");

                reviewPost();//리뷰 리스트에 붙이기

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "thatreview");
                params.put("productname",thatReview);//상단에서 매개변수로 가져온 거를 params로 날렸네
               /* String email1 = params.put("email", email);
                params.put("password", password);*/

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    private void changeReviews(String a, String b, String c, String d){
        StringBuffer bf = new StringBuffer();
        bf.append("@mipmap/");
        bf.append(a);
        int imgresource = getResources().getIdentifier(bf.toString(), "drawable", getPackageName());
        Bitmap img = BitmapFactory.decodeResource(getResources(), imgresource);

        float rating = Float.parseFloat(c);

        ReviewItem item = new ReviewItem(img, b, rating, d);
        rlistdata.add(item);
    }   //문자열 이미지와 평점을 변환해 배열리스트에 더한다.


    private void reviewPost(){
        rlad = new ReviewListViewAdapter(Product.this, rlistdata);
        reviewlist.setAdapter(rlad);
    }


    //리뷰리스트 어댑터 생성
    private class ReviewListViewAdapter extends BaseAdapter{
        private  Context mContext = null;
        private LayoutInflater inflater;
        private ArrayList<ReviewItem> reviews = new ArrayList<>();

        public ReviewListViewAdapter(Context mContext, ArrayList<ReviewItem> arr){
            super();
            this.mContext = mContext;
            this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.reviews = arr;
        }

        @Override
        public int getCount() { return reviews.size(); }
        @Override
        public Object getItem(int position) { return reviews.get(position); }
        @Override
        public long getItemId(int position) { return position; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.product_review_item, null);
                holder.userImage = (ImageView) convertView.findViewById(R.id.profile);
                holder.userNic = (TextView) convertView.findViewById(R.id.userName);
                holder.userRbar = (RatingBar) convertView.findViewById(R.id.ratingBar3);
                holder.userReview = (TextView) convertView.findViewById(R.id.review);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag(); }

            ReviewItem reviewItem = (ReviewItem) getItem(position);

            holder.userImage.setImageBitmap(reviewItem.getUserimg());
            holder.userNic.setText(reviewItem.getUsernic());
            holder.userRbar.setRating(reviewItem.getRating());
            holder.userReview.setText(reviewItem.getReview());

            return convertView;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //리스트어댑터뷰 notify
               downReview(product);//리뷰검색 다시
            }
        }
    }


    //------------------------------여기까지 리뷰리스트------------------------------


//------------------------------------원하는 제품만 가지고 오게
    /**
     * function to verify product details in mysql db
     * */
    private void downProduct(final String productname) {
        // Tag used to cancel the request
        String tag_string_req = "req_detail";

        pDialog.setMessage("로딩중...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "detail Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject product = jObj.getJSONObject("wish");//user 에 해당하는 JSONObject정보를 가져옴
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        //jObj와 jObj.user로 부터 정보를 읽어옴
                    /*    String imgUrl = product.getString("imgUrl");
                        String brandName = product.getString("brandName");
                        String productName = product.getString("productName");*/

                        imgurl = product.getString("imgUrl");
                        bname = product.getString("brandName");
                        pname = product.getString("productName");
                        type = product.getString("classify");
                        c1 = product.getString("color1");
                        c2 = product.getString("color2");
                        c3 = product.getString("color3");
                        depth = product.getString("depth");
                        t = product.getString("top");
                        m = product.getString("middle");
                        b = product.getString("base");
                        age = product.getString("age");
                        gender = product.getString("gender");
                        pitem = new PRItem(imgurl, bname, pname, c1, c2, c3, depth, type, t, m, b);

                        onPost();

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
                Log.e(TAG, "Detail Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "wish");
                params.put("productname",productname);//상단에서 매개변수로 가져온 거를 params로 날렸네
               /* String email1 = params.put("email", email);
                params.put("password", password);*/

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

    public void onPost() {
        String img = pitem.getProductImgStr();
        new getImg().execute(img);
        setting();
    }
//-------------------------------------------------------------------------------------------------------------DB서버에 위시리스트 추가
    private void insertWishToServer() {
        // Tag used to cancel the request
        String tag_string_req = "req_wish";
        //pDialog.setMessage("등록 중...");
        //showDialog();sqlite에 저장할때 toast띄어주므로 생략

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_WISH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Wish Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //추가로 가져올 정보가 있다면 여기에 작성

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Wish Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(),
                        //error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "wish");
                params.put("uid", userid);//전역변수라 이렇게 params 날리겠슴다
                params.put("imgurl",imgurl);
                params.put("productname", pname);
                params.put("brandname",bname);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }//여기까지

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

    private void actionBar(){
        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_detail, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                )
        );
        //홈버튼은 안보이고 뒤로가기 안보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }
    }

}