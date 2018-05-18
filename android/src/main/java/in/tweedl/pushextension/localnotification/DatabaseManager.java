package in.tweedl.pushextension.localnotification;

/**
 * Created by sparshjain on 28/09/17.
 */

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mihir on 30/11/16.
 */
public class DatabaseManager {

    private static DatabaseManager instance = null;
    private DatabaseHandler databaseHelper;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }


    private DatabaseHandler getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHandler.class);
        }
        return databaseHelper;
    }

    public void releaseHandler() {
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
        instance = null;
    }


    public long insertNotification(Context context, PushData message) {
        try {
            final Dao<PushData, Integer> dao = getHelper(context).getPushData();
            return dao.create(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    public List<PushData> getNotificationList(Context context, String key) {
        List<PushData> catList = null;
        try {
            QueryBuilder<PushData, Integer> query = getHelper(context).getPushData().queryBuilder();
            query.where().eq("inboxStyleKey", key);
            query.orderBy("timeStamp", false);
            catList = query.query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return catList;
    }

    public int deleteTable(Context context) {
        try {
            Log.e("fooooooo", "deleting the table");
            DeleteBuilder<PushData, Integer> deleteBuilder = getHelper(context).getPushData().deleteBuilder();
            return deleteBuilder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public List<PushData> getNotificationList(Context context) {
        List<PushData> catList = null;
        try {
            QueryBuilder<PushData, Integer> query = getHelper(context).getPushData().queryBuilder();
            query.limit(10L);
            query.orderBy("timeStamp", false);
            catList = query.query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return catList;
    }

    public void deleteNotification(Context mContext, String notificationId) {
        try {
            DeleteBuilder<PushData, Integer> deleteBuilder = getHelper(mContext).getPushData().deleteBuilder();
            deleteBuilder.where().eq("id", notificationId);
            deleteBuilder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

