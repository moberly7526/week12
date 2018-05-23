package info.androidhive.loginandregistration;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;

import android.text.InputFilter;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler3;

import android.widget.AdapterView.OnItemSelectedListener;

public class Search extends Activity implements OnItemSelectedListener{
    ImageView img1, img2, img3;
    EditText et;
    Button btn1;
    Spinner s1, s2, s3, s4, s5, s6;
    TextView tv;
    int condition;
    SearchResultItem item;
    private ListView lv = null;
    private ListViewAdapter lad = null;
    private ArrayList<SearchResultItem> listdata;

    int count = 0;//리스트뷰 갯수

    // LogCat tag
    private static final String TAG = Search.class.getSimpleName();
    private ProgressDialog pDialog;//다이얼로그

    private SQLiteHandler3 db3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        setLayout(); //뷰 연결 및 처리
        showAllProduct();//일단 리스트에 우리 DB에 있는 모든 정보를 띄어준다


        //스피너 구현하기_
        //s1-s6항목 선택시, 항목의 텍스트값을 질의쿼리로 넣어 select해온다. 가져온 값들을 리스트에 뿌려준다. 검색버튼을 통해 가져온 결과리스트를 수정 정렬한다.
        //s6 항목 선택시, 리스트에 뿌려준 항목들을 조회순 또는 리뷰순 등으로 재정렬해준다.

        //lv에 커스텀리스뷰 적용해서 값들 뿌려줄 수 있도록 구현한다.

        //new phpDown().execute("http://swu2015.cafe24.com/search2.php");
        //phpDown 명령어를 언제 어디서 던져?

