package com.rishabh.eventplanner;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView.LayoutParams;

public class ResizeAnimation extends Animation {
	private View mView;
	private float mToHeight;
	private float mFromHeight;

	private float mToWidth;
	private float mFromWidth;

	private ListAdapter mListAdapter;
	public ResizeAnimation(ListAdapter listAdapter, ListItem listItem,
			float fromWidth, float fromHeight, float toWidth, float toHeight) {
		mToHeight = toHeight;
		mToWidth = toWidth;
		mFromHeight = fromHeight;
		mFromWidth = fromWidth;
		mView = listItem.getHolder().getTextViewWrap();
		mListAdapter = listAdapter;
		setDuration(200);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float height = (mToHeight - mFromHeight) * interpolatedTime
				+ mFromHeight;
		float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
		LayoutParams p = (LayoutParams) mView.getLayoutParams();
		p.height = (int) height;
		p.width = (int) width;
		mListAdapter.notifyDataSetChanged();
	}
}
