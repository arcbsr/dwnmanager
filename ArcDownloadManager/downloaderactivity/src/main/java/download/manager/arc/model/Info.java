
package download.manager.arc.model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;
    @SerializedName("urls")
    @Expose
    public ArrayList<FileInfo> urls = new ArrayList<>();

}
