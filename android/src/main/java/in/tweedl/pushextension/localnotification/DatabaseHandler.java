package in.tweedl.pushextension.localnotification;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by sparshjain on 28/09/17.
 */


public class DatabaseHandler extends OrmLiteSqliteOpenHelper {

    private static final String DBNAME = "PUSH_DATA_DB" ;
    private static final int DBVERSION = 1;
    Context context;

    protected Dao<PushData, Integer> pushDataDao = null;


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, PushData.class);
        } catch (Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


    public DatabaseHandler(Context context) {
        super(context, DBNAME, null, DBVERSION);
        this.context = context;
    }


    public Dao<PushData, Integer> getPushData() throws SQLException {
        if (pushDataDao == null) {
            pushDataDao = getDao(PushData.class);
        }
        return pushDataDao;
    }






    @Override
    public void close() {
        super.close();
        this.pushDataDao = null;
    }

}

