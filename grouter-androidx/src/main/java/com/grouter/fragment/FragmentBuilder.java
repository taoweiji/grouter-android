package com.grouter.fragment;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;

public class FragmentBuilder {
    private Class<? extends Fragment> clazz;
    private String tag;
    private Fragment fragment;
    private Bundle args;

    public FragmentBuilder(Class<? extends Fragment> clazz, String tag) {
        this.clazz = clazz;
        this.tag = tag;
    }

    @SuppressWarnings("unchecked")
    public FragmentBuilder(String className, String tag) {
        try {
            this.clazz = (Class<? extends Fragment>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.tag = tag;
    }


    public FragmentBuilder setArguments(@Nullable Bundle args) {
        this.args = args;
        return this;
    }

    public void addToView(FragmentManager fragmentManager, @IdRes int idRes) {
        fragmentManager.beginTransaction().add(idRes, getFragment(fragmentManager), tag).commit();
    }

    public Fragment getFragment(FragmentManager fragmentManager) {
        if (fragment != null) {
            return fragment;
        }
        fragment = newFragment(fragmentManager, tag);
        return fragment;
    }

    public Fragment newFragment(FragmentManager fragmentManager, String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (!TextUtils.isEmpty(tag) && fragment != null) {
            return fragment;
        }
        try {
            fragment = clazz.newInstance();
            if (args != null) {
                fragment.setArguments(args);
            }
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
