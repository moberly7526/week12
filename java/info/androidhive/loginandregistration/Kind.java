package info.androidhive.loginandregistration;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import info.androidhive.loginandregistration.manager.ParentActivity;


public class Kind extends ParentActivity {
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind);


        mListView = (ListView) findViewById(R.id.mList);

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        mAdapter.addItem(getResources().getDrawable(R.drawable.c_floral),  //이미지 변경, 계열이름 작성하기
                "플로럴(Floral)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_woody),
                "우디(Woody)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_green),
                "그린(Green)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_chypre),
                "시프레(Chypre)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_fruity),
                "프루티(Fruity)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_citrus),
                "시트러스(Citrus)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_powdery),
                "파우더리(Powdery)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_oriental),
                "오리엔탈(Oriental)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_aqua),
                "아쿠아(Aqua)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_aldehyde),
                "알데히드(Aldehyde)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_fougere),
                "후제아(Fougere)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_spicy),
                "스파이시(Spicy)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_musk),
                "머스크(Musk)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_leather),
                "레더(Leather)");
        mAdapter.addItem(getResources().getDrawable(R.drawable.c_gourmand),
                "그루망(Gourmand)");


        mListView.setOnItemClickListener(new OnItemClickListener() { //
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListData mData = mAdapter.mListData.get(position);
                Intent intent = new Intent(Kind.this,Classify.class);
                String str = mData.mTitle;
                intent.putExtra("title",str);
                startActivity(intent);
                //Toast.makeText(Kind.this, mData.mTitle, Toast.LENGTH_SHORT).show(); //mtitle
                            }
        });

        //홈버튼은 안보이고 뒤로가기 안보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }//onCreate();


    private class ViewHolder {
        public ImageView mIcon;

        public TextView mText;

    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(Drawable icon, String mTitle){
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mIcon = icon;
            addInfo.mTitle = mTitle;

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void sort(){
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_kind_list_item, null);

                holder.mIcon = (ImageView) convertView.findViewById(R.id.mImage);
                holder.mText = (TextView) convertView.findViewById(R.id.mText);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);

            if (mData.mIcon != null) {
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            }else{
                holder.mIcon.setVisibility(View.GONE);
            }

            holder.mText.setText(mData.mTitle);
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
}