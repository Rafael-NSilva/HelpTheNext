package com.example.helpthenext.dataclasses;

import android.net.Uri;

import java.util.List;

public interface UserInfoCallback {
    public void loadUserInfo(UserInformation userInformation);
    public void loadUsersInfo(List<UserInformation> userInformationList);
    public void loadImage(Uri userImage);
}
