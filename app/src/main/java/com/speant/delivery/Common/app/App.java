package com.speant.delivery.Common.app;

import android.app.Application;
import android.util.Log;

import com.speant.delivery.Common.SessionManager;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import static com.speant.delivery.Common.SessionManager.KEY_USER_ID;

public class App extends Application {
    DatabaseReference mDatabaseAvailableProv;
    String userId;

    @Override
    public void onCreate() {
        Log.e("TAG", "onCreate:App " );
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetatils = sessionManager.getUserDetails();
        userId = userDetatils.get(KEY_USER_ID);
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        Log.e("TAG", "onTerminate:App");
        /*if(userId != null){
            Log.e("TAG", "onTerminate:userId");
            mDatabaseAvailableProv = FirebaseDatabase.getInstance().getReference().child(CONST.Params.available_providers).child(userId);
            mDatabaseAvailableProv.removeValue();
        }
*/
        super.onTerminate();
    }

}