        db3 = new SQLiteHandler3(getApplicationContext());


    }   //onCreate()


    private class ViewHolder {
        public ImageView imv;
        public TextView txt1;
        public TextView txt2;
    }   //viewholder


    private void showAllProduct() {
        // Tag used to cancel the request
        String tag_string_req = "req_search";
        listdata = new ArrayList<SearchResultItem>();

        pDialog.setMessage("로딩중...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_SEACH_ALL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "detail Response: " + response.toString());
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

                            item = new SearchResultItem(a, b, c);
                            listdata.add(item);
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

                HashMap<Integer, Object> imgs = new HashMap<Integer, Object>();
                lad = new ListViewAdapter(Search.this, R.layout.search_result_item, listdata, imgs);
                searchPost(lad);

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
                params.put("tag", "allproduct");
               /* String email1 = params.put("email", email);
                params.put("password", password);*/

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    //여기에 추가
    //이건 EditText로 검색쿼리 받아서 해당되는 제품만 가지고 오는 함수
    private void downProduct(final String searchword) {
        // Tag used to cancel the request
        String tag_string_req = "req_search";
        listdata = new ArrayList<SearchResultItem>();

        pDialog.setMessage("로딩중...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "detail Response: " + response.toString());
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

                            item = new SearchResultItem(a, b, c);
                            listdata.add(item);
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

                HashMap<Integer, Object> imgs = new HashMap<Integer, Object>();
                lad = new ListViewAdapter(Search.this, R.layout.search_result_item, listdata, imgs);
                searchPost(lad);

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
               params.put("tag", "product");
                params.put("searchword",searchword);//상단에서 매개변수로 가져온 거를 params로 날렸네
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
/*
    //이건 지우지 말고, 처음 딱 들어왔을 때는 -> 우리 앱에 등록된 전체 제품 보여주게끔 (아직 몇개 안되니까 ㅎㅎ)
    private class phpDown extends AsyncTask<String, Integer, ListViewAdapter> {
        JSONArray ja;
        String a, b, c;

        @Override
        protected ListViewAdapter doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            String line = br.readLine();
                            if (line == null) break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String data = jsonHtml.toString();

            try {
                JSONObject root = new JSONObject(data);
                ja = root.getJSONArray("search");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    a = jo.getString("imgUrl");
                    b = jo.getString("brandName");
                    c = jo.getString("productName");
                    item = new SearchResultItem(a, b, c);
                    listdata.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<Integer, Object> imgs = new HashMap<Integer, Object>();
            lad = new ListViewAdapter(Search.this, R.layout.search_result_item, listdata, imgs);

            return lad;
        }

        @Override
        protected void onPostExecute(ListViewAdapter listViewAdapter) {
            lv.setAdapter(listViewAdapter);
            for (int i = 0; i < listViewAdapter.getCount(); i++) {
                SearchResultItem sri = (SearchResultItem) listViewAdapter.getItem(i);
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("img_path", sri.getSet(0));
                hm.put("position", i);
                count = lad.getCount();
                //tv는 리스트뷰 갱신할 때마다 항목의 갯수를 넣어 텍스트뷰를 갱신한다.
                tv.setText("총 " + count + "개 제품");
                new setImg().execute(hm);
            }
        }
    }   //phpDown*/



   public void searchPost(ListViewAdapter listViewAdapter) {
        lv.setAdapter(listViewAdapter);
        for (int i = 0; i < listViewAdapter.getCount(); i++) {
            SearchResultItem sri = (SearchResultItem) listViewAdapter.getItem(i);
            HashMap<String, Object> hm = new HashMap<String, Object>();
            hm.put("img_path", sri.getSet(0));
            hm.put("position", i);
            count = lad.getCount();
            //tv는 리스트뷰 갱신할 때마다 항목의 갯수를 넣어 텍스트뷰를 갱신한다.
            tv.setText("총 " + count + "개 제품");
            new setImg().execute(hm);
        }
    }


    private class setImg extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {
        Bitmap bmp;

        @Override
        protected HashMap<String, Object> doInBackground(HashMap<String, Object>... params) {
            String imgurl = (String) params[0].get("img_path");
            int position = (Integer) params[0].get("position");

            try {
                URL u = new URL(imgurl);
                HttpURLConnection conn2 = (HttpURLConnection) u.openConnection();
                conn2.setDoInput(true);
                conn2.connect();
                InputStream is = conn2.getInputStream();
                bmp = BitmapFactory.decodeStream(is);

                HashMap<String, Object> imghm = new HashMap<String, Object>();
                imghm.put("position", position);
                imghm.put("bitmap", bmp);
                return imghm;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            Bitmap bitmap = (Bitmap) result.get("bitmap");
            int position = (Integer) result.get("position");
            ListViewAdapter adapter = (ListViewAdapter) lv.getAdapter();
            HashMap<Integer, Object> newhm = (HashMap)((ListViewAdapter) lv.getAdapter()).getHM();
            newhm.put(position, bitmap);
            adapter.notifyDataSetChanged();
        }
    }   //setImg


    private class ListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int layout;
        private Context mContext = null;
        private ArrayList<SearchResultItem> listdata2;
        private HashMap<Integer, Object> imgs;

        public ListViewAdapter(Context context, int layout, ArrayList<SearchResultItem> ar, HashMap<Integer, Object> imgs) {
            super();
            this.mContext = context;
            this.layout = layout;
            this.listdata2 = ar;
            this.imgs = imgs;
            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() { return listdata2.size(); }
        @Override
        public Object getItem(int position) { return listdata2.get(position); }
        @Override
        public long getItemId(int position) { return position; }
        public HashMap getHM() { return imgs; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.search_result_item, null);
                holder.imv = (ImageView) convertView.findViewById(R.id.pImage);
                holder.txt1 = (TextView) convertView.findViewById(R.id.bName);
                holder.txt2 = (TextView) convertView.findViewById(R.id.pName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SearchResultItem data = (SearchResultItem) getItem(position);
            Bitmap b = (Bitmap) imgs.get(position);
            holder.imv.setImageBitmap(b);
            holder.txt1.setText(data.getSet(1));
            holder.txt2.setText(data.getSet(2));

            return convertView;
        }
    }   //listviewadapter


    //콜백메서드
    //이벤트 등이 발생하면 시스템에 의해 호출되는 메서드
    //아이템을 하나 선택했을 시에 호출
    /**
     * spinner 의 항목이 선택되었을 경우 호출된다.
     * @param parent - spinner 객체
     * @param view - 선택된 위치를 구성하는 View
     * @param position - 선택된 위치에 대한 인덱스
     * @param id - 선택된 위치에 대한 인덱스
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position !=0) {
            switch (parent.getId()) {
                case R.id.spinner1://브랜드
                    s2.setSelection(0); s3.setSelection(0); s4.setSelection(0);//나머지 스피너들 초기화
                    String q = s1.getSelectedItem().toString();
                    Toast.makeText(this,q, Toast.LENGTH_SHORT).show();
                    checkSpinner(q);
                    break;
                case R.id.spinner2://else가 넘겨지면 나머지 애들은 제외하고 가지고 오기
                    s1.setSelection(0); s3.setSelection(0); s4.setSelection(0);
                    String q2 = s2.getSelectedItem().toString();
                    checkSpinner(q2);
                    break;
                case R.id.spinner3:
                    s2.setSelection(0); s1.setSelection(0); s4.setSelection(0);
                    String q3 = s3.getSelectedItem().toString();//연령 40대~ 가 들어오면 40대로 바꿔서
                    checkSpinner(q3);
                    break;//성별
                case R.id.spinner4:
                    s2.setSelection(0); s3.setSelection(0); s1.setSelection(0);
                    String q4 = s4.getSelectedItem().toString();//연령 40대~ 가 들어오면 40대로 바꿔서
                    checkSpinner(q4);
                    break;
            }
        }
    }

    /** spinner의 선택 상태가 해제될 경우 호출된다.*/
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //찾는 향수가 없을땐? 버튼을 누르면
    private void sendEmail(){
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.send_email, null);

        new AlertDialog.Builder(this).setTitle("제품 등록 요청하기").setView(dialogView).setPositiveButton("전송하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText t = (EditText)dialogView.findViewById(R.id.title);
                EditText c = (EditText)dialogView.findViewById(R.id.mail);
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
        }).setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }  //찾는 향수가 없다면


    //뷰-자바 연결 함수
    private void setLayout(){
        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView3);
        img3 = (ImageView) findViewById(R.id.imageView4);
        et = (EditText) findViewById(R.id.editText1);
        btn1 = (Button) findViewById(R.id.button1);
        lv = (ListView) findViewById(R.id.search_result);
        tv = (TextView) findViewById(R.id.textView1);
        s1 = (Spinner) findViewById(R.id.spinner1);
        s1.setPrompt("브랜드 선택"); //시발 왜 프롬프트 안먹히는데 십빨!!!!!!!
        s2 = (Spinner) findViewById(R.id.spinner2);
        s3 = (Spinner) findViewById(R.id.spinner3);
        s4 = (Spinner) findViewById(R.id.spinner4);
        //s5 = (Spinner) findViewById(R.id.spinner5);
        s6 = (Spinner) findViewById(R.id.spinner6);

        s1.setOnItemSelectedListener(this);
        s2.setOnItemSelectedListener(this);
        s3.setOnItemSelectedListener(this);
        s4.setOnItemSelectedListener(this);
        s6.setOnItemSelectedListener(this);



        //condition = s1.getSelectedItemPosition() + s2.getSelectedItemPosition() + s3.getSelectedItemPosition() + s4.getSelectedItemPosition();//+ s5.getSelectedItemPosition();

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); //뒤로가기 버튼

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//돋보기 이미지 버튼
                if (!et.getText().toString().equals("")) {
                    /*'et.getText().toString()' 를 select문 질의쿼리에 넣어준다
                            검색 결과를 리스트에 뿌린다*/
                    /*tv.setText(); 리스트 항목 개수 세어서 셋해주기*/
                    String searchword = et.getText().toString();
                    downProduct(searchword);//검색어의 내용을 넘겨서 해당 제품/브랜드에 해당하는 내용만 가지고 오는 함수

                }
            }
        }); //겁색버튼

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.getText().clear();
                lv.clearDisappearingChildren(); // 검색결과 리스트 비우기, 새로고침
            }
        });

        et.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(13)
        }); //et 글자수 제한



        //btn 선택시, Dialog 띄워 '제품 등록 요청하기' 를 구현한다.
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //검색 결과 리스트 항목 클릭시 이벤트 구현.
                //최근 본 상품 목록에 삽입
                Bitmap bitmap = (Bitmap)((ListViewAdapter) lv.getAdapter()).getHM().get(position);
                db3.addRecent(bitmap , listdata.get(position).getSet(1), listdata.get(position).getSet(2));

                //누르면 pName의 값을 읽어서 넘겨줌
                String productname = ((SearchResultItem) lad.getItem(position)).getSet(2);
                Intent in = new Intent(getApplicationContext(),
                        Product.class);
                in.putExtra("product",  productname);
                startActivity(in);
            }
        });


    } //쩌리 뷰 처리 구현

    //---------------------------------------------------------------스피너 클릭시 이거 마지막으로 꼭 해내고 말겠다ㅏㅏㅏㅏ
    private void checkSpinner(final String spinner1) {
        // Tag used to cancel the request
        String tag_string_req = "req_spinner";
        listdata = new ArrayList<SearchResultItem>();

        pDialog.setMessage("로딩중...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,AppConfig.URL_SPINNER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "spinner Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //JSONObject product = jObj.getJSONObject("results");//user 에 해당하는 JSONObject정보를 가져옴
                    JSONArray ja = jObj.getJSONArray("results");
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            String a = jo.getString("imgUrl");
                            String b = jo.getString("brandName");
                            String c = jo.getString("productName");

                            item = new SearchResultItem(a, b, c);
                            listdata.add(item);
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

                HashMap<Integer, Object> imgs = new HashMap<Integer, Object>();
                lad = new ListViewAdapter(Search.this, R.layout.search_result_item, listdata, imgs);
                searchPost(lad);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "spinner Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "spinner");
                params.put("spinner1",spinner1);//상단에서 매개변수로 가져온 거를 params로 날렸네
               /* String email1 = params.put("email", email);
                params.put("password", password);*/

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}