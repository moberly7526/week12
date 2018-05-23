package info.androidhive.loginandregistration;

public class MyReviewItem {
    private String productimg;
    private String brandname;
    private String productname;
    private float rating;
    private String contents;

    public MyReviewItem(String pimg, String bname, String pname, float rating, String contents){
        this.productimg = pimg;
        this.brandname = bname;
        this.productname = pname;
        this.rating = rating;
        this.contents = contents;
    }

    public String getProductimg() { return productimg; }
    public String getBrandname(){ return brandname; }
    public String getProductname() { return productname; }
    public float getRating() { return rating; }
    public String getContents() { return contents; }
}
