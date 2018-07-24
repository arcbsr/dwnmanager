package download.manager.arc.DownloadManager.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import download.manager.arc.DownloadManager.ui.fragment.ListViewFragment;
import download.manager.arc.downloader.R;

public class AppListActivity extends AppCompatActivity {

    public static final class TYPE {
        public static final int TYPE_LISTVIEW = 0;
        public static final int TYPE_RECYCLERVIEW = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

//        Intent intent = getIntent();
//        int type = intent.getIntExtra("EXTRA_TYPE", TYPE.TYPE_LISTVIEW);

        if (savedInstanceState == null) {
            Fragment fragment = new ListViewFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Download Manager");
    }
}
