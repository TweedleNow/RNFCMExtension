package in.tweedl.pushextension;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.webkit.URLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.tweedl.pushextension.localnotification.DatabaseManager;
import in.tweedl.pushextension.localnotification.PushData;

public class SendNotificationTask extends AsyncTask<Void, Void, Void> {
    private static final long DEFAULT_VIBRATION = 300L;
    private static final String TAG = SendNotificationTask.class.getSimpleName();
    private Context mContext;
    DatabaseManager dm;
    private Bundle bundle;
    private SharedPreferences sharedPreferences;
    private Boolean mIsForeground;

    public SendNotificationTask(Context context, SharedPreferences sharedPreferences, Boolean mIsForeground, Bundle bundle) {
        this.mContext = context;
        this.bundle = bundle;
        this.sharedPreferences = sharedPreferences;
        this.mIsForeground = mIsForeground;
        this.dm = new DatabaseManager();
    }

    protected Void doInBackground(Void... params) {


        PushData pd = getPushDataFromBundle(bundle);
     //   Log.e(TAG,"the push data is " + pd);
        if(pd == null)
            return null;

        dm.insertNotification(mContext, pd);

        List<PushData> pdList = new ArrayList<>();
        if (pd.getInboxStyleKey() != null && !pd.getInboxStyleKey().isEmpty())
            pdList = dm.getNotificationList(mContext, pd.getInboxStyleKey());


        try {
            String intentClassName = getMainActivityClassName();
            if (intentClassName == null) {
                return null;
            }

            if (pd.getBody() == null) {
                return null;
            }

            Resources res = mContext.getResources();
            String packageName = mContext.getPackageName();

            String title = pd.getTitle();
            if (title == null) {
                ApplicationInfo appInfo = mContext.getApplicationInfo();
                title = mContext.getPackageManager().getApplicationLabel(appInfo).toString();
            }

            NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext)
                    .setContentTitle(title)
                    .setContentText(pd.getBody())
                    .setTicker(pd.getTicker())
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                    .setAutoCancel(pd.isAuto_cancel())
                    .setNumber(pd.getNumber())
                    .setSubText(pd.getSub_text())
                    .setGroup(pd.getGroup())
                    .setVibrate(new long[]{0, DEFAULT_VIBRATION})
                    .setExtras(bundle.getBundle("data"));

            if (pd.isOngoing() != null && pd.isOngoing()) {
                notification.setOngoing(pd.isOngoing());
            }

            //priority
            String priority = pd.getPriority();
            switch (priority) {
                case "min":
                    notification.setPriority(NotificationCompat.PRIORITY_MIN);
                    break;
                case "high":
                    notification.setPriority(NotificationCompat.PRIORITY_HIGH);
                    break;
                case "max":
                    notification.setPriority(NotificationCompat.PRIORITY_MAX);
                    break;
                default:
                    notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            }

            //icon
            String smallIcon = pd.getIcon();
            int smallIconResId = res.getIdentifier(smallIcon, "mipmap", packageName);
            notification.setSmallIcon(smallIconResId);

            //large icon
            String largeIcon = pd.getLarge_icon();
            if (largeIcon != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (largeIcon.startsWith("http://") || largeIcon.startsWith("https://")) {
                    Bitmap bitmap = getBitmapFromURL(largeIcon);
                    notification.setLargeIcon(bitmap);
                } else {
                    int largeIconResId = res.getIdentifier(largeIcon, "mipmap", packageName);
                    Bitmap largeIconBitmap = BitmapFactory.decodeResource(res, largeIconResId);

                    if (largeIconResId != 0) {
                        notification.setLargeIcon(largeIconBitmap);
                    }
                }
            }


            //check if there are already push of the same group in tray, if they are than remove all of them and
            //have only one notification style of inbox style
            if (pdList != null && pdList.size() > 1) {

                // todo remove existing push notifications for the same key as we are now grouping them
                removeSameTypeNotif(pdList, pd);
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                //Log.i("myLog","show notification messge is::"+message.size()+"summary "+summary);
                for (int i = 0; i < pdList.size(); i++) {
                    if (title.length() > 20)
                        title = pd.getTitle().substring(0, 19) + "...";
                    inboxStyle.addLine(title + " : " + pd.getBody());
                }
                notification.setStyle(inboxStyle);
            } else {
                //show specifically that notification and add the notification to DB

                if (pd.getPicture() != null && URLUtil.isValidUrl(pd.getPicture())) {
                    // show image style notification

                    String picture = pd.getPicture();
                    if (picture != null) {
                        NotificationCompat.BigPictureStyle bigPicture = new NotificationCompat.BigPictureStyle();

                        if (picture.startsWith("http://") || picture.startsWith("https://")) {
                            Bitmap bitmap = getBitmapFromURL(picture);
                            bigPicture.bigPicture(bitmap);
                        } else {
                            int pictureResId = res.getIdentifier(picture, "mipmap", packageName);
                            Bitmap pictureResIdBitmap = BitmapFactory.decodeResource(res, pictureResId);

                            if (pictureResId != 0) {
                                bigPicture.bigPicture(pictureResIdBitmap);
                            }
                        }
                        bigPicture.setBigContentTitle(title);
                        bigPicture.setSummaryText(pd.getBody());

                        notification.setStyle(bigPicture);
                    }
                } else {
                    // show big text style notification
                    String bigText = pd.getBig_text();
                    if (bigText != null) {
                        notification.setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));
                    }
                }
                // done - add notification to db
            }


            String soundName = pd.getSound();
            if (soundName != null) {
                if (soundName.equalsIgnoreCase("default")) {
                    notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                } else {
                    int soundResourceId = res.getIdentifier(soundName, "raw", packageName);
                    if (soundResourceId == 0) {
                        soundName = soundName.substring(0, soundName.lastIndexOf('.'));
                        soundResourceId = res.getIdentifier(soundName, "raw", packageName);
                    }
                    notification.setSound(Uri.parse("android.resource://" + packageName + "/" + soundResourceId));
                }
            }

            //color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.setCategory(NotificationCompat.CATEGORY_CALL);

                String color = pd.getColor();
                if (color != null) {
                    notification.setColor(Color.parseColor(color));
                }
            }

            //vibrate
            if (pd.getVibrate() != null) {
                long vibrate = pd.getVibrate();
                if (vibrate > 0) {
                    notification.setVibrate(new long[]{0, vibrate});
                } else {
                    notification.setVibrate(null);
                }
            }

            //lights
            if (pd.isLights() != null && pd.isLights()) {
                notification.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
            }


            if (!mIsForeground || pd.getShow_in_foreground()) {
                Intent intent = new Intent();
                intent.setClassName(mContext, intentClassName);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtras(bundle);
                intent.setAction(pd.getClick_action());

                int notificationID = ((pdList != null && pdList.size() > 1) ? pdList.get(pdList.size() - 1).getId() : pd.getId()).hashCode();
                // giving size here to replace it

                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, notificationID, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager notificationManager =
                        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                notification.setContentIntent(pendingIntent);

                Notification info = notification.build();

                if (pd.getTag() != null) {
                    String tag = pd.getTag();
                    notificationManager.notify(tag, notificationID, info);

                } else {
                    notificationManager.notify(notificationID, info);
                }

            }


        } catch (Exception e) {
        }
        return null;
    }

    private void removeSameTypeNotif(List<PushData> pdList, PushData pd) {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        for (int i = 0; i < pdList.size(); i++) {
            PushData pdListI = pdList.get(i);
            if (pd.getInboxStyleKey() != null && pd.getInboxStyleKey().equals(pdListI.getInboxStyleKey())) {
                notificationManager.cancel(pdListI.getId().hashCode());

                // dm.clearSpecificDB(mContext,pdListI.getId());
            }
        }
    }


    private PushData getPushDataFromBundle(Bundle bundle) {


        PushData pd = new PushData();

        try {
            pd.setId(bundle.getString("id"));
            pd.setTitle(bundle.getString("title"));
            pd.setBody(bundle.getString("body"));
            pd.setSound(bundle.getString("sound"));
            pd.setPriority(bundle.getString("priority"));
            pd.setClick_action(bundle.getString("click_action"));
            pd.setBadge(bundle.getInt("badge", 0));
            pd.setNumber(bundle.getInt("number", 0));
            pd.setTicker(bundle.getString("ticker"));
            pd.setAuto_cancel(bundle.getBoolean("auto_cancel", true));
            pd.setLarge_icon(bundle.getString("large_icon"));
            pd.setIcon(bundle.getString("icon", "ic_launcher"));
            pd.setBig_text(bundle.getString("big_text"));
            pd.setSub_text(bundle.getString("sub_text"));
            pd.setColor(bundle.getString("color"));
            pd.setVibrate(bundle.getInt("vibrate", 100));
            pd.setTag(bundle.getString("tag"));
            pd.setGroup(bundle.getString("group"));
            pd.setPicture(bundle.getString("picture"));
            pd.setOngoing(bundle.getBoolean("ongoing", false));
            pd.setMy_custom_data(bundle.getString("my_custom_data"));
            pd.setLights(bundle.getBoolean("lights", false));
            pd.setShow_in_foreground(bundle.getBoolean("show_in_foreground",true));
            pd.setInboxStyle(bundle.getBoolean("inboxStyle", true));
            pd.setInboxStyleKey(bundle.getString("inboxStyleKey"));
            pd.setInboxStyleMessage(bundle.getString("inboxStyleMessage"));
            pd.setTimeStamp(System.currentTimeMillis());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }


        return pd;
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMainActivityClassName() {
        String packageName = mContext.getPackageName();
        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        String className = launchIntent.getComponent().getClassName();
        return className;
    }
}
