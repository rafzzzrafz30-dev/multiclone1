package com.multiclone.app;
package com.multiclone.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    ArrayList<ApplicationInfo> userApps;
    PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pm = getPackageManager();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);
        layout.setBackgroundColor(Color.rgb(15, 20, 35));

        TextView title = new TextView(this);
        title.setText("MultiClone");
        title.setTextSize(30);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Kelola aplikasi favoritmu");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setPadding(0, 8, 0, 20);
        subtitle.setGravity(Gravity.CENTER);
        layout.addView(subtitle);

        ListView listView = new ListView(this);
        layout.addView(listView);

        List<ApplicationInfo> apps =
            pm.getInstalledApplications(0);

        userApps = new ArrayList<>();

        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0
                    && pm.getLaunchIntentForPackage(app.packageName) != null) {
                userApps.add(app);
            }
        }

        Collections.sort(userApps, (a, b) ->
            pm.getApplicationLabel(a).toString()
            .compareToIgnoreCase(
                pm.getApplicationLabel(b).toString()
            )
        );

        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return userApps.size();
            }

            @Override
            public Object getItem(int position) {
                return userApps.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {

                LinearLayout row = new LinearLayout(MainActivity.this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setPadding(12, 12, 12, 12);

                ImageView icon = new ImageView(MainActivity.this);
                icon.setImageDrawable(
                    pm.getApplicationIcon(userApps.get(position))
                );

                LinearLayout.LayoutParams iconParams =
                    new LinearLayout.LayoutParams(55, 55);
                row.addView(icon, iconParams);

                TextView name = new TextView(MainActivity.this);
                name.setText(
                    pm.getApplicationLabel(userApps.get(position)).toString()
                );
                name.setTextSize(17);
                name.setTextColor(Color.WHITE);
                name.setPadding(18, 0, 0, 0);

                row.addView(name);

                return row;
            }
        };

        listView.setAdapter(adapter);

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
