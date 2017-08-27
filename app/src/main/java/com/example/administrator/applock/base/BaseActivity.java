package com.example.administrator.applock.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.applock.LockApplication;
import com.example.administrator.applock.R;
import com.example.administrator.applock.utils.SystemBarHelper;

/**
 * Created by Administrator on 2017/8/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mCustomTitleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LockApplication.getInstance().doForCreate(this);
        setContentView(getLayoutId());
        initViews(savedInstanceState);
        initToolbar();
        inintData();
        initAction();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar != null){
            //Toolbar 作为 ActionBar 处理
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("");
            SystemBarHelper.immersiveStatusBar(this);
            SystemBarHelper.setHeightAndPadding(this,mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            //设置自定义 View
            resetToolbar();
            //使自定义的 View 在 Toolbar 显示
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            //设置 Toolbar 阴影为 0
            getSupportActionBar().setElevation(0);
        }

    }

    /**
     * Toolbar 上显示自定义 View
     */
    private void resetToolbar() {
        if(mCustomTitleTextView == null){
            mCustomTitleTextView = (TextView)getLayoutInflater().inflate(R.layout.layout_toolbar_title,null);
        }
        getSupportActionBar().setCustomView(mCustomTitleTextView,new ActionBar.LayoutParams(Gravity.CENTER));
        if(getTitle() != null){
//            mCustomTitleTextView = (TextView)getSupportActionBar().getCustomView();
            mCustomTitleTextView.setText(getTitle());
        }
    }

    /**
     * 隐藏 Toolbar
     */
    public void hiddenActionBar(){
        getSupportActionBar().hide();
    }

    protected abstract void initAction();

    protected abstract void inintData();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LockApplication.getInstance().doForFinish(this);
    }
}
