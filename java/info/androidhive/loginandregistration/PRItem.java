package info.androidhive.loginandregistration;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RatingBar;

public class PRItem {
    private Bitmap productImg;
    private String productImgStr; //서버 디비에 저장된 이미지 주소값
    private String brandName;
    private String productName;
    private String[] colors;
    private String depth;
    private String type;
    private String[] notes;

    private String color1, color2, color3;
    private String top, mid, base;

    public Bitmap getProductImg() { return productImg; }
    public String getProductImgStr(){ return productImgStr; }
    public String getBrandName(){ return brandName; }
    public String getProductName(){ return productName; }
    public String[] getColors(){ return colors; }
    public String getDepth(){ return depth; }
    public String getType(){ return type; }
    public String[] getNotes() { return notes; }


    public PRItem(String imgurl, String bname, String pname, String c1,String c2,String c3,
                             String depth, String type, String t, String m, String b){
        this.productImgStr = imgurl;
        this.brandName = bname;
        this.productName = pname;
        this.colors = new String[3];
        colors[0] = c1;
        colors[1] = c2;
        colors[2] = c3;
        this.depth = depth;
        this.type = type;
        this.notes = new String[3];
        notes[0] = t;
        notes[1] = m;
        notes[2] = b;
    }   //제이슨 파싱시 사용


    public PRItem(Bitmap a, String b, String c, String[] d, String e, String f, String[] g){
        this.productImg = a;
        this.brandName = b;
        this.productName = c;
        this.colors = d;
        this.depth = e;
        this.type = f;
        this.notes = g;
    }

}   //제품 상세 객체
