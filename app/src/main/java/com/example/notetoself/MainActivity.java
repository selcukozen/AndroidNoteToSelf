package com.example.notetoself;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetoself.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//	Note mTempNote = new Note();
//	private List<Note> noteList = new ArrayList<Note>();
	private JSONSerializer mSerializer;
	private List<Note> noteList;
	private RecyclerView recyclerView;
	private NoteAdapter mAdapter;

	private AppBarConfiguration appBarConfiguration;
	private ActivityMainBinding binding;

	private boolean mShowDividers;
	private SharedPreferences mPrefs;

	@Override
	protected void onResume() {
		super.onResume();

		mPrefs = getSharedPreferences("Note to self", MODE_PRIVATE);

		mShowDividers = mPrefs.getBoolean("dividers", true);

		if (mShowDividers) {
			recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
		}
		else {
			if (recyclerView.getItemDecorationCount() > 0) {
				recyclerView.removeItemDecorationAt(0);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		setSupportActionBar(binding.toolbar);


	//	NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
	//	appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
	//	NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

		binding.fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogNewNote dialog = new DialogNewNote();
				dialog.show(getSupportFragmentManager(), "");
			}
		});

		mSerializer = new JSONSerializer("NoteToSelf.json", getApplicationContext());

		try {
			noteList = mSerializer.load();
		} catch (Exception e) {
			noteList = new ArrayList<Note> ();
			Log.e("Error loading notes:", "", e);
		}

		recyclerView = findViewById(R.id.recyclerView);
		mAdapter = new NoteAdapter(this, noteList);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(mAdapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveNotes();
	}

	public void saveNotes() {
		try {
			mSerializer.save(noteList);
		} catch (Exception e) {
			Log.e("Error Saving Notes", "", e);
		}
	}

	public void CreateNewNote(Note n){
		noteList.add(n);
		mAdapter.notifyDataSetChanged();
	}

	public void showNote(int noteToShow){
		DialogShowNote dialog = new DialogShowNote();

		dialog.sendNoteSelected((noteList.get(noteToShow)));
		dialog.show(getSupportFragmentManager(), "");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onSupportNavigateUp() {
		/*
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
		return NavigationUI.navigateUp(navController, appBarConfiguration)
						|| super.onSupportNavigateUp();

		 */
		return true;
	}
}