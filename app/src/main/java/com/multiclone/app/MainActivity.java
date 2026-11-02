package com.multiclone.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.widget.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25, 25, 25, 25);

        TextView title = new TextView(this);
        title.setText("MultiClone");
        title.setTextSize(28);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Daftar aplikasi di HP kamu");
        layout.addView(subtitle);

        ListView listView = new ListView(this);
        layout.addView(listView);

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps =
            pm.getInstalledApplications(0);

        ArrayList<ApplicationInfo> userApps =
            new ArrayList<>();

        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                userApps.add(app);
            }
        }

        Collections.sort(userApps, (a, b) ->
            pm.getApplicationLabel(a).toString()
            .compareToIgnoreCase(
                pm.getApplicationLabel(b).toString()
            )
        );

        ArrayList<String> names = new ArrayList<>();

        for (ApplicationInfo app : userApps) {
            names.add(pm.getApplicationLabel(app).toString());
        }

        listView.setAdapter(new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            names
        ));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String packageName = userApps.get(position).packageName;
            Intent launch = pm.getLaunchIntentForPackage(packageName);

            if (launch != null) {
                startActivity(launch);
            }
        });

        setContentView(layout);
    }
}
