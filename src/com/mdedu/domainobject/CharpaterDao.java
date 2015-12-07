package com.mdedu.domainobject;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CHARPATER.
*/
public class CharpaterDao extends AbstractDao<Charpater, Long> {

    public static final String TABLENAME = "CHARPATER";

    /**
     * Properties of entity Charpater.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CourseId = new Property(1, Long.class, "courseId", false, "COURSE_ID");
        public final static Property Parent = new Property(2, Long.class, "parent", false, "PARENT");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Seq = new Property(4, Integer.class, "seq", false, "SEQ");
    };

    private DaoSession daoSession;

    private Query<Charpater> charpater_ChidrenCharpeterQuery;
    private Query<Charpater> course_CharptersQuery;

    public CharpaterDao(DaoConfig config) {
        super(config);
    }
    
    public CharpaterDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CHARPATER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'COURSE_ID' INTEGER," + // 1: courseId
                "'PARENT' INTEGER," + // 2: parent
                "'NAME' TEXT," + // 3: name
                "'SEQ' INTEGER);"); // 4: seq
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CHARPATER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Charpater entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long courseId = entity.getCourseId();
        if (courseId != null) {
            stmt.bindLong(2, courseId);
        }
 
        Long parent = entity.getParent();
        if (parent != null) {
            stmt.bindLong(3, parent);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        Integer seq = entity.getSeq();
        if (seq != null) {
            stmt.bindLong(5, seq);
        }
    }

    @Override
    protected void attachEntity(Charpater entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Charpater readEntity(Cursor cursor, int offset) {
        Charpater entity = new Charpater( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // courseId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // parent
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // seq
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Charpater entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCourseId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setParent(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSeq(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Charpater entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Charpater entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "chidrenCharpeter" to-many relationship of Charpater. */
    public List<Charpater> _queryCharpater_ChidrenCharpeter(Long parent) {
        synchronized (this) {
            if (charpater_ChidrenCharpeterQuery == null) {
                QueryBuilder<Charpater> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Parent.eq(null));
                charpater_ChidrenCharpeterQuery = queryBuilder.build();
            }
        }
        Query<Charpater> query = charpater_ChidrenCharpeterQuery.forCurrentThread();
        query.setParameter(0, parent);
        return query.list();
    }

    /** Internal query to resolve the "charpters" to-many relationship of Course. */
    public List<Charpater> _queryCourse_Charpters(Long courseId) {
        synchronized (this) {
            if (course_CharptersQuery == null) {
                QueryBuilder<Charpater> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CourseId.eq(null));
                course_CharptersQuery = queryBuilder.build();
            }
        }
        Query<Charpater> query = course_CharptersQuery.forCurrentThread();
        query.setParameter(0, courseId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getCourseDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getCharpaterDao().getAllColumns());
            builder.append(" FROM CHARPATER T");
            builder.append(" LEFT JOIN COURSE T0 ON T.'COURSE_ID'=T0.'_id'");
            builder.append(" LEFT JOIN CHARPATER T1 ON T.'PARENT'=T1.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Charpater loadCurrentDeep(Cursor cursor, boolean lock) {
        Charpater entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Course course = loadCurrentOther(daoSession.getCourseDao(), cursor, offset);
        entity.setCourse(course);
        offset += daoSession.getCourseDao().getAllColumns().length;

        Charpater charpater = loadCurrentOther(daoSession.getCharpaterDao(), cursor, offset);
        entity.setCharpater(charpater);

        return entity;    
    }

    public Charpater loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Charpater> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Charpater> list = new ArrayList<Charpater>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Charpater> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Charpater> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}