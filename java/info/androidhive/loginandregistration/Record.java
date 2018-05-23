package info.androidhive.loginandregistration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import info.androidhive.loginandregistration.helper.SQLiteHandler2;

public class Record extends ActionBarActivity {

    private RecordDBAdapter rda;
    private SQLiteHandler2 wda;
    private ListViewAdapter lad = null;
    ArrayList<RItem> rlistdata;
    ArrayList<MyWishItem> mwlistdata;

    Button load, newr;
    ListView rlist;
    TextView empty;
    Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        actionBar();

        load = (Button)findViewById(R.id.load);
        newr = (Button)findViewById(R.id.rnew);
        rlist = (ListView)findViewById(R.id.recordlist);
        empty = (TextView)findViewById(R.id.empty);

        //BitmapDrawable eric = (BitmapDrawable)getResources().getDrawable(R.drawable.seric);
        //img = eric.getBitmap();

        wda = new SQLiteHandler2(Record.this);
        rda = new RecordDBAdapter(Record.this); //dbadapter, helper 생성
        rda.open();

        rlistdata = rda.getAllRecords();

        if(rlistdata.size() > 0){
            empty.setVisibility(View.INVISIBLE);
        }

        lad = new ListViewAdapter(Record.this, R.layout.record_item, rlistdata);
        rlist.setAdapter(lad);

        //1. 초기화면은 버튼 둘, 빈 리스트만 보여준다.

        load.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        }); //2. 위시리스트에서 불러오기

        newr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addR();
            }
        }); //3. 리스트 항목 추가하기

        rlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editintent = new Intent(Record.this, EditRecord.class);
                editintent.putExtra("position", rlistdata.get(position));
                editintent.putExtra("p", position);
                editintent.putExtra("dbid", rlistdata.get(position).getId());
                startActivityForResult(editintent, 2);
            }
        }); //4. 리스트 항목 클릭시, 추가 정보 입력 및 수정을 위한 액티비티 호출 구현

        rlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (lad.getCount() > 0) {
                    long dbid = ((RItem) lad.getItem(position)).getId();
                    rda.delRecord(dbid);
                    rlistdata.remove(position);
                    lad.notifyDataSetChanged();
                    if (lad.getCount() <= 0) {
                        empty.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        }); //5. 리스트 항목 길게 클릭시 삭제



    }//onCreate();



    private class ViewHolder{
        public TextView bname;
        public TextView pname;
        public TextView slash;
        public ImageView pimg;
        public RatingBar rb;    //여기선 레이팅바로 선언해도 되는거겠찌?
        public TextView comm;
        public ImageView alm;
    }


    private void load(){    //위시리스트에서 불러오기
        //sqlite wish 테이블에서, 제품 이미지/브랜드명/제품명을 가져와 add한다.
        mwlistdata = wda.getAllWishes();  //sqlite 위시테이블에서 위시들 전부 가져오기

        for(int i = 0; i < mwlistdata.size(); i++){
            lad.add(rda.addRecord(mwlistdata.get(i).getBrandname(), mwlistdata.get(i).getProductname(), mwlistdata.get(i).getImg()));
            lad.notifyDataSetChanged();

            if(lad.getCount() > 0){
                empty.setVisibility(View.INVISIBLE);
            }
        }

    } //위시리스트에서 불러오기


    private void addR(){
        Intent addintent = new Intent(Record.this, AddRecord.class);
        startActivityForResult(addintent, 1);
    }   //추가하기



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    if(data != null){
                        /*RItem newitem = data.getParcelableExtra("newitem");
                        rda.addRecord(newitem);
                        rlistdata.add(newitem);
                        lad.notifyDataSetChanged();*/
                        /*Bundle bundle = data.getExtras();
                        RItem newitem = bundle.getParcelable("newitem");
                        RItem n = new RItem(newitem.getBrandName(), newitem.getProductName(), newitem.getrBar(), newitem.getrEcord());*/
                        String a1 = data.getStringExtra("a1");
                        String a2 = data.getStringExtra("a2");
                        byte[] a3 = data.getByteArrayExtra("a3");
                        float a4 = data.getFloatExtra("a4", 0);
                        String a5 = data.getStringExtra("a5");
                        lad.add(rda.addRecord(a1, a2, a3, a4, a5));  //dbid도 같이 넘겨준다
                        lad.notifyDataSetChanged();
                        if(lad.getCount() > 0){
                            empty.setVisibility(View.INVISIBLE);
                        }
                    }
                }   //추가
                break;

            case 2: //edit
                if(resultCode == RESULT_OK){
                    if(data != null){
                        int p = data.getIntExtra("p", -1);
                        float f = data.getFloatExtra("newrating", 0);
                        String s = data.getStringExtra("newcomment");
                        long dbid = data.getLongExtra("dbid", 0);

                        rda.editRecord(dbid, f, s);

                        RItem o = (RItem)lad.getItem(p);
                        RItem renew = new RItem(dbid, o.getBrandName(), o.getProductName(), o.getProductImg(), f, s);
                        rlistdata.set(p, renew);

                        lad.notifyDataSetChanged();

                    }
                }   //변경사항 적용
                break;
        }
    }   //액티비티가 종료되면 호출되는 콜백메소드



    @Override
    protected void onResume() {
        super.onResume();
        rda.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //rda.close();
    }


    private void hide(){
        if(rda.count == 0){
            empty.setVisibility(View.VISIBLE);
        } else{
            empty.setVisibility(View.INVISIBLE);
        }
    }


    private class ListViewAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private int layout;
        private Context mContext = null;
        private ArrayList<RItem> listdata;

        public ListViewAdapter(Context context, int layout, ArrayList<RItem> arr){
            super();
            this.mContext = context;
            this.layout = layout;
            this.listdata = arr;
            this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {return listdata.size();}
        @Override
        public Object getItem(int position) {return listdata.get(position);}
        @Override
        public long getItemId(int position) {return position;}

        public void add(RItem r){
            listdata.add(r);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.record_item, null);
                holder.bname = (TextView)convertView.findViewById(R.id.brandname);
                holder.pname = (TextView)convertView.findViewById(R.id.productname);
                holder.slash = (TextView)convertView.findViewById(R.id.sl);
                holder.pimg = (ImageView)convertView.findViewById(R.id.rimg);
                holder.rb = (RatingBar)convertView.findViewById(R.id.rrb);
                holder.comm = (TextView)convertView.findViewById(R.id.rcomment);
                holder.alm = (ImageView)convertView.findViewById(R.id.alarm);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            RItem item = (RItem) getItem(position);
            holder.bname.setText(item.getBrandName());
            holder.pname.setText(item.getProductName());
            holder.pimg.setImageBitmap(item.getProductImg());
            holder.rb.setRating(item.getrBar());
            holder.comm.setText(item.getrEcord());

            holder.alm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getPhoto();
                }
            });

            return convertView;
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


    private void actionBar(){
        //액션바(21이하 버전이라 getActionBar하면 crash현상)
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //중간정렬을 위한 부분
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.ab_record, null),
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

}

/*

if(lad.getCount() > 0){
        empty.setVisibility(View.INVISIBLE);
        } else if(lad.getCount() <= 0){
        empty.setVisibility(View.VISIBLE);
        }
*/
