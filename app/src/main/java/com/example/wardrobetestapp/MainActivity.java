package com.example.wardrobetestapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wardrobetestapp.adapter.OutfitAdapter;
import com.example.wardrobetestapp.model.Outfit;
import com.example.wardrobetestapp.util.DBUtil;
import com.example.wardrobetestapp.util.PermissionUtil;
import com.example.wardrobetestapp.util.StorageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager mTopViewPager;
    ViewPager mBottomViewPager;
    ImageView mAddNewTopIcon;
    ImageView mAddNewBottomIcon;
    ImageView mAddTopIcon;
    ImageView mAddBottomIcon;
    ImageView mShuffleIcon;
    ImageView mFavouriteIcon;
    OutfitAdapter mTopAdapter;
    OutfitAdapter mBottomAdapter;

    DBUtil mDBUtil;
    List<Outfit> mTopOutfitList = new ArrayList<>();
    List<Outfit> mBottomOutfitList = new ArrayList<>();
    private int mTopSelected;
    private int mBottomSelected;
    Random mRandom = new Random();

    private static final int CAMERA_PERMISSION_CODE = 3456;
    private static final int CAMERA_REQUEST_TOP = 7634;
    private static final int CAMERA_REQUEST_BOTTOM = 7635;

    public enum OutfitType {
        TOP,
        BOTTOM
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        showOutfits(getDBUtil().getAllOutfit(true), true);
        showOutfits(getDBUtil().getAllOutfit(false), false);

        if(mTopOutfitList.size() > 0 && mBottomOutfitList.size() > 0) {
            if (getDBUtil().isFavourite(mTopOutfitList.get(mTopSelected).getId(), mBottomOutfitList.get(mBottomSelected).getId()))
                mFavouriteIcon.setBackground(getResources().getDrawable(R.drawable.circle_favourite_enable));
            else
                mFavouriteIcon.setBackground(getResources().getDrawable(R.drawable.circle_favourite_disable));
            mTopSelected = mTopOutfitList.size() - 1;
            mBottomSelected = mBottomOutfitList.size() - 1;
        }

        mTopViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("Top page selected: " + position);
                mTopSelected = position;
                if(getDBUtil().isFavourite(mTopOutfitList.get(mTopSelected).getId(), mBottomOutfitList.get(mBottomSelected).getId()))
                    mFavouriteIcon.setBackground(getResources().getDrawable(R.drawable.circle_favourite_enable));
                else
                    mFavouriteIcon.setBackground(getResources().getDrawable(R.drawable.circle_favourite_disable));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("Bottom page selected: " + position);
                mBottomSelected = position;
                if(getDBUtil().isFavourite(mTopOutfitList.get(mTopSelected).getId(), mBottomOutfitList.get(mBottomSelected).getId()))
                    mFavouriteIcon.setBackground(getResources().getDrawable(R.drawable.circle_favourite_enable));
                else
                    mFavouriteIcon.setBackground(getResources().getDrawable(R.drawable.circle_favourite_disable));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        mTopViewPager = findViewById(R.id.top_view_pager);
        mBottomViewPager = findViewById(R.id.bottom_view_pager);
        mAddNewTopIcon = findViewById(R.id.add_new_top_icon);
        mAddNewBottomIcon = findViewById(R.id.add_new_bottom_icon);
        mAddTopIcon = findViewById(R.id.add_top_icon);
        mAddBottomIcon = findViewById(R.id.add_bottom_icon);
        mShuffleIcon = findViewById(R.id.shuffle_icon);
        mFavouriteIcon = findViewById(R.id.favourite_icon);

        mAddNewTopIcon.setOnClickListener(this);
        mAddNewBottomIcon.setOnClickListener(this);
        mAddTopIcon.setOnClickListener(this);
        mAddBottomIcon.setOnClickListener(this);
        mShuffleIcon.setOnClickListener(this);
        mFavouriteIcon.setOnClickListener(this);
    }

    private int getTopRandom() {
        int top = 0;
        if(mTopOutfitList.size() > 1) {
            do {
                top = mRandom.nextInt(mTopOutfitList.size());
            } while (top == mTopSelected);
        }
        return top;
    }

    private int getBottomRandom() {
        int bottom = 0;
        if(mBottomOutfitList.size() > 1) {
            do {
                bottom = mRandom.nextInt(mBottomOutfitList.size());
            } while (bottom == mBottomSelected);
        }
        return bottom;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_top_icon: case R.id.add_top_icon:
                openCamera(CAMERA_REQUEST_TOP);
                break;

            case R.id.add_new_bottom_icon: case R.id.add_bottom_icon:
                openCamera(CAMERA_REQUEST_BOTTOM);
                break;

            case R.id.shuffle_icon:
                int top = getTopRandom();
                int bottom = getBottomRandom();
                mTopViewPager.setCurrentItem(top);
                mBottomViewPager.setCurrentItem(bottom);
                break;

            case R.id.favourite_icon:
                if(mTopOutfitList.size() < 1 || mBottomOutfitList.size() < 1)
                    break;

                if(!getDBUtil().isFavourite(mTopOutfitList.get(mTopSelected).getId(), mBottomOutfitList.get(mBottomSelected).getId())) {
                    getDBUtil().insertFavourite(mTopOutfitList.get(mTopSelected).getId(), mBottomOutfitList.get(mBottomSelected).getId());
                    mFavouriteIcon.setBackground(getResources().getDrawable(R.drawable.circle_favourite_enable));
                } else {
                    getDBUtil().deleteFavourite(mTopSelected, mBottomSelected);
                    mFavouriteIcon.setBackground(getResources().getDrawable(R.drawable.circle_favourite_disable));
                }
                break;
        }
    }

    private void openCamera(int cameraRequestCode) {
        if(PermissionUtil.getInstance(this).isPermissionGranted(Manifest.permission.CAMERA) && PermissionUtil.getInstance(this).isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + File.separator + "TempImage.png";
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, cameraRequestCode);
        } else {
            if(!PermissionUtil.getInstance(this).isPermissionGranted(Manifest.permission.CAMERA))
                PermissionUtil.getInstance(this).requestPermission(this, CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
            else if(!PermissionUtil.getInstance(this).isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                PermissionUtil.getInstance(this).requestPermission(this, CAMERA_PERMISSION_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void showOutfits(List<Outfit> outfitList, boolean isTop) {
        if(outfitList == null || outfitList.size() < 1)
            return;
        if(isTop) {
            mTopOutfitList = outfitList;
            mAddNewTopIcon.setVisibility(View.GONE);
            mAddTopIcon.setVisibility(View.VISIBLE);
            if(mTopAdapter == null) {
                mTopAdapter = new OutfitAdapter(getSupportFragmentManager(), outfitList);
                mTopViewPager.setAdapter(mTopAdapter);
            } else {
                mTopAdapter.setData(outfitList);
            }
            mTopViewPager.setCurrentItem(outfitList.size() - 1);
        } else {
            mBottomOutfitList = outfitList;
            mAddNewBottomIcon.setVisibility(View.GONE);
            mAddBottomIcon.setVisibility(View.VISIBLE);
            if(mBottomAdapter == null) {
                mBottomAdapter = new OutfitAdapter(getSupportFragmentManager(), outfitList);
                mBottomViewPager.setAdapter(mBottomAdapter);
            } else {
                mBottomAdapter.setData(outfitList);
            }
            mBottomViewPager.setCurrentItem(outfitList.size() - 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == CAMERA_REQUEST_TOP || requestCode == CAMERA_REQUEST_BOTTOM) && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = StorageUtil.getImageUri(this, photo);
            String path = StorageUtil.getRealPathFromURI(this, uri);
            System.out.println("Path: "+path);
            boolean isTop = requestCode == CAMERA_REQUEST_TOP;
            getDBUtil().insertOutfit(path, isTop);

            List<Outfit> outfitList = getDBUtil().getAllOutfit(isTop);
            showOutfits(outfitList, isTop);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERMISSION_CODE) {
            boolean granted = true;
            for(int grantResult : grantResults) {
                if(grantResult != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if(granted) {
                openCamera(CAMERA_REQUEST_TOP);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private DBUtil getDBUtil() {
        if(mDBUtil == null)
            mDBUtil = new DBUtil(this);
        return mDBUtil;
    }
}
