package com.example.mytodo.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mytodo.FirstFragment.FirstTabFragment;
import com.example.mytodo.SecondFragment.SecondTabFragment;

public class TabAdapter extends FragmentStateAdapter {


    public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;

        switch (position){
            case 0:
                fragment = new FirstTabFragment();
                break;
            case 1:
                fragment = new SecondTabFragment();
                break;
            default:
                fragment = new FirstTabFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
