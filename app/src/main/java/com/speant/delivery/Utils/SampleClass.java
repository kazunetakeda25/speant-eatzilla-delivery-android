package com.speant.delivery.Utils;

import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Models.DirectionResults;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleClass {
    private DataInterface mListener;

    public SampleClass() {
        super();
    }

    public void getDataForId(final LatLng fromLatLng, final LatLng toLatLng) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<DirectionResults> call = apiInterface.polyLines(CONST.APIKEY, fromLatLng.latitude + "," + fromLatLng.longitude,
                toLatLng.latitude + "," + toLatLng.longitude);
        call.enqueue(new Callback<DirectionResults>() {
            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
                if (response!=null && response.body() != null && mListener != null) {
                    mListener.responseData(response.body());
                }
            }
            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {

            }
        });
    }

    public void setOnDataListener(DataInterface listener) {
        mListener = listener;
    }

    public interface DataInterface {
        void responseData( DirectionResults DirectionResults );
    }
}
