package com.konamgil.receiver.receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BroadcastReceiver {
    private String info = "";
    private Vibrator vide;
    private XmlHelper mXmlHelper;
    private ArrayList<info> infoItem;
    private int TimeCount;
    private String TimeKinds;
    private int RepeatCount;
    private String infoMessage;
    private int count = 1;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.provider.Telephony.SMS_RECEIVED".equals(action)) {
            /**
             * SMS메세지 파싱
             */
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                /**
                 * PDU포멧의 SMS를 변환합니다
                 */
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }
//            받은 메시지 번호
            final String origNumber = smsMessage[0].getOriginatingAddress();
//            String Message = smsMessage[0].getMessageBody().toString();

            //장문일경우 내용 받아오기
            for (SmsMessage message : smsMessage) {
                info += message.getMessageBody().toString();
            }
            Toast.makeText(context,info, Toast.LENGTH_SHORT).show();
            mXmlHelper = new XmlHelper();
            infoItem = mXmlHelper.getParsedData(info);

            TimeCount = infoItem.get(0).getTimeCount();
            TimeKinds = infoItem.get(0).getTimeKinds();
            RepeatCount = infoItem.get(0).getRepeatCount();
            infoMessage = infoItem.get(0).getMessage();

            //notification 쓰레드로 실행
            Thread notiThread = new Thread() {
                @Override
                public void run() {
                    notiTimerTask(context, origNumber, TimeCount, TimeKinds, RepeatCount, infoMessage);
                }
            };
            notiThread.start();
        }
    }

    /**
     * info 정보를 분석하여 noti를 발생시킵니다
     *
     * @param context
     * @param origNumber
     * @param TimeCount
     * @param TimeKinds
     * @param RepeatCount
     * @param infoMessage
     */
    private void notiTimerTask(final Context context, final String origNumber, final int TimeCount,
                               final String TimeKinds, final int RepeatCount, final String infoMessage) {
        final Timer timer;
        TimerTask timerTask;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    //반복시간
                    int repeatTime = divTimeKinds(TimeKinds);
                    Thread.sleep(TimeCount * repeatTime);

                    //noti를 발생시킨다
                    infoNnotify(context, origNumber, count, TimeCount, TimeKinds, RepeatCount, infoMessage);

                    //진동을 발생시킨다
                    vibe(context);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count == RepeatCount) {
                    timer.cancel();
                }
                count++;
            }

            @Override
            public boolean cancel() {
                Log.v("", "타이머 종료");
                return super.cancel();
            }
        };
        timer.schedule(timerTask, 0, 1000);

    }

    /**
     * TimeKinds 에 따른 시간계산 메서드
     *
     * @param TimeKinds
     * @return
     */
    private int divTimeKinds(String TimeKinds) {
        int kind = 0;
        switch (TimeKinds.toLowerCase()) {
            case "s":
                kind = 1000;
                break;
            case "m":
                kind = 1000 * 60;
                break;
            case "h":
                kind = 1000 * 60 * 60;
                break;
            default:
                break;
        }
        return kind;
    }

    /**
     * show noti
     *
     * @param context
     */
    public void infoNnotify(Context context, String origNumber, int count, int TimeCount, String TimeKinds, int RepeatCount, String infoMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(infoMessage)
                .setContentText(count + "")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
    }

    //진동
    public void vibe(Context context) {
        vide = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vide.vibrate(1000);
    }
}

