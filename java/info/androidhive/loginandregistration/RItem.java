package info.androidhive.loginandregistration;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.media.Rating;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import android.widget.RatingBar;

public class RItem implements Parcelable{
    private String brandName;    private String productName;
    private Bitmap productImg;   private float rBar;
    private String rEcord;       private Drawable aLarm;
    private byte[] productImg2;  private long id;

    public String getBrandName(){ return brandName; }
    public String getProductName(){ return productName; }
    public Bitmap getProductImg(){ return productImg; }
    public float getrBar(){ return rBar; }
    public String getrEcord(){ return rEcord; }
    public byte[] getProductImg2(){ return productImg2; }
    public long getId(){ return id; }

    public RItem(int id, String a, String b, Bitmap c, float d, String e){
        this.id = (long)id;
        this.brandName = a;
        this.productName = b;
        this.productImg = c;
        this.rBar = d;
        this.rEcord = e;
    }   //addRecord, sqlite에서 사용

    public RItem(String a, String b, Bitmap c, float d, String e){
        this.brandName = a;
        this.productName = b;
        this.productImg = c;
        this.rBar = d;
        this.rEcord = e;
    }

    public RItem(long id, String a, String b, byte[] c, float d, String e){
        this.id = id;
        this.brandName = a;
        this.productName = b;
        this.productImg2 = c;
        this.rBar = d;
        this.rEcord = e;
    }   //AddRecord에서 사용

    public RItem(long id, String a, String b, Bitmap c, float d, String e){
        this.id = id;
        this.brandName = a;
        this.productName = b;
        this.productImg = c;
        this.rBar = d;
        this.rEcord = e;
    }   //addRecord return type

    public RItem(long id, String a, String b, Bitmap c){
        this.id = id;
        this.brandName = a;
        this.productName = b;
        this.productImg = c;
    }   //위시리스트에서 불러온 데이터 저장시 사용



    public RItem(Parcel src){
        this.brandName = src.readString();
        this.productName = src.readString();
        //this.productImg = src.readParcelable(Bitmap.class.getClassLoader());
        //this.rBar.setRating(src.readFloat());
        this.rBar = src.readFloat();
        this.rEcord = src.readString();
    }

    public static final Creator<RItem> CREATOR = new Creator<RItem>() {
        @Override
        public RItem createFromParcel(Parcel source) {
            return new RItem(source);
        }   //serialization 역할
        @Override
        public RItem[] newArray(int size) {
            return new RItem[size];
        }
    };  //CREATOR

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandName);
        dest.writeString(productName);
        dest.writeParcelable(productImg, flags);
        //dest.writeFloat(rBar.getRating());
        dest.writeFloat(rBar);
        dest.writeString(rEcord);
    }


}
//parcel 참고 http://blog.naver.com/kimdaesuk/60180737200
