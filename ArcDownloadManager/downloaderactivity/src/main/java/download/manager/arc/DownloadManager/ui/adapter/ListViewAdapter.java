package download.manager.arc.DownloadManager.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import download.manager.arc.DownloadManager.entity.AppInfo;
import download.manager.arc.DownloadManager.listener.OnItemClickListener;
import download.manager.arc.downloader.R;

/**
 * Created by Aspsine on 2015/7/8.
 */
public class ListViewAdapter extends BaseAdapter {

    private List<AppInfo> mAppInfos;

    private OnItemClickListener mListener;

    public ListViewAdapter() {
        this.mAppInfos = new ArrayList<AppInfo>();
    }

    public void setData(List<AppInfo> appInfos) {
        this.mAppInfos.clear();
        this.mAppInfos.addAll(appInfos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return mAppInfos.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return mAppInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloading_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AppInfo appInfo = mAppInfos.get(position);
        holder.tvName.setText(appInfo.getName());
        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
        holder.tvStatus.setText(appInfo.getStatusText());
        holder.progressBar.setProgress(appInfo.getProgress());
        holder.btnDownload.setText(appInfo.getButtonText());
        if (appInfo.getImage().length() != 0)
            Picasso.with(parent.getContext()).load(appInfo.getImage()).into(holder.ivIcon);
        holder.tvImgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, position, appInfo);
                }
            }
        });
        holder.itemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    appInfo.setStatus(AppInfo.STATUS_DELETE);
                    mListener.onItemClick(v, position, appInfo);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    public final static class ViewHolder {

        //        @BindView(R.id.ivIcon)
        public ImageView ivIcon;
        //
//        @BindView(R.id.tvName)
        public TextView tvName;
        //
//        @BindView(R.id.item_img_download)
        public ImageView tvImgDownload;
        //        @BindView(R.id.btnDownload)
        public Button btnDownload;
        //
//        @BindView(R.id.tvDownloadPerSize)
        public TextView tvDownloadPerSize;
        //        @BindView(R.id.item_rem)
        public ImageView itemRemove;
        //        @BindView(R.id.tvStatus)
        public TextView tvStatus;
        //
//        @BindView(R.id.progressBar)
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvImgDownload = itemView.findViewById(R.id.item_img_download);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            tvDownloadPerSize = itemView.findViewById(R.id.tvDownloadPerSize);
            itemRemove = itemView.findViewById(R.id.item_rem);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
