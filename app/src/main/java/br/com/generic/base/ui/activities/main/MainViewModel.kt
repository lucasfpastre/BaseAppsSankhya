package br.com.generic.base.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.generic.base.data.repository.RequestRepository
import br.com.generic.base.models.server.ServerData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val requestRepo : RequestRepository) : ViewModel() {

    val serverData : LiveData<ServerData> = requestRepo.server.getServer().asLiveData()

}