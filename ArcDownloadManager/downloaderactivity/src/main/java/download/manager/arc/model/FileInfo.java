
package download.manager.arc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileInfo {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("label")
    @Expose
    public String label;
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("extension")
    @Expose
    public String extension;
    @SerializedName("pathToSave")
    @Expose
    public String pathToSave;
    private String name = "";

    public FileInfo(String id, String extension, String pathToSave, String name, String imageLink) {
        this.id = id;
        this.extension = extension;
        this.pathToSave = pathToSave;
        this.name = name;
        this.imageLink = imageLink;
    }

//    public FileInfo(String id, String extension, String pathToSave) {
//        this.id = id;
//        this.extension = extension;
//        this.pathToSave = pathToSave;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    private String imageLink = "";

    @Override
    public String toString() {
        return "FileInfo{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", size='" + size + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
