package com.byarbyur.myapplication;


import java.util.List;

public interface iFireStoreLoadDone {
    void onFireStoreLoadSuccess(List<ImagePage> imagePages);
    void onFireStoreLoadFailed(String message);

}
