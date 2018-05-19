package edu.hzuapps.androidlabs.com1614080901238.mostbeautifulwallpaper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.hzuapps.androidlabs.com1614080901238.mostbeautifulwallpaper.adapter.WallpaperAdapter;
import edu.hzuapps.androidlabs.com1614080901238.mostbeautifulwallpaper.db.WallpaperDB;
import edu.hzuapps.androidlabs.com1614080901238.mostbeautifulwallpaper.entity.ItemContent;

public class Com820238114Activity extends AppCompatActivity {
    private Context context;
    private List<ItemContent> itemContents;
    private List<String> comments;
    private WallpaperDB wallpaperDB;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com820238114_activity);
        initData();
        initView();
    }

//            public void DBclick(View view){
//        //初始化数据库，建表
//        wallpaperDB = new WallpaperDB(this, "wallpaper.db", null, 1);
//        db = wallpaperDB.getWritableDatabase();
//
//        //初始化数据
//        //插入item内容
//        for (int i = 1; i < 100; i++) {
//            ContentValues values = new ContentValues();
//            values.put("title", "小动物" + i + "号");
//            values.put("comment_id", i);
//            db.insert("wallpaper_content", null, values);
//            values.clear();
//            //插入评论
//            for (int j = 1; j <= 5; j++) {
//                values.put("comment_id", i);
//                values.put("comment_text", "小动物" + i + "号的第" + j + "评论");
//                db.insert("comment", null, values);
//                values.clear();
//            }
//        }
//            Toast.makeText(context, "ojbk", Toast.LENGTH_SHORT).show();
//    }
    private void initData() {
        context = getApplicationContext();
        wallpaperDB = new WallpaperDB(this, "wallpaper.db", null, 1);
        db = wallpaperDB.getWritableDatabase();
        itemContents = new ArrayList<>();

        Cursor wallpaperCursor = db.query("wallpaper_content", null, null, null, null, null, null);
        if (wallpaperCursor.moveToFirst()) {
            do {
                ItemContent itemContent = new ItemContent();
                itemContent.setTitle(wallpaperCursor.getString(wallpaperCursor.getColumnIndex("title")));
                itemContent.setId(wallpaperCursor.getInt(wallpaperCursor.getColumnIndex("id")));
                Cursor commentCursor = db.query("comment", null, "comment_id =?", new String[]{String.valueOf(wallpaperCursor.getPosition() + 1)}, null, null, null);
                Log.i("", "initData: " + wallpaperCursor.getPosition());
                comments = new ArrayList<>();
                if (commentCursor.moveToFirst()) {
                    do {

                        comments.add(commentCursor.getString(commentCursor.getColumnIndex("comment_text")));
                        Log.i("czc", "initData: "+comments.get(commentCursor.getPosition()));

                    }
                    while (commentCursor.moveToNext());
                    commentCursor.close();
                }
                itemContent.setComnent(comments);
                Log.i("czc", "initData: " + itemContent.getTitle());
                itemContents.add(itemContent);

            } while (wallpaperCursor.moveToNext());
            wallpaperCursor.close();
        }


    }

    private void initView() {
        initRV();
    }

    private void initRV() {
        RecyclerView rv = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(layoutManager);
        WallpaperAdapter adapter = new WallpaperAdapter(itemContents, context);
        rv.setAdapter(adapter);
    }
}
