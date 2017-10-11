package in.tweedl.pushextension.localnotification;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by sparshjain on 28/09/17.
 */
@DatabaseTable(tableName = "pushData")
public class PushData {

    @DatabaseField(columnName = "timeStamp")
    private long timeStamp;
    @DatabaseField(columnName = "id")
    private Integer id;            // "UNIQ_ID_STRING",               // (optional for instant notification)
    @DatabaseField(columnName = "title")
    private String title;            // "My Notification Title",            // as FCM payload
    @DatabaseField(columnName = "body")
    private String body;            // "My Notification Message",private String               // as FCM payload (required)
    @DatabaseField(columnName = "sound")
    private String sound;            // "default",            // as FCM payload
    @DatabaseField(columnName = "priority")
    private String priority;            // "high",            // as FCM payload
    @DatabaseField(columnName = "click_action")
    private String click_action;            // "ACTION",             // as FCM payload
    @DatabaseField(columnName = "badge")
    private Integer badge;            // 10,            // as FCM payload IOS only, set 0 to clear badges
    @DatabaseField(columnName = "number")
    private Integer number;            // 10,private String               // Android only
    @DatabaseField(columnName = "ticker")
    private String ticker;            // "My Notification Ticker",private String              // Android only
    @DatabaseField(columnName = "auto_cancel")
    private Boolean auto_cancel;            // true,private String               // Android only (default true)
    @DatabaseField(columnName = "large_icon")
    private String large_icon;            // "ic_launcher",private String               // Android only
    @DatabaseField(columnName = "icon")
    private String icon;            // "ic_launcher",private String             // as FCM payload, you can relace this with custom icon you put in mipmap
    @DatabaseField(columnName = "big_text")
    private String big_text;            // "Show when notification is expanded",private String              // Android only
    @DatabaseField(columnName = "sub_text")
    private String sub_text;            // "This is a subText",             // Android only
    @DatabaseField(columnName = "color")
    private String color;            // "red",private String             // Android only
    @DatabaseField(columnName = "vibrate")
    private Integer vibrate;            // 300,private String             // Android only default;            // 300, no vibration if you pass null
    @DatabaseField(columnName = "tag")
    private String tag;            // 'some_tag',             // Android only
    @DatabaseField(columnName = "group")
    private String group;            // "group",              // Android only
    @DatabaseField(columnName = "picture")
    private String picture;            // "https;            //            //google.png",             // Android only bigPicture style
    @DatabaseField(columnName = "ongoing")
    private Boolean ongoing;            // true,               // Android only
    @DatabaseField(columnName = "my_custom_data")
    private String my_custom_data;            //'my_custom_field_value',private String               // extra data you want to throw
    @DatabaseField(columnName = "lights")
    private Boolean lights;            // true,private String             // Android only, LED blinking (default false)
    @DatabaseField(columnName = "inboxStyle")
    private Boolean inboxStyle;
    @DatabaseField(columnName = "show_in_foreground")
    private Boolean show_in_foreground;
    @DatabaseField(columnName = "inboxStyleMessage")
    private String inboxStyleMessage;
    @DatabaseField(columnName = "inboxStyleKey")
    private String inboxStyleKey;


    public Boolean isAuto_cancel() {
        return auto_cancel;
    }

    public Boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    public Boolean isLights() {
        return lights;
    }

    public void setLights(Boolean lights) {
        this.lights = lights;
    }

    public Boolean isInboxStyle() {
        return inboxStyle;
    }

    public void setInboxStyle(Boolean inboxStyle) {
        this.inboxStyle = inboxStyle;
    }

    public String getInboxStyleMessage() {
        return inboxStyleMessage;
    }

    public void setInboxStyleMessage(String inboxStyleMessage) {
        this.inboxStyleMessage = inboxStyleMessage;
    }

    public String getInboxStyleKey() {
        return inboxStyleKey;
    }

    public void setInboxStyleKey(String inboxStyleKey) {
        this.inboxStyleKey = inboxStyleKey;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
    }

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }


    public void setAuto_cancel(Boolean auto_cancel) {
        this.auto_cancel = auto_cancel;
    }

    public String getLarge_icon() {
        return large_icon;
    }

    public void setLarge_icon(String large_icon) {
        this.large_icon = large_icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBig_text() {
        return big_text;
    }

    public void setBig_text(String big_text) {
        this.big_text = big_text;
    }

    public String getSub_text() {
        return sub_text;
    }

    public void setSub_text(String sub_text) {
        this.sub_text = sub_text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getVibrate() {
        return vibrate;
    }

    public void setVibrate(Integer vibrate) {
        this.vibrate = vibrate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMy_custom_data() {
        return my_custom_data;
    }

    public void setMy_custom_data(String my_custom_data) {
        this.my_custom_data = my_custom_data;
    }


    public Boolean getShow_in_foreground() {
        return show_in_foreground;
    }

    public void setShow_in_foreground(Boolean show_in_foreground) {
        this.show_in_foreground = show_in_foreground;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    //true


}
