package com.nwuking.ytalk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class ContactsDao extends AbstractDao<Contacts, Long> {

    public static final String TABLENAME = "CONTACTS";

    /**
     * Properties of entity Contacts.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Userid = new Property(1, int.class, "userid", false, "USERID");
        public final static Property Type = new Property(2, int.class, "type", false, "TYPE");
        public final static Property Username = new Property(3, String.class, "username", false, "USERNAME");
        public final static Property Accept = new Property(4, int.class, "accept", false, "ACCEPT");
    }


    public ContactsDao(DaoConfig config) {
        super(config);
    }

    public ContactsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACTS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"USERID\" INTEGER NOT NULL ," + // 1: userid
                "\"TYPE\" INTEGER NOT NULL ," + // 2: type
                "\"USERNAME\" TEXT," + // 3: username
                "\"ACCEPT\" INTEGER NOT NULL );"); // 4: accept
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACTS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Contacts entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getUserid());
        stmt.bindLong(3, entity.getType());

        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(4, username);
        }
        stmt.bindLong(5, entity.getAccept());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Contacts entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getUserid());
        stmt.bindLong(3, entity.getType());

        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(4, username);
        }
        stmt.bindLong(5, entity.getAccept());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }

    @Override
    public Contacts readEntity(Cursor cursor, int offset) {
        Contacts entity = new Contacts( //
                cursor.getLong(offset + 0), // id
                cursor.getInt(offset + 1), // userid
                cursor.getInt(offset + 2), // type
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // username
                cursor.getInt(offset + 4) // accept
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, Contacts entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setUserid(cursor.getInt(offset + 1));
        entity.setType(cursor.getInt(offset + 2));
        entity.setUsername(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAccept(cursor.getInt(offset + 4));
    }

    @Override
    protected final Long updateKeyAfterInsert(Contacts entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(Contacts entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Contacts entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

}