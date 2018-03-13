package is.yranac.canary.widget;


import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.adapter.SettingsAdapter;
import is.yranac.canary.model.SettingsObject;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.util.Log;


/**
 * Created by Schroeder on 3/4/16.
 */

@SuppressLint("Registered")
public class ConfigurationActivity extends Activity implements AdapterView.OnItemClickListener {


    private List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_config_activity);
        setResult(RESULT_CANCELED);

        locations = LocationDatabaseService.getLocationList();

        List<SettingsObject> settingsObjects = new ArrayList<>();
        for (Location location : locations) {

            SettingsObject object = SettingsObject.promptWithTitle(location.name, location.address);
            settingsObjects.add(object);
        }

        SettingsAdapter adapter = new SettingsAdapter(this, settingsObjects, null);

        ListView listView = (ListView) findViewById(R.id.list_view);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    int mAppWidgetId;


    private void showAppWidget(Location location) {


        mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID);


            Intent startService = new Intent(ConfigurationActivity.this,
                    MyWidgetProvider.class);
            startService.putExtra(EXTRA_APPWIDGET_ID, mAppWidgetId);
            startService.putExtra("location", location.id);
            setResult(RESULT_OK, startService);
            startService(startService);


            finish();
        }
        if (mAppWidgetId == INVALID_APPWIDGET_ID) {
            Log.i("I am invalid", "I am invalid");
            finish();
        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Location location = locations.get(position);
        showAppWidget(location);
    }
}