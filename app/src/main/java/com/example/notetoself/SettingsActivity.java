package com.example.notetoself;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;
	private boolean mShownDividers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		mPrefs = getSharedPreferences("Note to self", MODE_PRIVATE);
		mEditor = mPrefs.edit();

		mShownDividers = mPrefs.getBoolean("dividers", true);
		Switch switch1 = findViewById(R.id.switch1);

		switch1.setChecked(mShownDividers);

		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				if (isChecked) {
					mEditor.putBoolean("dividers", true);
					mShownDividers = true;
				}
				else {
					mEditor.putBoolean("dividers", false);
					mShownDividers = false;
				}

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();

		mEditor.commit();
	}
}