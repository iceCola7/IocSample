package com.cxz.iocsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cxz.icolibrary.InjectManager;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract void initView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注册
        InjectManager.inject(this);

        initView();

    }
}
