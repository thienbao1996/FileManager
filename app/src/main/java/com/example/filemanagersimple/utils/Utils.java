package com.example.filemanagersimple.utils;

import android.widget.Switch;

import com.example.filemanagersimple.R;

public class Utils {
    public static int getFileIcon(String extension) {
        switch (extension) {
            case "doc":
                return R.drawable.ic_file_doc;
            case "docx":
                return R.drawable.ic_file_doc;
            case "html":
                return R.drawable.ic_file_html;
            case "jpg":
                return R.drawable.ic_file_jpg;
            case "jpeg":
                return R.drawable.ic_file_jpg;
            case "json":
                return R.drawable.ic_file_json;
            case "mp3":
                return R.drawable.ic_file_mp3;
            case "mp4":
                return R.drawable.ic_file_mp4;
            case "pdf":
                return R.drawable.ic_file_pdf;
            case "sql":
                return R.drawable.ic_file_sql;
            case "txt":
                return R.drawable.ic_file_txt;
            case "xls":
                return R.drawable.ic_file_xls;
            case "xml":
                return R.drawable.ic_file_xml;
            case "zip":
                return R.drawable.ic_file_zip;
            default:
                return R.drawable.ic_file_generic;
        }
    }
}
