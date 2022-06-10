package com.example.filemanagersimple.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.filemanagersimple.R;
import com.example.filemanagersimple.utils.ViewType;
import com.example.filemanagersimple.utils.Utils;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<File> filteredFile;
    private FileItemEventListener callbackItem;
    private ViewType viewType;

    public FileAdapter(List<File> files, FileItemEventListener callbackItem) {
        filteredFile = files;
        this.callbackItem = callbackItem;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                viewType == ViewType.LIST.value ? R.layout.item_files : R.layout.item_files_grid,
                parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.onBind(filteredFile.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFile.size();
    }

    protected class FileViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView fileName, fileDate, fileSize;
        private final ImageView fileIcon;

        public FileViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.txt_nameFile);
            fileIcon = itemView.findViewById(R.id.img_itemFiles_main);
            fileDate = itemView.findViewById(R.id.txt_dateFile);
            fileSize = itemView.findViewById(R.id.txt_sizeFile);
        }

        void onBind(File file) {
            fileName.setText(file.getName());
            Date lastModify = new Date(file.lastModified());
            SimpleDateFormat simpleFormat = new SimpleDateFormat("MMM dd, yyyy  hh:mm");
            fileDate.setText(simpleFormat.format(lastModify));

            File[] files = file.listFiles();

            if (file.isDirectory()) {
                if (files != null) {
                    fileSize.setText(files.length > 1 ? files.length + " items" :  files.length + " item");
                } else {
                    fileSize.setText("0 item");
                }

                Glide.with(itemView).load(R.drawable.ic_folder_vector).centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade()).into(fileIcon);
            } else {
                fileSize.setText("");

                Glide.with(itemView).load(Utils.getFileIcon(file.getName().substring(file.getName().lastIndexOf(".") + 1)))
                        .transition(DrawableTransitionOptions.withCrossFade()).into(fileIcon);
            }

            itemView.setOnClickListener(view -> callbackItem.onFileItemClick(file));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.viewType.value;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    public void sortBy(String sortType) {
        switch (sortType) {
            case "name":
                Collections.sort(filteredFile, (file1, file2) -> file1.getName().compareTo(file2.getName()));
                break;
            case "date":
                Collections.sort(filteredFile, (file1, file2) -> new Date(file1.lastModified()).compareTo(new Date(file2.lastModified())));
                break;
            case "size":
                //Collections.sort(filteredFile, (file1, file2) -> (int) (file1.length() - file2.length()));
                Collections.sort(filteredFile, (file1, file2) -> (file2.listFiles() != null ? file2.listFiles().length : -1) -
                        (file1.listFiles() != null ? file1.listFiles().length : -1));
                break;
            case "type":
                Collections.sort(filteredFile, (file1, file2) -> {
                    if (file1.isDirectory() && file2.isDirectory()) {
                        return (file2.listFiles() != null ? file2.listFiles().length : -1) -
                                (file1.listFiles() != null ? file1.listFiles().length : -1);
                    } else if (file1.isDirectory() && !file2.isDirectory()) {
                        return -1;
                    } else if (!file1.isDirectory() && file2.isDirectory()) {
                        return 1;
                    } else {
                        return file1.getName().substring(file1.getName().lastIndexOf(".") + 1)
                                .compareTo(file2.getName().substring(file2.getName().lastIndexOf(".") + 1));
                    }
                });
                break;
        }
        notifyDataSetChanged();
    }

    public void updateListFile(List<File> list) {
        this.filteredFile = list;
        notifyDataSetChanged();
    }

    public interface FileItemEventListener {
        void onFileItemClick(File file);
    }
}
