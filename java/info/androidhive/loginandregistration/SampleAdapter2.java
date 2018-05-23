//내가 쓴 리뷰
package info.androidhive.loginandregistration;


import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/***
 * ADAPTER
 */

public class SampleAdapter2 extends ArrayAdapter<String> {

    private static final String TAG = "SampleAdapter";

    static class ViewHolder {
        //DynamicHeightTextView txtLineOne;
        ImageView productImage,btn_delete,profileImage,line;//제품 이미지,삭제버튼,프로필이미지,화살표,선
        TextView brandName,perfumeName,userName,review;//브랜드명,향수이름,사용자,별점,리뷰
        RatingBar ratingBar;

    }

    private final LayoutInflater mLayoutInflater;
    //private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    //private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    //자바 코드에서 불렀을 때
    public SampleAdapter2(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);

        //mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.custom_grey);//배경은 전체 배경화면과 같게끔
        /*
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.blue);
        mBackgroundColors.add(R.color.yellow);
        mBackgroundColors.add(R.color.grey);
        */

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample2, parent, false);
            vh = new ViewHolder();
            //vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
            vh.productImage = (ImageView) convertView.findViewById(R.id.productImage);
            vh.btn_delete = (ImageView) convertView.findViewById(R.id.btn_delete);
            vh.line = (ImageView) convertView.findViewById(R.id.line);
            vh.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);

            vh.brandName = (TextView)convertView.findViewById(R.id.brandName);
            vh.perfumeName = (TextView)convertView.findViewById(R.id.perfumeName);
            vh.userName = (TextView)convertView.findViewById(R.id.userName );
            vh.ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);
            vh.review = (TextView)convertView.findViewById(R.id.review);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        //double positionHeight = getPositionRatio(position);

        /*int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));*/

        //Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        //처음 예제에서 위치를 출력했던 텍스트뷰
        //vh.txtLineOne.setHeightRatio(positionHeight);
        //vh.txtLineOne.setText(getItem(position) + position);

        //삭제 버튼 눌렀을 때 해당 위치 출력
        //여기다가 삭제 액션 주면 되겠다
        //버튼을 누르면 -> 해당 꺼가 없어지게끔
        vh.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(getContext(), "Button Clicked Position " +
                        position, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

}