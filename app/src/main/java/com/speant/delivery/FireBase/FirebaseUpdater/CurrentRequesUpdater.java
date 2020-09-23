package com.speant.delivery.FireBase.FirebaseUpdater;

import com.speant.delivery.Common.CONST;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CurrentRequesUpdater {
    DatabaseReference mDatabaseCurrentRequest;
    public CurrentRequesUpdater(String requestId) {
        mDatabaseCurrentRequest = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_request).child(requestId);
    }
}
