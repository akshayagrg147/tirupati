package com.tirupati.vendor.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tirupati.vendor.databinding.LayoutOtpViewBinding;


public class CustomOTPView extends LinearLayout {
    private static final String TAG = "CustomOTPView";
    public LayoutOtpViewBinding binding;

    public CustomOTPView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomOTPView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomOTPView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = LayoutOtpViewBinding.inflate(inflater, this, true);
    }

    public String getOtp() {
        return binding.edt1.getText().toString() +
                binding.edt2.getText().toString() +
                binding.edt3.getText().toString() +
                binding.edt4.getText().toString() /*+
                binding.edt5.getText().toString() +
                binding.edt6.getText().toString()*/ ;
    }

    public void setOtp(String otp) {
        if (otp.length() >= 4) {
            binding.edt1.setText(String.valueOf(otp.charAt(0)));
            binding.edt2.setText(String.valueOf(otp.charAt(1)));
            binding.edt3.setText(String.valueOf(otp.charAt(2)));
            binding.edt4.setText(String.valueOf(otp.charAt(3)));
/*
            binding.edt5.setText(String.valueOf(otp.charAt(4)));
            binding.edt6.setText(String.valueOf(otp.charAt(5)));
*/
        }
    }
  public void setBlankOtp() {

            binding.edt1.setText("");
            binding.edt2.setText("");
            binding.edt3.setText("");
            binding.edt4.setText("");


    }

    public void setUpViews() {

        MyTextWatcher myTextWatcher1 = new MyTextWatcher(null, binding.edt2);
        binding.edt1.setTag(myTextWatcher1);

        MyTextWatcher myTextWatcher2 = new MyTextWatcher(binding.edt1, binding.edt3);
        binding.edt2.setTag(myTextWatcher2);

        MyTextWatcher myTextWatcher3 = new MyTextWatcher(binding.edt2, binding.edt4);
        binding.edt3.setTag(myTextWatcher3);

        MyTextWatcher myTextWatcher4 = new MyTextWatcher(binding.edt3, binding.edt5);
        binding.edt4.setTag(myTextWatcher4);

//        MyTextWatcher myTextWatcher5 = new MyTextWatcher(binding.edt4, binding.edt6);
//        binding.edt5.setTag(myTextWatcher5);
//
//        MyTextWatcher myTextWatcher6 = new MyTextWatcher(binding.edt5, null);
//        binding.edt6.setTag(myTextWatcher6);

        OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EditText editText = ((EditText) v);
                    editText.setSelection(editText.length());
                }
            }
        };


        OnKeyListener onKeyListener = new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //check if the right key was pressed
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        EditText editText = ((EditText) v);
                        if (editText.getText().toString().length() == 0) {
                            if (editText.getTag() != null) {
                                ((MyTextWatcher) editText.getTag()).afterStringChange("");
                            }
                            return false;
                        }
                    }
                }
                return false;
            }
        };

        binding.edt1.addTextChangedListener(myTextWatcher1);
        binding.edt2.addTextChangedListener(myTextWatcher2);
        binding.edt3.addTextChangedListener(myTextWatcher3);
        binding.edt4.addTextChangedListener(myTextWatcher4);
//        binding.edt5.addTextChangedListener(myTextWatcher5);
//        binding.edt6.addTextChangedListener(myTextWatcher6);

        binding.edt1.setOnFocusChangeListener(onFocusChangeListener);
        binding.edt2.setOnFocusChangeListener(onFocusChangeListener);
        binding.edt3.setOnFocusChangeListener(onFocusChangeListener);
        binding.edt4.setOnFocusChangeListener(onFocusChangeListener);
//        binding.edt5.setOnFocusChangeListener(onFocusChangeListener);
//        binding.edt6.setOnFocusChangeListener(onFocusChangeListener);

        binding.edt1.setOnKeyListener(onKeyListener);
        binding.edt2.setOnKeyListener(onKeyListener);
        binding.edt3.setOnKeyListener(onKeyListener);
        binding.edt4.setOnKeyListener(onKeyListener);
//        binding.edt5.setOnKeyListener(onKeyListener);
//        binding.edt6.setOnKeyListener(onKeyListener);

    }

    class MyTextWatcher implements TextWatcher {
        private EditText editPrev, editNext;

        MyTextWatcher(EditText editPrev, EditText editNext) {
            this.editPrev = editPrev;
            this.editNext = editNext;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            afterStringChange(s.toString());
        }

        public void afterStringChange(String s) {
            Log.d(TAG, "afterTextChanged() called with: s = [" + s + "]");
            changeBackgroundRes();
            if (s.length() == 1) {
                if (editNext != null) {
                    editNext.requestFocus();
                }
            } else {
                if (editPrev != null) {
                    editPrev.requestFocus();
                }
            }
        }
    }

    private void changeBackgroundRes() {

        if (TextUtils.isEmpty(binding.edt1.getText().toString())) {
          //  binding.edt1.setBackgroundResource(R.drawable.otp3_new);
        } else {
           // binding.edt1.setBackgroundResource(R.drawable.otp1);
        }

        if (TextUtils.isEmpty(binding.edt2.getText().toString())) {
          //  binding.edt2.setBackgroundResource(R.drawable.otp2);
        } else {
           // binding.edt2.setBackgroundResource(R.drawable.otp1);
        }

        if (TextUtils.isEmpty(binding.edt3.getText().toString())) {
           // binding.edt3.setBackgroundResource(R.drawable.otp2);
        } else {
          //  binding.edt3.setBackgroundResource(R.drawable.otp1);
        }

        if (TextUtils.isEmpty(binding.edt4.getText().toString())) {
          //  binding.edt4.setBackgroundResource(R.drawable.otp2);
        } else {
           // binding.edt4.setBackgroundResource(R.drawable.otp1);
        }

//        if (TextUtils.isEmpty(binding.edt5.getText().toString())) {
//            binding.edt5.setBackgroundResource(R.drawable.otp2);
//        } else {
//            binding.edt5.setBackgroundResource(R.drawable.otp1);
//        }
//
//        if (TextUtils.isEmpty(binding.edt6.getText().toString())) {
//            binding.edt6.setBackgroundResource(R.drawable.otp2);
//        } else {
//            binding.edt6.setBackgroundResource(R.drawable.otp1);
//        }
    }
}