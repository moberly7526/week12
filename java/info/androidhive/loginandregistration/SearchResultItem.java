package info.androidhive.loginandregistration;

public class SearchResultItem {
    private String[] set;

    public SearchResultItem(String[] data) {
        this.set = data;
    }

    public SearchResultItem(String s1, String s2, String s3){
        set = new String[3];
        set[0] = s1;
        set[1] = s2;
        set[2] = s3;
    }

    public String[] getSet(){
        return set;
    }

    public String getSet(int index){
        return set[index];
    }

    public void setSet(String[] data){ set = data; }

}
