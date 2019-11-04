package com.example.wardrobetestapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.wardrobetestapp.fragment.OutfitFragment;
import com.example.wardrobetestapp.model.Outfit;

import java.util.List;

/**
 * Created by Nadimuddin on 4/11/19.
 */
public class OutfitAdapter extends FragmentStatePagerAdapter {
    private List<Outfit> mOutfitList;

    public OutfitAdapter(FragmentManager fm, List<Outfit> outfitList) {
        super(fm);
        mOutfitList = outfitList;
    }

    public void setData(List<Outfit> outfitList) {
        mOutfitList = outfitList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return OutfitFragment.getInstance(mOutfitList.get(position));
    }

    @Override
    public int getCount() {
        return mOutfitList.size();
    }
}
