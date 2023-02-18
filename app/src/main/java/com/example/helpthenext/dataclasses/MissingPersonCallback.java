package com.example.helpthenext.dataclasses;

import android.net.Uri;

public interface MissingPersonCallback {
    void loadInfo(MissingPerson missingPerson);
    void loadImage(Uri missingImage);
}
