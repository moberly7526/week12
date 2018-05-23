package info.androidhive.loginandregistration;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import info.androidhive.loginandregistration.manager.ParentActivity;

import java.util.ArrayList;

public class Dicnote extends ParentActivity {

    ExpandableListView mListView;
    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;

    //차일드 뷰의 내용이 될 변수 초기화
    private ArrayList<String> mChildListContent = null;
    private ArrayList<String> mChildListContent2 = null;
    private ArrayList<String> mChildListContent3 = null;
    private ArrayList<String> mChildListContent4 = null;
    private ArrayList<String> mChildListContent5 = null;
    private ArrayList<String> mChildListContent6 = null;
    private ArrayList<String> mChildListContent7 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicnote);

        //홈버튼은 안보이고 뒤로가기 보이게
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ExpandableListView) findViewById(R.id.elv_list2);

        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<ArrayList<String>>();

        //자식뷰 내용
        getChildListContent();

        mListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        mListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });


        mListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });


        mListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });


    }//onCreate();

    public void getChildListContent(){

        //자식 내용
        mChildListContent = new ArrayList<String>();
        mChildListContent2 = new ArrayList<String>();
        mChildListContent3 = new ArrayList<String>();

        mChildListContent4 = new ArrayList<String>();
        mChildListContent5 = new ArrayList<String>();
        mChildListContent6 = new ArrayList<String>();
        mChildListContent7 = new ArrayList<String>();

        //탑노트
        mGroupList.add("탑노트(Top Note)");
        mChildListContent.add("향수를 뿌린 후, 약 10분 전· 후의 향 강한 향. 휘발성이 강한 시트러스, 플로럴, 푸르티 계열을 주로 사용한다.");
        mChildList.add(mChildListContent);
        mListView.setAdapter(new BaseExpandableAdapter2(this, mGroupList, mChildList));

        //미들노트
        mGroupList.add("미들노트(Middle Note)");
        mChildListContent2.add("향수를 뿌린 후, 30분~1시간이 지난 후의 향.     알코올이 증발하여 가장 안정적인 향이다. 다양한 계열의 향조가 사용되며 은은하게 지속되는 것이 특징. 사람들이 일반적으로 느끼는 향이 이 미들노트이다.");
        mChildList.add(mChildListContent2);
        mListView.setAdapter(new BaseExpandableAdapter2(this, mGroupList, mChildList));

        //베이스노트
        mGroupList.add("베이스노트(Base Note)");
        mChildListContent3.add("향수를 뿌린 후, 3시간 이후의 향. 제일 오래가고 안정감 있는 향을 써야 하므로 바닐라, 머스크, 우디를 주로 사용한다.");
        mChildList.add(mChildListContent3);
        mListView.setAdapter(new BaseExpandableAdapter2(this, mGroupList, mChildList));

        mGroupList.add("퍼퓸(Perfume)");
        mChildListContent5.add("농도 15%~30%의 제품으로 지속시간이 6~7시간으로 가장 길다. 향수의 원액에 가까울 정도로 향이 깊다.");
        mChildList.add(mChildListContent5);
        mListView.setAdapter(new BaseExpandableAdapter2(this, mGroupList, mChildList));

        mGroupList.add("오 드 뜨왈렛(Eau de Toilette)");
        mChildListContent6.add("농도 8%~10%의 제품으로 지속시간은 3~4시간이다. 처음 향수를 쓰는 사람들이 보통 선택하는 종류이며     이 종류의 제품이 가장 다양하다");
        mChildList.add(mChildListContent6);
        mListView.setAdapter(new BaseExpandableAdapter2(this, mGroupList, mChildList));

        mGroupList.add("오 드 퍼퓸(Eau de Perfume)");
        mChildListContent7.add("농도 10%~25%의 제품으로 지속시간은 4~6시간이다. 퍼퓸에 가까울 정도로 농도가 높은 편이다.");
        mChildList.add(mChildListContent7);
        mListView.setAdapter(new BaseExpandableAdapter2(this, mGroupList, mChildList));

        mGroupList.add("오 드 코롱(Eadu de Cologne)");
        mChildListContent4.add("농도 3%~5%의 제품으로 지속시간은 1~2시간이다. 향이 가벼운 편으로 부담 없이 스포츠나 목욕 후에 사용한다.");
        mChildList.add(mChildListContent4);
        mListView.setAdapter(new BaseExpandableAdapter2(this, mGroupList, mChildList));


    };

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}

