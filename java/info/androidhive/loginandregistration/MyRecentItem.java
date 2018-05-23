package info.androidhive.loginandregistration;

import android.graphics.Bitmap;

public class MyRecentItem {
    private Bitmap pimg;
    private String bname;
    private String pname;

    public MyRecentItem(Bitmap a, String b, String c){
        this.pimg = a;
        this.bname = b;
        this.pname = c;
    }

    public Bitmap getPimg() { return pimg; }

    public String getBname() { return bname; }

    public String getPname() { return pname; }
}
