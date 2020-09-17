package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GamingActivityViewModel : ViewModel() {

    val allGamingSettings: LiveData<List<GameSettings>> = MutableLiveData()


}