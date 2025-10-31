package com.example.assignmentq2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    MutableLiveData<String> lastRating = new MutableLiveData<>();

    public void setLastRating(String rating){
        lastRating.setValue(rating);
    }

    public LiveData<String> getLastRating(){
        return lastRating;
    }
}