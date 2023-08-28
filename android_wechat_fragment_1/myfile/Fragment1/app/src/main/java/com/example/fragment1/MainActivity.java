package com.example.fragment1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private Fragment fmTabHome = new homeFragment();
    private Fragment fmTabAddr = new addrFragment();
    private Fragment fmTabFind = new findFragment();
    private Fragment fmTabSetting = new settingFragment();

    private FragmentManager fm;

    private LinearLayout mTabHome;
    private LinearLayout mTabAddr;
    private LinearLayout mTabFind;
    private LinearLayout mTabSetting;

    private ImageButton mImgHome;
    private ImageButton mImgAddr;
    private ImageButton mImgFind;
    private ImageButton mImgSetting;

    private TextView mTxtHome;
    private TextView mTxtAddr;
    private TextView mTxtFind;
    private TextView mTxtSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去标题，但没什么用...不如把styles.xml里的AppTheme改了
        setContentView(R.layout.activity_main);
        initFragment();
        initView();
        initEvent();
        setSelect(0);
    }

    private void initFragment() {
        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.id_content, fmTabHome);
        transaction.add(R.id.id_content, fmTabAddr);
        transaction.add(R.id.id_content, fmTabFind);
        transaction.add(R.id.id_content, fmTabSetting);
        transaction.commit();
    }

    private void initView() {
        mTabHome = (LinearLayout) findViewById(R.id.id_tab_home);
        mTabAddr = (LinearLayout) findViewById(R.id.id_tab_addr);
        mTabFind = (LinearLayout) findViewById(R.id.id_tab_find);
        mTabSetting = (LinearLayout) findViewById(R.id.id_tab_setting);

        mImgHome = (ImageButton) findViewById(R.id.imgBtnHome);
        mImgAddr = (ImageButton) findViewById(R.id.imgBtnAddr);
        mImgFind = (ImageButton) findViewById(R.id.imgBtnFind);
        mImgSetting = (ImageButton) findViewById(R.id.imgBtnSetting);

        mTxtHome = (TextView) findViewById(R.id.txtHome);
        mTxtAddr = (TextView) findViewById(R.id.txtAddr);
        mTxtFind = (TextView) findViewById(R.id.txtFind);
        mTxtSetting = (TextView) findViewById(R.id.txtSetting);
    }

    private void hideFragment(FragmentTransaction transaction) {
        transaction.hide(fmTabHome);
        transaction.hide(fmTabAddr);
        transaction.hide(fmTabFind);
        transaction.hide(fmTabSetting);
    }

    private void setSelect(int i) {
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                transaction.show(fmTabHome);
                mImgHome.setImageResource(R.drawable.tab_weixin_pressed);
                mTxtHome.setTextColor(Color.parseColor("#08c161"));
                break;
            case 1:
                transaction.show(fmTabAddr);
                mImgAddr.setImageResource(R.drawable.tab_address_pressed);
                mTxtAddr.setTextColor(Color.parseColor("#08c161"));
                break;
            case 2:
                transaction.show(fmTabFind);
                mImgFind.setImageResource(R.drawable.tab_find_frd_pressed);
                mTxtFind.setTextColor(Color.parseColor("#08c161"));
                break;
            case 3:
                transaction.show(fmTabSetting);
                mImgSetting.setImageResource(R.drawable.tab_settings_pressed);
                mTxtSetting.setTextColor(Color.parseColor("#08c161"));
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void resetImgs() {
        mImgHome.setImageResource(R.drawable.tab_weixin_normal);
        mTxtHome.setTextColor(Color.parseColor("#000000"));
        mImgAddr.setImageResource(R.drawable.tab_address_normal);
        mTxtAddr.setTextColor(Color.parseColor("#000000"));
        mImgFind.setImageResource(R.drawable.tab_find_frd_normal);
        mTxtFind.setTextColor(Color.parseColor("#000000"));
        mImgSetting.setImageResource(R.drawable.tab_settings_normal);
        mTxtSetting.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void onClick(View v) {//对应implement View....
//        Log.i("OnClick", "1");
        resetImgs();
        switch (v.getId()) {
            case R.id.id_tab_home:
                setSelect(0);
                break;
            case R.id.id_tab_addr:
                setSelect(1);
                break;
            case R.id.id_tab_find:
                setSelect(2);
                break;
            case R.id.id_tab_setting:
                setSelect(3);
                break;
            default:
                break;
        }
    }

    private void initEvent() {//控制监听范围，只位于bottom
        mTabHome.setOnClickListener(this);
        mTabAddr.setOnClickListener(this);
        mTabFind.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);
    }
}
