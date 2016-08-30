package com.knower.service;

    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.Context;
    import android.content.Intent;
    import android.media.RingtoneManager;
    import android.net.Uri;
    import android.support.v4.app.NotificationCompat;
    import android.util.Log;

    import com.google.firebase.messaging.FirebaseMessagingService;
    import com.google.firebase.messaging.RemoteMessage;
    import com.knower.gac.DrawerActivity;
    import com.knower.gac.R;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;


public class FCMMsgReceiverService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d("ouput", "From: " + remoteMessage.getFrom());
        Log.d("ouput", "Notification Message Body: " + remoteMessage.getData().get("payload"));


        //Calling method to generate notification
        sendNotification(remoteMessage.getData().get("payload"));
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        String message="";
        //notification messaging Pasing
        try {
            JSONArray array=new JSONArray(messageBody);
            JSONObject obj=array.getJSONObject(0);
            if(!obj.has("Command")){
                return;
            }
            if(obj.getString("Command").equalsIgnoreCase("SP")){
                if (!obj.has("CHL")){
                    return;
                }
                message=obj.getString("CHL").toString()+" "+" has gone off.";
            }else if(obj.getString("Command").equalsIgnoreCase("EM")) {
                if (!obj.has("CHL")) {
                    return;
                }
                message = obj.getString("CHL").toString() + " " + " has come on.";
            }else{
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if (commandValue.equalsIgnoreCase("SM")) {
//            //seeking permission to view channel
//            // outDisplay = "Subscribe";
//            Intent dialogIntent = new Intent(context, PrimaryScreen.class);
//            dialogIntent.putExtra("Command", "SM");
//            dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            dialogIntent.putExtra("CHL", newMessage.get(1) + "");
//            dialogIntent.putExtra("PH", newMessage.get(2) + "");
//            dialogIntent.putExtra("CHLID", newMessage.get(3) + "");
//            dialogIntent.putExtra("SUBSID", newMessage.get(4) + "");
//            context.startActivity(dialogIntent);
//        }


//
//        else if (commandValue.equalsIgnoreCase("EM")) {
//            //start publishing
//            System.out.println("primaryscreenOntop:" + GlobalContext.channelBean.isPrimaryScreenOnTop());
//            if (GlobalContext.channelBean.isPrimaryScreenOnTop()) {
//                Intent dialogIntent = new Intent(context, PrimaryScreen.class);
//                dialogIntent.putExtra("Command", "EM");
//                dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                dialogIntent.putExtra("CHL", newMessage.get(1) + "");
//                context.startActivity(dialogIntent);
//            } else {
//
//                final Calendar calendar = Calendar.getInstance();
//                Intent intentAlarm = new Intent(context, AlarmReceiver.class);
//                final PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, RQS_1, intentAlarm, 0);
//                final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent1);
//
//                String chlName = "" + newMessage.get(1);
//                //GlobalContext.setNOTIFICATION_ID(GlobalContext.getNOTIFICATION_ID()+1);
//
//                int requestID = (int) System.currentTimeMillis();
//                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                Intent notificationIntent = new Intent(context, PrimaryScreen.class);
//                notificationIntent.putExtra("Command", "EM");
//                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                notificationIntent.putExtra("CHL", newMessage.get(1) + "");
//                notificationIntent.putExtra("Publish", chlName +" "+ context.getResources().getString(R.string.startPublishing));
//                // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                // Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).extend(wearableExtender).setGroup(GlobalContext.getNOTIFICATION_ID()+"").setNumber(GlobalContext.getNOTIFICATION_ID()+1).setSmallIcon(R.drawable.msgicon).setContentTitle("Takeuz Notification").setGroupSummary(true).setStyle(new NotificationCompat.BigTextStyle().bigText(""+chlName+" On Notification")).setNumber(GlobalContext.getNOTIFICATION_ID()).setContentText(chlName + " "+context.getResources().getString(R.string.startPublishing)).setAutoCancel(true);
//                mBuilder.setContentIntent(contentIntent);
//                mBuilder.setDeleteIntent(contentIntent);
//                mNotificationManager.notify(GlobalContext.getNOTIFICATION_ID(), mBuilder.build());
//                System.out.println("start publishing");
//
//            }
//
//        }






        Intent intent = new Intent(this, DrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.takeuz_logo)
                .setContentTitle("TakeUz")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}