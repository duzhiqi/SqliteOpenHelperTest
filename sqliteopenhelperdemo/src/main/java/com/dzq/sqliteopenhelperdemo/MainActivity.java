package com.dzq.sqliteopenhelperdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MySqliteDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = new MySqliteDao(this);

        Button btn =   (Button) findViewById(R.id.btn);
        Button mixQuery = (Button) findViewById(R.id.btn1);

        List<User> list = new ArrayList<>();

        User u1 = new User(1, "xiao", "qiang");
        User u2 = new User(2, "xiao", "ming");
        User u22 = new User(2, "da", "ming");
        User u3 = new User(3, "xiao", "hong");
        User u33 = new User(3, "lao", "hong");
        User u4 = new User(4, "er", "gou");
        User u5 = new User(5, "lao", "wang");
        list.add(u1);
        list.add(u2);
        list.add(u22);
        list.add(u3);
        list.add(u33);
        list.add(u4);
        list.add(u5);

        dao.insert(list);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> users = dao.queryByAge(new String[]{"1", "2"});
                for (User user : users) {
                    Log.i(TAG, "user:" + user.toString());
                }
                Toast.makeText(MainActivity.this, users.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mixQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("age", "2");
                params.put("firstName", "xiao");
                List<User> users = dao.query(params);
                for (User user : users) {
                    Log.e("dzq", "user:" + user.toString());
                }
            }
        });
    }
}
