package info.androidhive.loginandregistration;

import android.graphics.Bitmap;

public class MyWishItem {
    private int id;
    private Bitmap img;
    private String brandname;
    private String productname;

    public int getId() { return id; }
    public Bitmap getImg() { return img; }
    public String getBrandname() { return brandname; }
    public String getProductname() { return productname; }

    public MyWishItem(int a, Bitmap b, String c, String d){
        this.id = a;
        this.img = b;
        this.brandname = c;
        this.productname = d;
    }

}
