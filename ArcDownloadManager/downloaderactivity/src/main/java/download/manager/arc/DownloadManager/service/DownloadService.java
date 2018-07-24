package download.manager.arc.DownloadManager.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.aspsine.multithreaddownload.CallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadManager;
import com.aspsine.multithreaddownload.DownloadRequest;
import com.aspsine.multithreaddownload.util.L;

import java.io.File;

import download.manager.arc.DownloadManager.DataSource;
import download.manager.arc.DownloadManager.entity.AppInfo;
import download.manager.arc.DownloadManager.ui.activity.AppListActivity;
import download.manager.arc.DownloadManager.util.CustomReceiver;
import download.manager.arc.DownloadManager.util.Utils;
import download.manager.arc.downloader.R;

/**
 * Created by aspsine on 15/7/28.
 */
public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();

    public static final String ACTION_DOWNLOAD_BROAD_CAST = "avdwn.avdn.com.avdn.DownloadManager:action_download_broad_cast";

    public static final String ACTION_DOWNLOAD = "avdwn.avdn.com.avdn.DownloadManager:action_download";

    public static final String ACTION_PAUSE = "avdwn.avdn.com.avdn.DownloadManager:action_pause";

    public static final String ACTION_CANCEL = "avdwn.avdn.com.avdn.DownloadManager:action_cancel";

    public static final String ACTION_PAUSE_ALL = "avdwn.avdn.com.avdn.DownloadManager.demo:action_pause_all";

    public static final String ACTION_CANCEL_ALL = "avdwn.avdn.com.avdn.DownloadManager:action_cancel_all";

    public static final String EXTRA_POSITION = "extra_position";

    public static final String EXTRA_TAG = "extra_tag";

    public static final String EXTRA_APP_INFO = "extra_app_info";

    /**
     * Dir: /Download
     */
    //private File mDownloadDir;

    private DownloadManager mDownloadManager;

    private NotificationManagerCompat mNotificationManager;

    public static void intentDownload(Context context, int position, String tag, AppInfo info) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_TAG, tag);
        intent.putExtra(EXTRA_APP_INFO, info);
        context.startService(intent);
    }

    public static void intentPause(Context context, String tag) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_PAUSE);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }

    public static void intentCancel(Context context, String tag) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_CANCEL);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }

    public static void intentPauseAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
    }

    public static void destory(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        context.stopService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            int position = intent.getIntExtra(EXTRA_POSITION, 0);
            AppInfo appInfo = (AppInfo) intent.getSerializableExtra(EXTRA_APP_INFO);
            String tag = intent.getStringExtra(EXTRA_TAG);
            switch (action) {
                case ACTION_DOWNLOAD:
                    download(position, appInfo, tag);
                    break;
                case ACTION_PAUSE:
                    pause(tag);
                    break;
                case ACTION_CANCEL:
                    cancel(tag);
                    break;
                case ACTION_PAUSE_ALL:
                    pauseAll();
                    break;
                case ACTION_CANCEL_ALL:
                    cancelAll();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public static PendingIntent getAction(Context context, int mPosition, AppInfo mAppInfo, String action) {
        Intent intenReceiver = new Intent(context, CustomReceiver.class);
        intenReceiver.setAction(action);
        intenReceiver.putExtra(EXTRA_POSITION, mPosition);
        intenReceiver.putExtra(EXTRA_TAG, mAppInfo.getUrl());
        intenReceiver.putExtra(EXTRA_APP_INFO, mAppInfo);
        PendingIntent pendinIntent = PendingIntent.getBroadcast(context, mPosition, intenReceiver, 0);
        return pendinIntent;
    }

    private void download(final int position, final AppInfo appInfo, String tag) {
        final DownloadRequest request = new DownloadRequest.Builder()
                .setName(appInfo.getName() + "." + appInfo.getExtension())
                .setUri(appInfo.getUrl())
                .setFolder(new File(appInfo.getSdPathFolder()))
                .build();
        mDownloadManager.download(request, tag,
                new DownloadCallBack(position, appInfo, mNotificationManager, getApplicationContext()));
    }

    private void pause(String tag) {
        mDownloadManager.pause(tag);
    }

    private void cancel(String tag) {
        mDownloadManager.cancel(tag);
    }

    private void pauseAll() {
        mDownloadManager.pauseAll();
    }

    private void cancelAll() {
        mDownloadManager.cancelAll();
    }

    public static class DownloadCallBack implements CallBack {

        private int mPosition;

        private AppInfo mAppInfo;

        private LocalBroadcastManager mLocalBroadcastManager;

        private NotificationCompat.Builder mBuilder;

        private NotificationManagerCompat mNotificationManager;

        private long mLastTime;

        public DownloadCallBack(int position, AppInfo appInfo, NotificationManagerCompat notificationManager, Context context) {
            mPosition = position;
            mAppInfo = appInfo;
            mNotificationManager = notificationManager;
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
            mBuilder.mActions.clear();
//            mBuilder.addAction(R.drawable.ic_close_red_24dp, CustomReceiver.CANCEL,
//                    getAction(mBuilder.mContext, mPosition, mAppInfo, CustomReceiver.CANCEL));
            Intent intent = new Intent(context, AppListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
        }


        @Override
        public void onStarted() {
            L.i(TAG, "onStart()");
            mBuilder.setSmallIcon(R.drawable.ic_cloud_download_black_24dp)
                    .setContentTitle(mAppInfo.getName())
                    .setContentText("Init Download")
                    .setProgress(100, 0, true)
                    .setTicker("Start download " + mAppInfo.getName());
            updateNotification();
        }

        @Override
        public void onConnecting() {
            L.i(TAG, "onConnecting()");
            mBuilder.setContentText("Connecting")
                    .setProgress(100, 0, true);
            updateNotification();

            mAppInfo.setStatus(AppInfo.STATUS_CONNECTING);
            sendBroadCast(mAppInfo);
        }

        @Override
        public void onConnected(long total, boolean isRangeSupport) {
            L.i(TAG, "onConnected()");
            mBuilder.mActions.clear();

//            mBuilder.addAction(R.drawable.ic_close_red_24dp, CustomReceiver.CANCEL,
//                    getAction(mBuilder.mContext, mPosition, mAppInfo, CustomReceiver.CANCEL));
//            mBuilder.addAction(R.drawable.ic_close_red_24dp,
//                    CustomReceiver.PAUSE, getAction(mBuilder.mContext, mPosition, mAppInfo, CustomReceiver.PAUSE));
            mBuilder.setContentText("Connected")
                    .setProgress(100, 0, true);
            updateNotification();
        }

        @Override
        public void onProgress(long finished, long total, int progress) {

            if (mLastTime == 0) {
                mLastTime = System.currentTimeMillis();
            }

            mAppInfo.setStatus(AppInfo.STATUS_DOWNLOADING);
            mAppInfo.setProgress(progress);
            mAppInfo.setDownloadPerSize(Utils.getDownloadPerSize(finished, total));

            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime > 500) {
                L.i(TAG, "onProgress()");
                mBuilder.setContentText("Downloading");
                mBuilder.setProgress(100, progress, false);
                updateNotification();

                sendBroadCast(mAppInfo);

                mLastTime = currentTime;
            }
        }

        @Override
        public void onCompleted() {
            L.i(TAG, "onCompleted()");
            mBuilder.setContentText("Download Complete");
            mBuilder.setProgress(0, 0, false);
            mBuilder.setTicker(mAppInfo.getName() + " download Completed");
            updateNotification();
            mAppInfo.setStatus(AppInfo.STATUS_COMPLETE);
            mAppInfo.setProgress(100);
            DataSource.getInstance().removeData(mBuilder.mContext, mAppInfo.getUrl());
            sendBroadCast(mAppInfo);
        }

        @Override
        public void onDownloadPaused() {
            L.i(TAG, "onDownloadPaused()");
            mBuilder.setContentText("Download Paused");
            mBuilder.setTicker(mAppInfo.getName() + " download Paused");
            mBuilder.setProgress(100, mAppInfo.getProgress(), false);
            mBuilder.mActions.clear();

//            mBuilder.addAction(R.drawable.ic_close_red_24dp, CustomReceiver.CANCEL,
//                    getAction(mBuilder.mContext, mPosition, mAppInfo, CustomReceiver.CANCEL));
//            mBuilder.addAction(R.drawable.ic_close_red_24dp,
//                    CustomReceiver.RESUME, getAction(mBuilder.mContext, mPosition, mAppInfo, CustomReceiver.RESUME));
            updateNotification();

            mAppInfo.setStatus(AppInfo.STATUS_PAUSED);
            sendBroadCast(mAppInfo);
        }

        @Override
        public void onDownloadCanceled() {
            L.i(TAG, "onDownloadCanceled()");
            mBuilder.setContentText("Download Canceled");
            mBuilder.setTicker(mAppInfo.getName() + " download Canceled");
            updateNotification();

            //there is 1000 ms memory leak, shouldn't be a problem
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNotificationManager.cancel(mPosition + 1000);
                }
            }, 1000);

            mAppInfo.setStatus(AppInfo.STATUS_NOT_DOWNLOAD);
            mAppInfo.setProgress(0);
            mAppInfo.setDownloadPerSize("");
            sendBroadCast(mAppInfo);
        }

        @Override
        public void onFailed(DownloadException e) {
            L.i(TAG, "onFailed()");
            e.printStackTrace();
            mBuilder.setContentText("Download Failed");
            mBuilder.setTicker(mAppInfo.getName() + " download failed");
            mBuilder.setProgress(100, mAppInfo.getProgress(), false);
            updateNotification();

            mAppInfo.setStatus(AppInfo.STATUS_DOWNLOAD_ERROR);
            sendBroadCast(mAppInfo);
        }

        private void updateNotification() {
            mNotificationManager.notify(mPosition + 1000, mBuilder.build());
        }

        private void cancelNotification() {
            mNotificationManager.cancel(mPosition + 1000);
        }

        private void sendBroadCast(AppInfo appInfo) {
            Intent intent = new Intent();
            intent.setAction(DownloadService.ACTION_DOWNLOAD_BROAD_CAST);
            intent.putExtra(EXTRA_POSITION, mPosition);
            intent.putExtra(EXTRA_APP_INFO, appInfo);
            mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    public static final String CHANNEL_ID = "channel-1";

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = DownloadManager.getInstance();
        mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
        String channelName = "Notification";
        int importance = NotificationManager.IMPORTANCE_LOW;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    CHANNEL_ID, channelName, importance);
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(null);
            NotificationManager manager = (NotificationManager) (getSystemService(Context.NOTIFICATION_SERVICE));
            manager.createNotificationChannel(mChannel);
        }
        //mDownloadDir = new File(AppUtils.getFolderPath(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloadManager.pauseAll();
    }


}
