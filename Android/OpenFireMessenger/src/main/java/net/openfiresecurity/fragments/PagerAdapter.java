/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.fragments;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class PagerAdapter extends FragmentStatePagerAdapter {

	private final List<Fragment> fragments;

	public PagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		fragments = list;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup view, int arg1) {
		// view.setTag("");
		return super.instantiateItem(view, arg1);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}