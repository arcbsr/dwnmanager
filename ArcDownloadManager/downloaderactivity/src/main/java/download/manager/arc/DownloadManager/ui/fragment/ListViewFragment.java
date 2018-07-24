package download.manager.arc.DownloadManager.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadManager;

import java.io.File;
import java.util.List;

import download.manager.arc.DownloadManager.DataSource;
import download.manager.arc.DownloadManager.entity.AppInfo;
import download.manager.arc.DownloadManager.listener.OnItemClickListener;
import download.manager.arc.DownloadManager.service.DownloadService;
import download.manager.arc.DownloadManager.ui.adapter.ListViewAdapter;
import download.manager.arc.DownloadManager.util.Utils;
import download.manager.arc.downloader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment implements OnItemClickListener<AppInfo> {


    ListView listView;

    private List<AppInfo> mAppInfos;
    private ListViewAdapter mAdapter;

    //private File mDownloadDir;

    private DownloadReceiver mReceiver;

    public ListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mDownloadDir = new File(AppUtils.getFolderPath(getContext()));
        mAdapter = new ListViewAdapter();
        mAdapter.setOnItemClickListener(this);
        mAppInfos = DataSource.getInstance().getData(getContext());
        for (AppInfo info : mAppInfos) {
            DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(info.getUrl());
            if (downloadInfo != null) {
                info.setProgress(downloadInfo.getProgress());
                info.setDownloadPerSize(Utils.getDownloadPerSize(downloadInfo.getFinished(), downloadInfo.getLength()));
                info.setStatus(AppInfo.STATUS_PAUSED);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        listView = view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setAdapter(mAdapter);
        mAdapter.setData(mAppInfos);
    }

    @Override
    public void onResume() {
        super.onResume();
        register();
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegister();
    }

    @Override
    public void onItemClick(View v, final int position, final AppInfo appInfo) {
        if (appInfo.getStatus() == AppInfo.STATUS_DOWNLOADING || appInfo.getStatus() == AppInfo.STATUS_CONNECTING) {
            pause(appInfo.getUrl());
        } else if (appInfo.getStatus() == AppInfo.STATUS_COMPLETE) {
            install(appInfo);
        } else if (appInfo.getStatus() == AppInfo.STATUS_INSTALLED) {
            //unInstall(appInfo);
        } else if (appInfo.getStatus() == AppInfo.STATUS_DELETE) {
            if (DownloadManager.getInstance().isRunning(appInfo.getUrl())) {
                Toast.makeText(getContext(), "Download Process Running...", Toast.LENGTH_SHORT).show();
                return;
            }
            DataSource.getInstance().removeData(getContext(), appInfo.getUrl());
            delete(appInfo.getUrl(), position);
        } else {
            download(position, appInfo.getUrl(), appInfo);
        }
    }

    private void delete(String tag, int position) {
        DownloadManager.getInstance().delete(tag);
        mAppInfos.remove(position);
        mAdapter.setData(mAppInfos);
        mAdapter.notifyDataSetChanged();

    }

    private void register() {
        mReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_DOWNLOAD_BROAD_CAST);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter);
    }

    private void unRegister() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        }
    }

    private void download(int position, String tag, AppInfo info) {
        DownloadService.intentDownload(getActivity(), position, tag, info);
    }

    private void pause(String tag) {
        DownloadService.intentPause(getActivity(), tag);
    }

    private void pauseAll() {
        DownloadService.intentPauseAll(getActivity());
    }

    private void install(AppInfo appInfo) {
        // Utils.installApp(getActivity(), new File(mDownloadDir, appInfo.getName() + ".apk"));
        File file = new File(appInfo.getSdPathFolder(), appInfo.getName() + "." + appInfo.getExtension());
        //VLog.w("get info", file.getAbsolutePath().toString());
//        Intent myIntent = new Intent(Intent.ACTION_VIEW);
//        //myIntent.setData(Uri.fromFile(apk));
//        myIntent.setDataAndType(Uri.fromFile(apk), "video/*");
//        Intent j = Intent.createChooser(myIntent, "Choose an application to open with:");
//        startActivity(j);
//        AppUtils.startAppVideo(getContext(), file.getAbsolutePath());
    }

    private void unInstall(AppInfo appInfo) {
        Utils.unInstallApp(getActivity(), appInfo.getPackageName());
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action == null || !action.equals(DownloadService.ACTION_DOWNLOAD_BROAD_CAST)) {
                return;
            }
            final int position = intent.getIntExtra(DownloadService.EXTRA_POSITION, -1);
            final AppInfo tmpInfo = (AppInfo) intent.getSerializableExtra(DownloadService.EXTRA_APP_INFO);
            if (tmpInfo == null || position == -1) {
                return;
            }
            final AppInfo appInfo = mAppInfos.get(position);
            final int status = tmpInfo.getStatus();
            switch (status) {
                case AppInfo.STATUS_CONNECTING:
                    appInfo.setStatus(AppInfo.STATUS_CONNECTING);
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.tvImgDownload.setImageResource(R.drawable.ic_cast_connected_black_24dp);
                    }
                    break;

                case AppInfo.STATUS_DOWNLOADING:
                    appInfo.setStatus(AppInfo.STATUS_DOWNLOADING);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
                        holder.progressBar.setProgress(appInfo.getProgress());
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.tvImgDownload.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    }
                    break;
                case AppInfo.STATUS_COMPLETE:
                    appInfo.setStatus(AppInfo.STATUS_COMPLETE);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());

                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
                        holder.progressBar.setProgress(appInfo.getProgress());
                        holder.tvImgDownload.setImageResource(R.drawable.ic_open_in_new_black_24dp);
                    }
                    break;

                case AppInfo.STATUS_PAUSED:
                    appInfo.setStatus(AppInfo.STATUS_PAUSED);
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.tvImgDownload.setImageResource(R.drawable.ic_cloud_download_black_24dp);
                    }
                    break;
                case AppInfo.STATUS_NOT_DOWNLOAD:
                    appInfo.setStatus(AppInfo.STATUS_NOT_DOWNLOAD);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.progressBar.setProgress(appInfo.getProgress());
                        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
                        holder.tvImgDownload.setImageResource(R.drawable.ic_open_in_new_black_24dp);
                    }
                    break;
                case AppInfo.STATUS_DOWNLOAD_ERROR:
                    appInfo.setStatus(AppInfo.STATUS_DOWNLOAD_ERROR);
                    appInfo.setDownloadPerSize("");
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.tvDownloadPerSize.setText("");
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.tvImgDownload.setImageResource(R.drawable.ic_error_black_24dp);
                    }
                    break;
            }
        }
    }

    private boolean isCurrentListViewItemVisible(int position) {
        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    private ListViewAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - listView.getFirstVisiblePosition();
        View view = listView.getChildAt(childPosition);
        return (ListViewAdapter.ViewHolder) view.getTag();
    }

}
