package download.manager.arc.DownloadManager.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import download.manager.arc.downloader.R;

public class DwnMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwn_main);
        //startActivity(new Intent(this, DwnMainActivity.class));
//        FileInfo url = new FileInfo("https://k003.kiwi6.com/hotlink/unx4bsw08k/Kuasha_23_July_2018_23-07-2018_-_.mp3",
//                "mp3", DownloadBind.getFolderPath(DwnMainActivity.this),
//                "bappy", "http://79.143.190.131:83/base/arcadio/browser/app1/tutorial_2.png");
//        DownloadBind.downloadQueeInit(DwnMainActivity.this, url);
//        FileInfo url2 = new FileInfo("https://k003.kiwi6.com/hotlink/88km02o6me/Kuasha_16_July_2018_16-07-2018_-_.mp3",
//                "mp3", DownloadBind.getFolderPath(DwnMainActivity.this),
//                "bappy2", "http://79.143.190.131:83/base/arcadio/browser/app1/tutorial_2.png");
//        DownloadBind.downloadQueeInit(DwnMainActivity.this, url2);
    }
}
