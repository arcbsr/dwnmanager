package download.manager.arc.DownloadManager;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import download.manager.arc.DownloadManager.ui.activity.AppListActivity;
import download.manager.arc.Utils.ArcSharedPrefUtil;
import download.manager.arc.model.FileInfo;

public class DownloadBind {
    public static final String FOLDER_NAME = "arcdownload";

    public static String getFolderPath(Context context) {
        String fileDir = ArcSharedPrefUtil.getSetting(context, "dwn_dir_selected",
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME);
        return fileDir;

    }

    public static void downloadQueeInit(Context context, FileInfo data) {
        DataSource.getInstance().setData(data, context);
        Intent intent = new Intent(context, AppListActivity.class);
        context.startActivity(intent);
        //Log.w("playing path", data.id);
        //startAppVideo(context, data.id);
    }

    public static void downloadQueeInit(Context context, FileInfo data[]) {
        for (FileInfo url : data) {
            DataSource.getInstance().setData(url, context);
        }
        Intent intent = new Intent(context, AppListActivity.class);
        context.startActivity(intent);
        //Log.w("playing path", data.id);
        //startAppVideo(context, data.id);
    }
}
