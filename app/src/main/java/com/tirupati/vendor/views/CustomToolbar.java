package com.tirupati.vendor.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.tirupati.vendor.databinding.CustomToolbarBinding;
import com.tirupati.vendor.helper.SessionManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CustomToolbar extends FrameLayout {
    CustomToolbarBinding binding;
    @Inject
    SessionManager sessionManager;

    public CustomToolbar(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = CustomToolbarBinding.inflate(inflater, this, true);

    }

    public CustomToolbarBinding getBinding() {
        return binding;
    }

    public void hideAll() {
//        binding.ivBack.setVisibility(GONE);
//        binding.ivMenu.setVisibility(GONE);
//        binding.tvHeaderTitle.setText("");
//        binding.rlSearch.setVisibility(VISIBLE);
//        binding.tvHeaderTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    public void setToolbarClickListener(OnClickListener toolbarClickListener) {

       /* binding.ivBack.setOnClickListener(toolbarClickListener);
        binding.ivLogo.setOnClickListener(toolbarClickListener);
        binding.ivMenu.setOnClickListener(toolbarClickListener);
        binding.ivWishList.setOnClickListener(toolbarClickListener);
        binding.tvSearch.setOnClickListener(toolbarClickListener);
        binding.tvCart.setOnClickListener(toolbarClickListener);*/

    }

    public void setConfig(ToolbarConfig toolbarConfig) {
        hideAll();
        setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(toolbarConfig.getTitle())) {
          /*  binding.tvHeaderTitle.setText(toolbarConfig.getTitle());
            binding.tvHeaderTitle.setVisibility(VISIBLE);*/
        } else {
//            binding.tvHeaderTitle.setVisibility(GONE);
        }
        if (toolbarConfig.isShowMenuBtn()) {
//            binding.ivMenu.setVisibility(View.VISIBLE);
        } else {
//            binding.ivMenu.setVisibility(View.GONE);
        }
        if (toolbarConfig.isShowBackBtn()) {
//            binding.ivBack.setVisibility(View.VISIBLE);
//            binding.ivLogo.setVisibility(View.GONE);

        } else {
//            binding.ivBack.setVisibility(View.GONE);
//            binding.ivLogo.setVisibility(View.VISIBLE);
        }
        if (toolbarConfig.isShowLogo()) {
//            binding.ivLogo.setVisibility(View.VISIBLE);
        } else {
//            binding.ivLogo.setVisibility(View.GONE);
        }
        if (toolbarConfig.isShowSearchView(true)) {
//            binding.rlSearch.setVisibility(View.VISIBLE);
        } else {
//            binding.rlSearch.setVisibility(View.GONE);
        }
        if (toolbarConfig.isHideSearchView()) {
//            binding.rlSearch.setVisibility(View.GONE);
        } else {
//            binding.rlSearch.setVisibility(View.VISIBLE);
        }


        if (toolbarConfig.isHideNotificationBtn()) {
//            binding.ivWishList.setVisibility(View.GONE);
        } else {
//            binding.ivWishList.setVisibility(View.VISIBLE);
        }
        if (toolbarConfig.istHideCartBtn()) {
//            binding.tvCart.setVisibility(View.GONE);
        } else {
//            binding.tvCart.setVisibility(View.VISIBLE);
        }
    }

}

