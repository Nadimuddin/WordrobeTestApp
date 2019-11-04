package com.example.wardrobetestapp.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.wardrobetestapp.R;
import com.example.wardrobetestapp.model.Outfit;

/**
 * Created by Nadimuddin on 4/11/19.
 */
public class OutfitFragment extends Fragment {

    private LinearLayout mParentLayout;
    private ImageView mOutFitImage;

    private Outfit mOutfit;

    public static OutfitFragment getInstance(Outfit outfit) {
        OutfitFragment fragment = new OutfitFragment();
        fragment.mOutfit = outfit;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outfit, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mParentLayout = view.findViewById(R.id.parent_layout);
        mOutFitImage = view.findViewById(R.id.outfit_image);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if(mOutfit.isTop())
            layoutParams.setMargins(50, 50, 50, 0);
        else
            layoutParams.setMargins(50, 0, 50, 50);
        mOutFitImage.setLayoutParams(layoutParams);
        String path = mOutfit.getPath();
        Glide.with(getActivity()).load(path).into(mOutFitImage);
    }
}
