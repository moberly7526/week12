//최근 본 향수
package info.androidhive.loginandregistration;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.etsy.android.grid.StaggeredGridView;

import org.w3c.dom.Text;

import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SQLiteHandler3;
import info.androidhive.loginandregistration.manager.ParentActivity;



public class MyRecent extends ParentActivity implements OnClickListener{
    private static final String TAG = "StaggeredGridActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";

    private StaggeredGridView mGridView2;
/*
    private boolean mHasRequestedMore;
    //어댑터 부분
    private SampleAdapter mAdapter;
    private ArrayList<String> mData;*/

    //설정 버튼 변수
    private ImageView setting_intent2;
    private TextView wish_intent2;
    private TextView review_intent2;
    private TextView nickname2;

    private SQLiteHandler db;//디비 핸들러
    private SQLiteHandler3 db3;
    ArrayList<MyRecentItem> listdata;
    private ListViewAdapter lad;

    CheckProfile pro;
    private ImageView profileImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recent);

        //설정 버튼 관련
        setting_intent2 = (ImageView)findViewById(R.id.setting_img2);
        setting_intent2.setOnClickListener(this);
        wish_intent2 = (TextView)findViewById(R.id.my_wish2);
        wish_intent2.setOnClickListener(this);

        review_intent2 = (TextView)findViewById(R.id.my_review2);
        review_intent2.setOnClickListener(this);
        //여기까지

        nickname2 = (TextView)findViewById(R.id.nickname2);

        db = new SQLiteHandler(getApplicationContext());
        db3 = new SQLiteHandler3(getApplicationContext());

        profile();


        actionbar();

        mGridView2 = (StaggeredGridView) findViewById(R.id.grid_view2);
        LayoutInflater layoutInflater = getLayoutInflater();

        listdata = db3.getRecent();
        lad = new ListViewAdapter(MyRecent.this, listdata);
        mGridView2.setAdapter(lad);


    }//onCreate();



    private class ViewHolder{
        public ImageView pimg;
        public TextView brname;
        public TextView prname;
    }

    private class ListViewAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private Context mContext = null;
        private ArrayList<MyRecentItem> listdata2;

        public ListViewAdapter(Context context, ArrayList<MyRecentItem> arr){
            this.mContext = context;
            this.listdata2 = arr;
            this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return listdata2.size();
        }

        @Override
        public Object getItem(int i) {
            return listdata2.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item_sample_, null);
                holder.pimg = (ImageView)convertView.findViewById(R.id.product_img2);
                holder.brname = (TextView)convertView.findViewById(R.id.brand_name2);
                holder.prname = (TextView)convertView.findViewById(R.id.perfume_name2);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            MyRecentItem item = (MyRecentItem)getItem(i);
            holder.pimg.setImageBitmap(item.getPimg());
            holder.brname.setText(item.getBname());
            holder.prname.setText(item.getPname());

            return convertView;
        }
    }





    private void profile(){
        // sqllite에서 사용자 정보 가지고 오기
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        // Displaying the user details on the screen
        nickname2.setText(name);//sqllite db에서 가져온 name 내용으로 set

        //프로필 연결하기
        profileImg = (ImageView)findViewById(R.id.profile_img);

        //꽃이름 사진에 해당하는 id 가져오기
        String flowerName = user.get("profile");
        pro = new CheckProfile();
        int i = pro.isYourFlower(flowerName);
        profileImg.setImageResource(i);
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


    //종료하시겠습니까?(AlertDialog)관련 메소드
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_img2:
                // TODO Auto-generated method stub
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.my_wish2:
                // TODO Auto-generated method stub
                Intent intent2 = new Intent(this, MyWish.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.my_review2:
                // TODO Auto-generated method stub
                Intent intent3 = new Intent(this, MyReview.class);
                startActivity(intent3);
                finish();
                break;
            default:
                break;
        }
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
