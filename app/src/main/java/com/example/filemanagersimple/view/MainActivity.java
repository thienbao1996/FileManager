package com.example.filemanagersimple.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;


import com.example.filemanagersimple.R;
import com.example.filemanagersimple.utils.ViewType;
import com.example.filemanagersimple.utils.CustomDialog;
import com.example.filemanagersimple.utils.StorageHelper;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private String path;
    private String pathBack;
    private ViewType viewType = ViewType.LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StorageHelper.grantStoragePermission(this);
        Log.d("path sd", Environment.getExternalStorageState());
        Log.d("path sd", Environment.getExternalStorageDirectory().getPath());
        Log.d("path sd", Environment.getRootDirectory().getPath());
        Log.d("path sd", Environment.getDataDirectory().getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("path sd", Environment.getStorageDirectory().getPath());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission_group.STORAGE}, 111);
        }

        getSharedPreferences("SharedPreference", MODE_PRIVATE);

        if (savedInstanceState != null) {
            path = savedInstanceState.getString("path");
            listFile(path);
        } else {
            this.getPreferences(MODE_PRIVATE).edit().putInt("ViewType", viewType.value).apply();
            this.getPreferences(MODE_PRIVATE).edit().putString("Sort", "name").apply();

            if ((StorageHelper.isExternalStorageReadable())) {
                File externalFilesDir = Environment.getExternalStorageDirectory();
                if (externalFilesDir != null) {
                    listFile(externalFilesDir.getPath());
                }
            }
        }

        ImageButton homeBtn = findViewById(R.id.btn_home);
        homeBtn.setOnClickListener(v -> {
            CustomDialog customDialog = new CustomDialog(this);
            customDialog.setCallBack(new CustomDialog.CustomDialogCallBack() {
                @Override
                public void moveToInternalStorage() {
                    File internalDir = getFilesDir();
                    listFile(internalDir.getPath());
                }

                @Override
                public void moveToExternalStorage() {
                    if (StorageHelper.isExternalStorageReadable()) {
                        File externalFilesDir = Environment.getExternalStorageDirectory();
                        if (externalFilesDir != null) {
                            listFile(externalFilesDir.getPath());
                        }
                    }
                }
            });
            customDialog.show();
        });


        MaterialButtonToggleGroup changeViewButton = findViewById(R.id.toggleGroup_main);
        changeViewButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            Fragment fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.frame_main_fragmentContainer);
            if (checkedId == R.id.btn_main_list && isChecked) {
                if (fragment instanceof FragmentFile) {
                    ((FragmentFile) fragment).setViewType(ViewType.LIST);
                    this.viewType = ViewType.LIST;
                }
            } else if (checkedId == R.id.btn_main_grid && isChecked) {
                if (fragment instanceof FragmentFile) {
                    ((FragmentFile) fragment).setViewType(ViewType.GRID);
                    this.viewType = ViewType.GRID;
                }
            }
            this.getPreferences(MODE_PRIVATE).edit().putInt("ViewType", viewType.value).apply();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", path);
    }

    public void listFile(String path) {
        this.path = path;
        subPath();

        MaterialTextView pathName = findViewById(R.id.txt_file_path);
        pathName.setText(new File(path).getName());

        FragmentFile fragmentFile = new FragmentFile();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);

        fragmentFile.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_fragmentContainer, fragmentFile)
                .commit();
    }

    private void subPath() {
        if (path.lastIndexOf("/") > 1) {
            pathBack = path.substring(0, path.lastIndexOf("/"));
        } else {
            pathBack = "./";
        }

        Log.d("pathBack", pathBack);
    }

    @Override
    public void onBackPressed() {
        /*if (pathBack.endsWith("/storage/")) {

        } else
            super.onBackPressed();*/
        listFile(pathBack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu, menu);

        /*SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);*/
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setMaxWidth( Integer.MAX_VALUE );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*Fragment fragment = getSupportFragmentManager()
                        .findFragmentById(R.id.frame_main_fragmentContainer);
                if (fragment instanceof FragmentFile) {
                    ((FragmentFile) fragment).filerBy(query);
                }*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentById(R.id.frame_main_fragmentContainer);
                if (fragment instanceof FragmentFile) {
                    ((FragmentFile) fragment).filerBy(newText);
                }

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.frame_main_fragmentContainer);

        switch (item.getItemId()) {
            case R.id.menuItem_date:
                this.getPreferences(MODE_PRIVATE).edit().putString("Sort", "date").apply();
                if (fragment instanceof FragmentFile) {
                    ((FragmentFile) fragment).sortBy("date");
                }
                return true;
            case R.id.menuItem_name:
                this.getPreferences(MODE_PRIVATE).edit().putString("Sort", "name").apply();
                if (fragment instanceof FragmentFile) {
                    ((FragmentFile) fragment).sortBy("name");
                }
                return true;
            case R.id.menuItem_size:
                this.getPreferences(MODE_PRIVATE).edit().putString("Sort", "size").apply();
                if (fragment instanceof FragmentFile) {
                    ((FragmentFile) fragment).sortBy("size");
                }
                return true;
            case R.id.menuItem_type:
                this.getPreferences(MODE_PRIVATE).edit().putString("Sort", "type").apply();
                if (fragment instanceof FragmentFile) {
                    ((FragmentFile) fragment).sortBy("type");
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}