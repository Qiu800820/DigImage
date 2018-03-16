package com.pocoin.basemvp.third_party.push;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.Keep;

/**
 * Created by Robert yao on 2016/11/8.
 */
@Keep
public class PushMessage extends RealmObject implements Parcelable{
    public static String MSG_ID = "msgId";
    public static String MSG_SENT_TIME = "msgSentTime";
    public static String MSG_HAS_READ = "msgHasRead";
    @PrimaryKey
    private String msgId;
    private int msgType;

    private long msgSentTime;
    //本地字段, 不需要服务下发
    @Expose
    private boolean msgHasRead;
    private String msgContent;
    private String msgScheme;

    public PushMessage(){

    }

    protected PushMessage(Parcel in) {
        msgId = in.readString();
        msgType = in.readInt();
        msgSentTime = in.readLong();
        msgHasRead = in.readByte() != 0;
        msgContent = in.readString();
        msgScheme = in.readString();
    }

    public static final Creator<PushMessage> CREATOR = new Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel in) {
            return new PushMessage(in);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getMsgSentTime() {
        return msgSentTime;
    }

    public void setMsgSentTime(long msgSentTime) {
        this.msgSentTime = msgSentTime;
    }

    public boolean isMsgHasRead() {
        return msgHasRead;
    }

    public void setMsgHasRead(boolean msgHasRead) {
        this.msgHasRead = msgHasRead;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgScheme() {
        return msgScheme;
    }

    public void setMsgScheme(String msgScheme) {
        this.msgScheme = msgScheme;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msgId);
        dest.writeInt(msgType);
        dest.writeLong(msgSentTime);
        dest.writeByte((byte) (msgHasRead ? 1 : 0));
        dest.writeString(msgContent);
        dest.writeString(msgScheme);
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "msgId='" + msgId + '\'' +
                ", msgType=" + msgType +
                ", msgSentTime=" + msgSentTime +
                ", msgHasRead=" + msgHasRead +
                ", msgContent='" + msgContent + '\'' +
                ", msgScheme='" + msgScheme + '\'' +
                '}';
    }
}
