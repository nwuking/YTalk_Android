package com.nwuking.ytalk.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nwuking.ytalk.MyApplication;

public class MyDbUtil {

    private static ContactsDao mContactsDao;
    private static ChatSessionDao mChatSessionDao;

    public static void initDatabase(Context context, String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbName + ".db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        MyApplication.getInstance().setDaoSession(daoMaster.newSession());
        mContactsDao = MyApplication.getInstance().getDaoSession().getContactsDao();
        mChatSessionDao = MyApplication.getInstance().getDaoSession().getChatSessionDao();
    }

    public static ContactsDao getContactsDao() {
        return mContactsDao;
    }

    public static ChatSessionDao getChatSessionDao() {
        return mChatSessionDao;
    }

    public static void uninit() {
        if (MyApplication.getInstance().getDaoSession() != null) {
            MyApplication.getInstance().getDaoSession().getDatabase().close();
        }

        mChatSessionDao = null;
        mContactsDao = null;
        MyApplication.getInstance().setDaoSession(null);
    }
}
