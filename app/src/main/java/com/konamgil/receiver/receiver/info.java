package com.konamgil.receiver.receiver;

/**
 * Created by konamgil on 2017-05-24.
 */

public class info {

    private int TimeCount;
    private String TimeKinds;
    private int RepeatCount;
    private String Message;

    public info() {;}

    public info(int TimeCount, String TimeKinds, int RepeatCount, String Message) {
        this.TimeCount = TimeCount;
        this.TimeKinds = TimeKinds;
        this.RepeatCount = RepeatCount;
        this.Message = Message;
    }

    public int getTimeCount() {
        return TimeCount;
    }

    public void setTimeCount(int timeCount) {
        TimeCount = timeCount;
    }

    public String getTimeKinds() {
        return TimeKinds;
    }

    public void setTimeKinds(String timeKinds) {
        TimeKinds = timeKinds;
    }

    public int getRepeatCount() {
        return RepeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        RepeatCount = repeatCount;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
