package com.example.filemanagersimple.utils;

public enum ViewType {
    LIST(0), GRID(1), THUMBNAIL(2);

    final public int value;

    ViewType(int value) {
        this.value = value;
    }
}
