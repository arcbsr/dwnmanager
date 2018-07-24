package download.manager.arc.DownloadManager.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import download.manager.arc.DownloadManager.entity.AppInfo;
import download.manager.arc.DownloadManager.service.DownloadService;


public class CustomReceiver extends BroadcastReceiver {
    public final static String CANCEL = "cancel";
    public final static String PAUSE = "pause";
    public final static String RESUME = "resume";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        //Log.w("CustomReceiver", intent.getAction());
        int position = intent.getIntExtra(DownloadService.EXTRA_POSITION, 0);
        AppInfo appInfo = (AppInfo) intent.getSerializableExtra(DownloadService.EXTRA_APP_INFO);
        String tag = intent.getStringExtra(DownloadService.EXTRA_TAG);
        switch (intent.getAction()) {
            case CANCEL:
                DownloadService.intentCancel(context, tag);
                break;
            case PAUSE:
                DownloadService.intentPause(context, tag);
                break;
            case RESUME:
                DownloadService.intentDownload(context, position, tag, appInfo);
                break;
        }
    }
}
