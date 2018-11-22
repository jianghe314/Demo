package com.kangzhan.student.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by kangzhan011 on 2017/5/27.
 */

public class InitServiceApp extends IntentService {
    private static final String action="HURRAY UP";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InitServiceApp(String name) {
        super(name);
    }
    public static void start(Context context){
        Intent start=new Intent(context,InitServiceApp.class);
        start.setAction(action);
        context.startService(start);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            String maction=intent.getAction();
            if(maction.equals("HURRAY UP")){
                performInit();
            }
        }
    }

    private void performInit() {

    }
}
