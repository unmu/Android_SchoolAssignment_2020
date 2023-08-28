package com.example.fragment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private ImageButton btnTopAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initFragment();
        initView();
        initEvent();
        setSelect(0);

        btnTopAdd = (ImageButton) findViewById(R.id.btnTopAdd);
        btnTopAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 加载result.xml界面布局代表的视图
                final View resultDialog = getLayoutInflater().inflate(R.layout.top_btn_add, null);  // 设置成final,否则String name = ((EditText) resultDialog.findViewById(R.id.name)).getText().toString();报EditText空指针错误
                // 使用对话框来显示查询结果
                AlertDialog show = new AlertDialog.Builder(MainActivity.this)
                        .setView(resultDialog)
                        .setTitle("Add your contact")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 获取程序界面中的三个文本框的内容
                                String name = ((EditText) resultDialog.findViewById(R.id.name)).getText().toString();
                                String phone = ((EditText) resultDialog.findViewById(R.id.phone)).getText().toString();
                                String email = ((EditText) resultDialog.findViewById(R.id.email)).getText().toString();
                                // 创建一个空的ContentValues
                                ContentValues values = new ContentValues();
                                // 向RawContacts.CONTENT_URI执行一个空值插入
                                // 目的是获取系统返回的rawContactId
                                Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
                                long rawContactId = ContentUris.parseId(rawContactUri);
                                values.clear();
                                values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
                                // 设置内容类型
                                values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                                // 设置联系人名字
                                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
                                // 向联系人URI添加联系人名字
                                getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                                values.clear();
                                values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
                                values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                                // 设置联系人的电话号码
                                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
                                // 设置电话类型
                                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                                // 向联系人电话号码URI添加电话号码
                                getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                                values.clear();
                                values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
                                values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                                // 设置联系人的E-mail地址
                                values.put(ContactsContract.CommonDataKinds.Email.DATA, email);
                                // 设置该电子邮件的类型
                                values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                                // 向联系人E-mail URI添加E-mail数据
                                getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                                Toast.makeText(v.getContext(), "下拉刷新[朋友]，查看新增联系人", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null).show();

            }
        });


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

