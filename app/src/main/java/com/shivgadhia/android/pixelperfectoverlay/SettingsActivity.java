package com.shivgadhia.android.pixelperfectoverlay;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static com.shivgadhia.android.pixelperfectoverlay.ScreenshotOverlayService.EXTRA_SCREENSHOT_URI;


public class SettingsActivity extends PreferenceActivity {

    private static final int REQUEST_CODE = R.id.request_code_file_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);

        setupFilePickerPreference();
        setupDeviceMetricsDisplay();
    }

    private void setupDeviceMetricsDisplay() {
        Preference deviceMetrics = findPreference("deviceMetrics");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        String summary = getDeviceMetricsString(wm);
        deviceMetrics.setSummary(summary);
    }

    private String getDeviceMetricsString(WindowManager wm) {
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        return "W: " + metrics.widthPixels + "px H: " + metrics.heightPixels + "px";
    }

    private void setupFilePickerPreference() {
        Preference filePicker = findPreference("filePicker");
        filePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                startActivityForResult(chooser, REQUEST_CODE);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        stopService(new Intent(this, ScreenshotOverlayService.class));

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            Uri imageUri = data.getData();
            Intent intent = new Intent(this, ScreenshotOverlayService.class);
            intent.putExtra(EXTRA_SCREENSHOT_URI, imageUri);
            startService(intent);
            finish();
        }
    }
}
