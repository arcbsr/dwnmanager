package download.manager.arc.DownloadManager;


import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import download.manager.arc.DownloadManager.entity.AppInfo;
import download.manager.arc.Utils.ArcSharedPrefUtil;
import download.manager.arc.model.FileInfo;
import download.manager.arc.model.Info;

/**
 * Created by Aspsine on 2015/7/8.
 */
 public class DataSource {

    private static final DataSource sDataSource = new DataSource();

    public static String checkURL(String url) {
        return url.replaceAll("\\/", "");
    }


    public static DataSource getInstance() {
        return sDataSource;
    }

    //List<AppInfo> appInfos = new ArrayList<AppInfo>();
    private final String KEY_JSON_DWN_LOCAL = "key_dwn_source";
    Info appData = null;

    public void setData(FileInfo data, Context context) {
//        appInfos.clear();
//        appInfos.add(new AppInfo("", data.getName(), data.getImageLink(), data.id, extension, pathToSave));
        if (!new File(data.pathToSave).exists()) {
            new File(data.pathToSave).mkdirs();
        }
        if (appData == null) {
            appData = getDataLocal(context);
        }
        for (FileInfo urlD : appData.urls) {
            if (urlD.id.equals(data.id)) {
                return;
            }
        }
//        data.extension = extension;
//        data.pathToSave = pathToSave;
        appData.urls.add(data);

        try {
            ArcSharedPrefUtil.setSetting(context, KEY_JSON_DWN_LOCAL, new Gson().toJson(appData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AppInfo> getData(Context context) {
//        List<AppInfo> appInfos = new ArrayList<AppInfo>();
////        for (int i = 0; i < NAMES.length; i++) {
////            AppInfo appInfo = new AppInfo(String.valueOf(i), NAMES[i], IMAGES[i], URLS[i]);
////            appInfos.add(appInfo);
////        }
//        appInfos.add(new AppInfo("", name, imageUrl, data.id));
//        return appInfos;
        List<AppInfo> appInfosTemp = new ArrayList<>();
        if (appData == null) {
            appData = getDataLocal(context);
        }
        for (FileInfo data : appData.urls) {
            appInfosTemp.add(new AppInfo("", data.getName(), data.getImageLink(), data.id, data.extension, data.pathToSave));
        }

        return appInfosTemp;
    }

    public void removeData(Context context, String url) {

        if (appData == null) {
            appData = getDataLocal(context);
        }
        for (FileInfo data : appData.urls) {
            if (data.id.equals(url)) {
                appData.urls.remove(data);
                break;
            }
        }

        try {
            ArcSharedPrefUtil.setSetting(context, KEY_JSON_DWN_LOCAL, new Gson().toJson(appData));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Info getDataLocal(Context context) {
        Info appData = new Info();
        try {
            String downLiast = ArcSharedPrefUtil.getSetting(context, KEY_JSON_DWN_LOCAL, "");
            Gson gson = new Gson();
            appData = gson.fromJson(downLiast, Info.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (appData == null) {
            appData = new Info();
        }
        return appData;
    }
}
