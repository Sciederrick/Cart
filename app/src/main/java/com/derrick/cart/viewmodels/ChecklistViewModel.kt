package com.derrick.cart.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.derrick.cart.repository.CartRepository
import com.derrick.cart.database.CartRoomDatabase
import com.derrick.cart.models.Checklist

class ChecklistViewModel(application: Application): AndroidViewModel(application) {
    private val cartRepository = CartRepository(CartRoomDatabase.getInstance(application))

    val checklists = cartRepository.allChecklists

    private val _checklists = MutableLiveData<List<Checklist>>()
}