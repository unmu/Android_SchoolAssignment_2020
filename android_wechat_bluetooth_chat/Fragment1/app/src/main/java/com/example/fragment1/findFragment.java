package com.example.fragment1;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class findFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<String> mList = new ArrayList<>();
    public List<PersonData> mDataList = new ArrayList<>();
    public ExpandCollapseAdapter adapter;
    public RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    View view;

    public findFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_find, container, false);

        adapter = new ExpandCollapseAdapter(view.getContext(), mDataList);
        recyclerView = view.findViewById(R.id.rcv_expandcollapse);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.setExpandCollapseDataList(mDataList);
        // https://www.jianshu.com/p/b7878fb4d941

        // 给予权限
        int hasWriteContactsPermisson = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_CONTACTS);
        if(hasWriteContactsPermisson != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
        }

        //悬浮按钮点击操作
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final View resultDialog = getActivity().getLayoutInflater().inflate(R.layout.top_btn_add, null);  // 设置成final,否则String name = ((EditText) resultDialog.findViewById(R.id.name)).getText().toString();报EditText空指针错误
                // 使用对话框来显示查询结果
                AlertDialog show = new AlertDialog.Builder(view.getContext())
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
                                Uri rawContactUri = getActivity().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
                                long rawContactId = ContentUris.parseId(rawContactUri);
                                values.clear();
                                values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
                                // 设置内容类型
                                values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                                // 设置联系人名字
                                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
                                // 向联系人URI添加联系人名字
                                getActivity().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                                values.clear();
                                values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
                                values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                                // 设置联系人的电话号码
                                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
                                // 设置电话类型
                                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                                // 向联系人电话号码URI添加电话号码
                                getActivity().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                                values.clear();
                                values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
                                values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                                // 设置联系人的E-mail地址
                                values.put(ContactsContract.CommonDataKinds.Email.DATA, email);
                                // 设置该电子邮件的类型
                                values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                                // 向联系人E-mail URI添加E-mail数据
                                getActivity().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

                                Toast.makeText(view.getContext(), "联系人添加成功", Toast.LENGTH_SHORT).show();
                                PersonData personData = new PersonData();
                                personData.setName(name);
                                personData.setPhoneNumber(phone);
                                personData.setEmailAddress(email);
                                mDataList.add(personData);
                                mList.add("");
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);      // 下拉刷新

        onRefresh();

        return view;
    }

    private void viewShow() {       // 将系统联系人中的数据
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,// 使用ContentResolver查找联系人数据
                null, null,
                null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));// 获取联系人ID
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));// 获取联系人的名字
            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,// 使用ContentResolver查找联系人的电话号码
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID  + " = " + contactId,
                    null, null);
            String phoneNumber = "";
            while (phones.moveToNext()) {// 遍历查询结果，获取该联系人的多个电话号码
                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));// 获取查询结果中电话号码列中数据
            }
            phones.close();
            Cursor emails = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,// 使用ContentResolver查找联系人的E-mail地址
                    null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                    null, null);
            String emailAddress = "";
            while (emails.moveToNext()) {// 遍历查询结果，获取该联系人的多个E-mail地址
                emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));// 获取查询结果中E-mail地址列中数据
            }
            emails.close();
            mList.add(name + "| " + phoneNumber + "| " + emailAddress);
        }
        cursor.close();
        initData();
    }

    private void initData() {       // 对mList数据字符串切割存入mDataList
        for (int i = 0; i < mList.size(); i++) {
            PersonData personData = new PersonData();
            String s = mList.get(i);
            String[] all = s.split("\\|");      //*转义 https://www.cnblogs.com/uhhuh/p/6082716.html
            String name = all[0];
            String phoneNumber = all[1];
            String emailAddress = all[2];
            personData.setName(name);
            personData.setPhoneNumber(phoneNumber);
            personData.setEmailAddress(emailAddress);
            mDataList.add(personData);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mList.size(); ){// 先清空mList数据和RecyclerView的Item
                    mList.remove(0);
                    adapter.removeItem(0);
                }
                viewShow();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }
}
