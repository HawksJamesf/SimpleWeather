package com.jamesfchen.template;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jamesfchen.annotations.TraceTime;
import com.jamesfchen.template.ipc.BinderEntry;
import com.jamesfchen.template.ipc.BinderShadow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: jamesfchen
 * @since: Oct/25/2020  Sun
 */
public class StartActivity extends Activity {
    final ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.d("cjf", " " + name + " " + service);
            //在跨进程，远程service将获得BinderProxy;在同一个进程，本地service将获得binder实体。
            if (name.getClassName().equals(LocalService.class.getCanonicalName())) {
                IMyAidlInterface iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
                Log.d("cjf", "iMyAidlInterface:" + iMyAidlInterface);
            } else if (name.getClassName().equals(RemoteService.class.getCanonicalName())) {
//                BinderEntry.asInterface(service);
                Log.d("cjf", "BinderEntry");
                if (service instanceof BinderEntry) {//local
                    ((BinderEntry) service).basicTypes(1, 1, true, 1.0f, 1.0d, "1");
                } else {//remote
                    BinderShadow binderShadow = new BinderShadow(service);
                    try {
                        binderShadow.basicTypes(1, 1, true, 1.0f, 1.0d, "1");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }

            } else if (name.getClassName().equals(MessengerService.class.getCanonicalName())) {
                try {
                    Messenger messenger = new Messenger(service);
                    Message msg = Message.obtain();
                    Bundle b = new Bundle();
                    b.putString("cjf", "client ");
                    msg.setData(b);
                    msg.replyTo = clientMessage;
                    messenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Messenger clientMessage = new Messenger(new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d("cjf","print "+msg.getData().getString("cjf"));
        }
    });

    @TraceTime
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button button = findViewById(R.id.bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Router.select("module1_page").go(StartActivity.this,new Callback(){
//
//                });
            }
        });
        RemoteService.startAndBindService(this, con);
        LocalService.startAndBindService(this, con);
        MessengerService.startAndBindService(this, con);


    }

    @TraceTime
    @Override
    protected void onStart() {
        super.onStart();
//        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; ++i) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        Log.d("cjf", "耗时:" + (System.currentTimeMillis() - start));

    }

    @TraceTime
    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < 5; ++i) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        startActivity(new Intent(this, TestGuardActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RemoteService.stopAndUnbindService(this, con);
        LocalService.stopAndUnbindService(this, con);
        MessengerService.stopAndUnbindService(this, con);
    }
}
