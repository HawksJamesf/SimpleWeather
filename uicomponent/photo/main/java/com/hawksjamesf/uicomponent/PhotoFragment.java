package com.hawksjamesf.uicomponent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Nov/06/2018  Tue
 * <p>
 * bitmap，drawable、uri、resource id
 */
public class PhotoFragment extends Fragment {
    public static final String TAG = "PhotoFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_PAGE = "page";
    Page page;
    int sectionNumber = -1;
    ImageView ivPhoto;
    TextView tvText;

    public static PhotoFragment newInstance(int sectionNumber, Page page) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments == null) return;
        page = arguments.getParcelable(ARG_PAGE);
        sectionNumber = arguments.getInt(ARG_SECTION_NUMBER);
        ivPhoto = view.findViewById(R.id.iv_photo);
        tvText = view.findViewById(R.id.tv_text);
        if (page.thumbnailBitmap == null) {
            tvText.setText(String.valueOf(sectionNumber));
            tvText.setVisibility(View.VISIBLE);
            ivPhoto.setVisibility(View.GONE);
        } else {
            tvText.setVisibility(View.GONE);
            ivPhoto.setVisibility(View.VISIBLE);
            ivPhoto.setImageBitmap(page.thumbnailBitmap);

        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}