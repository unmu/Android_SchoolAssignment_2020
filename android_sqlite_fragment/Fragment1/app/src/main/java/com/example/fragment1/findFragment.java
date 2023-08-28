package com.example.fragment1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class findFragment extends Fragment implements View.OnClickListener {
//    SQLiteDatabase db;
//    Cursor cursor;
//    StringBuilder result = new StringBuilder("Program work: \n");
//    Context context;

    private usersDAO myDAO;
    private ListView lvFind;
    private List<Map<String, Object>> listData;
    private Map<String, Object> listItem;
    private SimpleAdapter listAdapter;

    private EditText etName;
    private EditText etPwd;
    private String selectId = null;

    View view;

    public findFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.tab_find, container, false);


        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        Button btnModify = (Button) view.findViewById(R.id.btnModify);
        Button btnDelete = (Button) view.findViewById(R.id.btnDelete);

        btnAdd.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        etName = (EditText) view.findViewById(R.id.etName);
        etPwd = (EditText) view.findViewById(R.id.etPwd);

        myDAO = new usersDAO(view.getContext());

        if(myDAO.getRecordsNumber() == 0) {
            myDAO.insertInfo("yi", "123456");
            myDAO.insertInfo("er", "123456");
        }

        displayRecords(view);

        return view;
    }

    public void displayRecords(View view) {
        lvFind = (ListView) view.findViewById(R.id.lvFind);
        listData = new ArrayList<Map<String, Object>>();
        Cursor cursor = myDAO.allQuery();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
//            String pwd = cursor.getString(2);
            String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            listItem = new HashMap<String, Object>();
            listItem.put("_id", id);
            listItem.put("name", name);
            listItem.put("pwd", pwd);

            listData.add(listItem);
        }
        cursor.close();
        listAdapter = new SimpleAdapter(view.getContext(),
                listData,
                R.layout.list_item_find,
                new String[]{"_id", "name", "pwd"},
                new int[]{R.id.txtId, R.id.txtName, R.id.txtPwd});
                lvFind.setAdapter(listAdapter);
                lvFind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> rec = (Map<String, Object>) listAdapter.getItem(position);
                etName.setText(rec.get("name").toString());
                etPwd.setText(rec.get("pwd").toString());
                Log.i("", rec.get("_id").toString());
                selectId = rec.get("_id").toString();
                Toast.makeText(view.getContext(), selectId + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (selectId != null) {// 选择列表项，以进行操作
            String p1 = etName.getText().toString().trim();
            String p2 = etPwd.getText().toString().trim();
            switch (v.getId()) {
                case R.id.btnAdd:
                    myDAO.insertInfo(p1, p2);
                    break;
                case R.id.btnModify:
                    myDAO.updateInfo(p1, p2, selectId);
                    Toast.makeText(v.getContext(), "update succeed", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnDelete:
                    myDAO.deleteInfo(selectId);
                    Toast.makeText(v.getContext(), "delete succeed", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {//未选择列表项
            if (v.getId() == R.id.btnAdd) {
                String p1 = etName.getText().toString();
                String p2 = etPwd.getText().toString();
                if (p1.equals("") || p2.equals("")) {
                    Toast.makeText(v.getContext(), "please input name and password", Toast.LENGTH_SHORT).show();
                } else {
                    myDAO.insertInfo(p1, p2);
                }
            } else {
                Toast.makeText(v.getContext(), "please select the record", Toast.LENGTH_SHORT).show();
            }
        }
        displayRecords(view);       // 之前出现空指针错误。必须把View设定为tab_find的View，否则闪退
                                // Attempt to invoke virtual method 'void android.widget.ListView.setAdapter(android.widget.ListAdapter)' on a null object reference
    }


    public class DbHelper extends SQLiteOpenHelper {
        public static final String TB_NAME = "users";


        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            Toast.makeText(context, "create tables of database...", Toast.LENGTH_SHORT).show();
            db.execSQL("create table if not exists " + TB_NAME + "(_id integer primary key autoincrement, name varchar, pwd varchar)");
//            db.execSQL("insert into user values(null, 'Android', '123456')");
//            result.append("create table and insert data\n");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TB_NAME);
            onCreate(db);
        }
    }

    public class usersDAO {
        private SQLiteDatabase db;
        private DbHelper dbHelper;

        public usersDAO(Context context) {
            dbHelper = new DbHelper(context, "test.db", null, 1);

        }
        // 查询所有记录
        public Cursor allQuery() {
            db = dbHelper.getReadableDatabase();
            return db.rawQuery("select * from users", null);

        }
        // 返回数据表记录数
        public int getRecordsNumber() {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from users", null);
            return cursor.getCount();
        }
        // 插入表数据
        public void insertInfo(String name, String pwd) {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("pwd", pwd);
            long rowId = db.insert(dbHelper.TB_NAME, null, values);
            if (rowId == -1) {
                Log.i("myDbDemo", "数据插入失败！");
            } else {
                Log.i("myDbDemo", "数据插入成功！" + rowId);
            }
        }
        // 删除表数据
        public void deleteInfo(String selectId) {
            String where = "_id = " + selectId;
            int i = db.delete(dbHelper.TB_NAME, where, null);
            if (i > 0) {
                Log.i("myDbDemo", "数据删除成功！");
            } else {
                Log.i("myDbDemo", "数据未删除！");
            }
        }
        // 更新表数据
        public void updateInfo(String name, String pwd, String selectId) {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("pwd", pwd);
            String where = "_id = " + selectId;
            int i = db.update(dbHelper.TB_NAME, values, where, null);
//            --or--
//            db.execSQL("update users set name = ?, pwd = ? where _id = ?", new Object[]{name, pswd, seleteId});
            if (i > 0) {
                Log.i("myDbDemo","数据更新成功！");
            } else {
                Log.i("myDbDemo","数据未更新！");
            }
        }
    }
}
