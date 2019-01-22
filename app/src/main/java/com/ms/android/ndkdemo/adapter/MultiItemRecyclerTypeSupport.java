package com.ms.android.ndkdemo.adapter;

import android.view.View;

public interface MultiItemRecyclerTypeSupport<T> {

    View getLayoutView(int ItemType);

    void onBindViewMultiSupport(View holder, int itemViewType, int realPosition, T t);

    int getMultiItemViewType(int position);
}