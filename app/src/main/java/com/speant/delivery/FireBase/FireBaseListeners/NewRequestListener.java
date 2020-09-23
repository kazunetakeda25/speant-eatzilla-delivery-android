package com.speant.delivery.FireBase.FireBaseListeners;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.FireBase.FireBaseModels.NewRequestFirebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import static com.speant.delivery.Common.SessionManager.KEY_USER_ID;

public class NewRequestListener implements ChildEventListener{
    private static final String TAG = "NewReqListnerService" ;
    private DatabaseReference mDatabaseNewRequest;
    private String userId,request_id;
    private Context context;

    public NewRequestListener() {

    }

    public NewRequestListener(Context context) {
        if(mDatabaseNewRequest == null) {
            this.context = context;
            mDatabaseNewRequest = FirebaseDatabase.getInstance().getReference().child(CONST.Params.new_request);
            mDatabaseNewRequest.addChildEventListener(this);
        }
    }

    /*private void setFirebaseListener() {
        Log.e(TAG, "onCreate:mDatabaseNewRequest KEY_USER_ID " + userId);
        mDatabaseNewRequest = FirebaseDatabase.getInstance().getReference().child(CONST.Params.new_request).child(userId);
        mDatabaseNewRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.e(TAG, "onDataChange:mDatabaseNewRequest map " + map);
                if (map != null) {
                    request_id = String.valueOf(map.get(CONST.Params.request_id));
                    mDatabaseCurrentRequest = FirebaseDatabase.getInstance().getReference().
                            child(CONST.Params.current_request).child(String.valueOf(request_id));

                    mDatabaseCurrentRequest.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            Log.e(TAG, "onDataChange: mDatabaseCurrentRequest map " + map);
                            if (map != null) {
                                request_id = String.valueOf(map.get(CONST.Params.request_id));
                                provider_id = String.valueOf(map.get(CONST.Params.provider_id));
                                status = String.valueOf(map.get(CONST.Params.status));
                                if (status.equalsIgnoreCase("2")) {
                                    isStatusTwoFirst = !isStatusTwoFirst;
                                } else
                                    isStatusTwoFirst = false;
                                Log.e(TAG, "onDataChange: " + request_id + "\t" + provider_id + "\t" + status);
                                HashMap<String, String> requestMap = new HashMap<>();
                                requestMap.put(CONST.Params.request_id, request_id);
                                jsonRequestDetail(requestMap, String.valueOf(status));
                            }
                            if (map == null) {
                                bottomSheetVisibilitychange(String.valueOf(1));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });


                    Log.e(TAG, "onDataChange: " + request_id);
                } else {
                    bottomSheetVisibilitychange(String.valueOf(1));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }*/

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if(dataSnapshot.getKey() !=null){
            Log.e(TAG, "onChildAdded:dataSnapshot getKey"+dataSnapshot.getKey());
            Log.e(TAG, "onChildAdded:dataSnapshot getValue"+dataSnapshot.getValue());
            if(dataSnapshot.getKey().equals(getUserId())){
                //remove node from Available provider list

                FirebaseDatabase.getInstance().getReference().child(CONST.Params.available_providers).child(getUserId()).removeValue();

                NewRequestFirebase newRequestFirebase=dataSnapshot.getValue(NewRequestFirebase.class);

                //sending Request Id to activities
                EventBus.getDefault().post(newRequestFirebase);


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

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.e(TAG, "onChildMoved:dataSnapshot getKey"+dataSnapshot.getKey());
        Log.e(TAG, "onChildMoved:dataSnapshot getValue"+dataSnapshot.getValue());
    }


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public String getUserId() {
        SessionManager sessionManager  = new SessionManager(context);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        userId=userDetails.get(KEY_USER_ID);
        Log.e(TAG, "getUserId:userId "+userId );
        return userId;
    }

    public void removeListener() {
        if(mDatabaseNewRequest != null){
            mDatabaseNewRequest.removeEventListener(this);
        }
    }
}
