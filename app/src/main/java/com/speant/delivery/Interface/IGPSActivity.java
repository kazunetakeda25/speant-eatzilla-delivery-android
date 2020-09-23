package com.speant.delivery.Interface;

import android.location.Location;

public interface IGPSActivity {
    public void locationChanged(Location loc);
    public void displayGPSSettingsDialog();
}
