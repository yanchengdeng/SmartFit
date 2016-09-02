package com.smartfit.recieve;/**
 * 作者：dengyancheng on 16/5/12 21:14
 * 邮箱：yanchengdeng@gmail.com
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.smartfit.R;
import com.smartfit.activities.MessageActivity;
import com.smartfit.beans.PushBean;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;

/**
 * 作者：dengyancheng on 16/5/12 21:14
 * 邮箱：yanchengdeng@gmail.com
 */
public class PushReceiver extends BroadcastReceiver {
    public final static int NOTIFICATION_ID = "NotificationActivityDemo".hashCode();
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtil.w("dyc", "onReceive() action=" + bundle.getInt("action"));

        /**
         * 10001    传透消息
         *
         *
         */
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
                LogUtil.w("dyc", "receiver payload : " + result);
                if (payload != null) {
                    String data = new String(payload);
                    if (!TextUtils.isDigitsOnly(data)) {
                        PushBean msg = JsonUtils.objectFromJson(data, PushBean.class);
                        if (msg != null) {
                            if (!Util.isInApp(context)) {
                                NormalDialogStyleOne(context, msg.getTitle());
                            } else {
                                showNotificaiton(context, msg);
                            }
                        }
                    }
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                SharedPreferencesUtils.getInstance().putString(Constants.CLINET_ID, cid);
                LogUtil.w("dyc","获取clientId"+cid);
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }

    private void showNotificaiton(Context context, PushBean msg) {
        Bitmap btm = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.message_icon);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.message_icon)
                .setContentTitle("SmartFit")
                .setContentText(msg.getTitle());
        mBuilder.setTicker(msg.getTitle());//第一次提示消息的时候显示在通知栏上
        mBuilder.setNumber(12);
        mBuilder.setLargeIcon(btm);
        mBuilder.setAutoCancel(true);//自己维护通知的消失

        //构建一个Intent
        Intent resultIntent = new Intent(context,
                MessageActivity.class);
        //封装一个Intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置通知主题的意图
        mBuilder.setContentIntent(resultPendingIntent);
        //获取通知管理器对象
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }


    private void NormalDialogStyleOne(final Context context, String tittle) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#ffffff"))//
                .cornerRadius(5)//
                .content(tittle)//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#222222"))//
                .dividerColor(Color.parseColor("#222222"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#222222"), Color.parseColor("#222222"))//
                .btnPressColor(Color.parseColor("#ffffff"))//
                .widthScale(0.85f)//
//                .showAnim(mBasIn)//
//                .dismissAnim(mBasOut)//
                .show();
        dialog.btnText(new String[]{"取消", "查看"});
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                    }
                });
    }
}
