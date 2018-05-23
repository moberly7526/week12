//위시리스트랑 최근 본 향수를 위한 Adapter
package info.androidhive.loginandregistration;


import android.content.Context;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.etsy.android.grid.util.DynamicHeightImageView;

import java.util.ArrayList;
import java.util.Random;

/***
 * ADAPTER
 */

public class SampleAdapter extends ArrayAdapter<String> {

    private static final String TAG = "SampleAdapter";

    private final LayoutInflater mLayoutInflater;


    public SampleAdapter(Context context, int textViewResourceId,
                         ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {//getView는 한 아이템에 들어갈 레이아웃을 지정한다

        ViewHolder vh;
        if (convertView == null) { //convertView가 null이면 새로 레이아웃을 생성하고, 그렇지 않으면 이미 만들어진 뷰를 재사용한다
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();

            vh.btnDelete = (ImageView) convertView.findViewById(R.id.my_delete1);
            vh.productImage = (ImageView) convertView.findViewById(R.id.product_img1);
            vh.brandName = (TextView) convertView.findViewById(R.id.brand_name1);
            vh.productName = (TextView)convertView.findViewById(R.id.perfume_name1);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        //vh.productImage.setHeightRatio(positionHeight);
        ImageLoader.getInstance().displayImage(getItem(position), vh.productImage);

        //요기다가 쿵짝쿵짝해서 나온 값을 set하는디?
        //예를 들어서


        //vh.brandName.setText(getItem(position)+position);
        //vh.productName.setText("아르페쥬");
        //vh.productImage.getResources().getDrawable(R.drawable.dicnote);

                vh.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Toast.makeText(getContext(), "Button Clicked Position " +
                                position, Toast.LENGTH_SHORT).show();
                    }
                }); //삭제버튼 온클릭 리스너
        return convertView;
    }//getView();,리턴값은 한 아이템의 화면인 converView

    static class ViewHolder {//뷰 홀더
        ImageView btnDelete;//삭제 버튼
        ImageView productImage;//제품 이미지
        TextView productName;//제품 이름
        TextView brandName;//브랜드 이름
    }

}