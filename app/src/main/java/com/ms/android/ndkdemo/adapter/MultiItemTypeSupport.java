package com.ms.android.ndkdemo.adapter;

public interface MultiItemTypeSupport<T>
{
	int getLayoutId(int position, T t);
	
	int getViewTypeCount();
	
	int getItemViewType(int position, T t);
}