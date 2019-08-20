package br.com.myslack.feature.channel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.myslack.model.Channel
import br.com.myslack.repository.AuthRepository
import br.com.myslack.repository.ChannelRepository

class ChannelViewModel : ViewModel() {

    private val channelsData : LiveData<List<Channel>>

    private val authRepository = AuthRepository()
    private val channelRepository = ChannelRepository()

    init {
        val uid = currentUser()?.uid
        channelsData = channelRepository.channels(uid)
    }

    fun observeChannels() = channelsData

    fun currentUser() = authRepository.currentUser()

}