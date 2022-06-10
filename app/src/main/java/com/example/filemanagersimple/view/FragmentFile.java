package com.example.filemanagersimple.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagersimple.adapter.FileAdapter;
import com.example.filemanagersimple.R;
import com.example.filemanagersimple.utils.ViewType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentFile extends Fragment implements FileAdapter.FileItemEventListener {
    private String path;
    private ViewType viewType;
    private FileAdapter fileAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<File> backupList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            path = bundle.getString("path", "");
        }

        viewType = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getInt("ViewType", 0) == 0 ? ViewType.LIST : ViewType.GRID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_file, container, false);

        RecyclerView recyclerViewFile = view.findViewById(R.id.rv_main);
        File currentFile = new File(path);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerViewFile.setLayoutManager(gridLayoutManager);

        File[] files = currentFile.listFiles();

        if (files != null) {
            List<File> list = Arrays.asList(files);
            backupList = list;

            fileAdapter = new FileAdapter(list, this);
            fileAdapter.sortBy(getActivity().getPreferences(Context.MODE_PRIVATE).getString("Sort", "name"));
            recyclerViewFile.setAdapter(fileAdapter);
            setViewType(viewType);
        }

        return view;
    }

    @Override
    public void onFileItemClick(File file) {
        if (file.isDirectory()) {
            ((MainActivity) getActivity()).listFile(file.getPath());
        }
    }

    public void setViewType(ViewType viewType) {
        if (fileAdapter != null) {
            fileAdapter.setViewType(viewType);
            gridLayoutManager.setSpanCount(viewType.equals(ViewType.LIST) ? 1 : 2);
        }
    }

    public void sortBy(String sortType) {
        if (fileAdapter != null) {
            fileAdapter.sortBy(sortType);
        }
    }

    public void filerBy(String content) {
        List<File> result = new ArrayList<>();
        if (TextUtils.isEmpty(content)) {
            result = backupList;
        } else {
            for (File file : backupList) {
                if (file.getName().contains(content)) {
                    result.add(file);
                }
            }
        }

        if (fileAdapter != null) {
            fileAdapter.updateListFile(result);
        }
    }
}
