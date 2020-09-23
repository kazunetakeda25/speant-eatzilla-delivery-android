package com.speant.delivery.FireBase.FireBaseListeners;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.FireBase.FireBaseModels.CurrentRequestFirebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

public class DeliveryStatusListener implements ChildEventListener {
    private static final String TAG = "DeliveryStatusListener" ;
    Context context;
    String userId;
    DatabaseReference mDatabaseCurrentRequest;
    SessionManager sessionManager;

    public DeliveryStatusListener(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        sessionManager  = new SessionManager(context);
        mDatabaseCurrentRequest = FirebaseDatabase.getInstance().getReference().child(CONST.Params.new_request);
        mDatabaseCurrentRequest.addChildEventListener(this);
       /* Query query = mDatabaseCurrentRequest.equalTo(userId).limitToLast(1);
        query.addChildEventListener(this);*/
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if(dataSnapshot.getKey() !=null){
            Log.e(TAG, "onChildAdded:dataSnapshot getKey"+dataSnapshot.getKey());
            Log.e(TAG, "onChildAdded:dataSnapshot getValue"+dataSnapshot.getValue());
            if(dataSnapshot.getKey().equals(userId)){
                Log.e(TAG, "onChildAdded:dataSnapshot getKey userId"+dataSnapshot.getKey());
                CurrentRequestFirebase currentRequestFirebase=dataSnapshot.getValue(CurrentRequestFirebase.class);
                sessionManager.setRequestId(currentRequestFirebase.getRequest_id());
                //sending Request Id to activities
                EventBus.getDefault().post(currentRequestFirebase);
            }
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.e(TAG, "onChildChanged:dataSnapshot getKey"+dataSnapshot.getKey());
        Log.e(TAG, "onChildChanged:dataSnapshot getValue"+dataSnapshot.getValue());
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.e(TAG, "onChildRemoved:dataSnapshot getKey"+dataSnapshot.getKey());
        Log.e(TAG, "onChildRemoved:dataSnapshot getValue"+dataSnapshot.getValue());
        if(dataSnapshot.getKey() !=null){
            if(dataSnapshot.getKey().equals(userId)){
                sessionManager.removeRequestId();
            }
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.e(TAG, "onChildMoved:dataSnapshot getKey"+dataSnapshot.getKey());
        Log.e(TAG, "onChildMoved:dataSnapshot getValue"+dataSnapshot.getValue());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public void removeListener() {
        if(mDatabaseCurrentRequest != null){
            mDatabaseCurrentRequest.removeEventListener(this);
        }
    }

    public void removeValue() {
        if(userId != null && mDatabaseCurrentRequest != null) {
            mDatabaseCurrentRequest.child(userId).setValue(null);
            sessionManager.removeRequestId();
        }else{
            Toast.makeText(context,"Login to user",Toast.LENGTH_SHORT).show();
        }
    }

}
