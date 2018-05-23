package info.androidhive.loginandregistration;

import android.graphics.Bitmap;

public class ReviewItem {
    private Bitmap userimg;
    private String usernic;
    private float rating;
    private String review;

    public Bitmap getUserimg(){  return userimg; }
    public String getUsernic(){ return usernic; }
    public float getRating() { return rating; }
    public String getReview() { return review; }

    public ReviewItem(Bitmap a, String b, float c, String d){
        this.userimg = a;
        this.usernic = b;
        this.rating = c;
        this.review = d;
    }
}
