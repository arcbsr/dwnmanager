//package download.manager.arc.DownloadManager.ui.adapter;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import download.manager.arc.DownloadManager.entity.AppInfo;
//import download.manager.arc.DownloadManager.listener.OnItemClickListener;
//import download.manager.arc.downloader.R;
//
///**
// * Created by Aspsine on 2015/7/8.
// */
//public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private List<AppInfo> mAppInfos;
//
//    private OnItemClickListener mListener;
//
//    public RecyclerViewAdapter() {
//        this.mAppInfos = new ArrayList<AppInfo>();
//    }
//
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mListener = listener;
//    }
//
//    public void setData(List<AppInfo> appInfos) {
//        this.mAppInfos.clear();
//        this.mAppInfos.addAll(appInfos);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
//        final AppViewHolder holder = new AppViewHolder(itemView);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(v.getContext(), AppDetailActivity.class);
////                intent.putExtra("EXTRA_APPINFO", mAppInfos.get(holder.getLayoutPosition()));
////                v.getContext().startActivity(intent);
//            }
//        });
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        bindData((AppViewHolder) holder, position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mAppInfos.size();
//    }
//
//    private void bindData(AppViewHolder holder, final int position) {
//        final AppInfo appInfo = mAppInfos.get(position);
//        holder.tvName.setText(appInfo.getName());
//        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
//        holder.tvStatus.setText(appInfo.getStatusText());
//        holder.progressBar.setProgress(appInfo.getProgress());
//        holder.btnDownload.setText(appInfo.getButtonText());
//        Picasso.with(holder.itemView.getContext()).load(appInfo.getImage()).into(holder.ivIcon);
//        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.onItemClick(v, position, appInfo);
//                }
//            }
//        });
//        holder.itemRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    appInfo.setStatus(AppInfo.STATUS_DELETE);
//                    mListener.onItemClick(v, position, appInfo);
//                }
//            }
//        });
//    }
//
//    public static final class AppViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.ivIcon)
//        public ImageView ivIcon;
//
//        @BindView(R.id.tvName)
//        public TextView tvName;
//
//        @BindView(R.id.btnDownload)
//        public CoolButton btnDownload;
//
//        @BindView(R.id.tvDownloadPerSize)
//        public TextView tvDownloadPerSize;
//
//        @BindView(R.id.tvStatus)
//        public TextView tvStatus;
//
//        @BindView(R.id.progressBar)
//        public ProgressBar progressBar;
//        @BindView(R.id.item_rem)
//        public ImageView itemRemove;
//
//        public AppViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
//}
