package com.nwuking.ytalk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.nwuking.ytalk.ChatSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class ChatSessionDao extends AbstractDao<ChatSession, Long> {

    public static final String TABLENAME = "CHAT_SESSION";

    /**
     * Properties of entity ChatSession.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property MFriendID = new Property(1, int.class, "mFriendID", false, "M_FRIEND_ID");
        public final static Property MSelfID = new Property(2, int.class, "mSelfID", false, "M_SELF_ID");
        public final static Property NUnreadCount = new Property(3, int.class, "nUnreadCount", false, "N_UNREAD_COUNT");
        public final static Property MFriendNickName = new Property(4, String.class, "mFriendNickName", false, "M_FRIEND_NICK_NAME");
        public final static Property MLastMsg = new Property(5, String.class, "mLastMsg", false, "M_LAST_MSG");
        public final static Property MRemark = new Property(6, String.class, "mRemark", false, "M_REMARK");
        public final static Property Time = new Property(7, java.util.Date.class, "time", false, "TIME");
    }


    public ChatSessionDao(DaoConfig config) {
        super(config);
    }

    public ChatSessionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAT_SESSION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"M_FRIEND_ID\" INTEGER NOT NULL ," + // 1: mFriendID
                "\"M_SELF_ID\" INTEGER NOT NULL ," + // 2: mSelfID
                "\"N_UNREAD_COUNT\" INTEGER NOT NULL ," + // 3: nUnreadCount
                "\"M_FRIEND_NICK_NAME\" TEXT," + // 4: mFriendNickName
                "\"M_LAST_MSG\" TEXT," + // 5: mLastMsg
                "\"M_REMARK\" TEXT," + // 6: mRemark
                "\"TIME\" INTEGER);"); // 7: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAT_SESSION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChatSession entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getMFriendID());
        stmt.bindLong(3, entity.getMSelfID());
        stmt.bindLong(4, entity.getNUnreadCount());

        String mFriendNickName = entity.getMFriendNickName();
        if (mFriendNickName != null) {
            stmt.bindString(5, mFriendNickName);
        }

        String mLastMsg = entity.getMLastMsg();
        if (mLastMsg != null) {
            stmt.bindString(6, mLastMsg);
        }

        String mRemark = entity.getMRemark();
        if (mRemark != null) {
            stmt.bindString(7, mRemark);
        }

        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(8, time.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChatSession entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getMFriendID());
        stmt.bindLong(3, entity.getMSelfID());
        stmt.bindLong(4, entity.getNUnreadCount());

        String mFriendNickName = entity.getMFriendNickName();
        if (mFriendNickName != null) {
            stmt.bindString(5, mFriendNickName);
        }

        String mLastMsg = entity.getMLastMsg();
        if (mLastMsg != null) {
            stmt.bindString(6, mLastMsg);
        }

        String mRemark = entity.getMRemark();
        if (mRemark != null) {
            stmt.bindString(7, mRemark);
        }

        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(8, time.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }

    @Override
    public ChatSession readEntity(Cursor cursor, int offset) {
        ChatSession entity = new ChatSession( //
                cursor.getLong(offset + 0), // id
                cursor.getInt(offset + 1), // mFriendID
                cursor.getInt(offset + 2), // mSelfID
                cursor.getInt(offset + 3), // nUnreadCount
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // mFriendNickName
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // mLastMsg
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // mRemark
                cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)) // time
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, ChatSession entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setMFriendID(cursor.getInt(offset + 1));
        entity.setMSelfID(cursor.getInt(offset + 2));
        entity.setNUnreadCount(cursor.getInt(offset + 3));
        entity.setMFriendNickName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMLastMsg(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setMRemark(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTime(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
    }

    @Override
    protected final Long updateKeyAfterInsert(ChatSession entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(ChatSession entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChatSession entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

}
