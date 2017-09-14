package com.sweetcompany.sweetie.actionInfo;


public interface ActionInfoVM {
    String getTitle();
    String getCreationDate();
    String getUriCover();

    boolean isUploadingImg();
    int getProgress();
}
