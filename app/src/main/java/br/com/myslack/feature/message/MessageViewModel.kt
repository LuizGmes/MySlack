package br.com.myslack.feature.message

import androidx.lifecycle.ViewModel
import br.com.myslack.model.Message
import br.com.myslack.repository.AuthRepository
import br.com.myslack.repository.ChannelRepository
import br.com.myslack.repository.MessageRepository
import java.util.*

class MessageViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val channelRepository = ChannelRepository()
    private val messageRepository = MessageRepository()

    fun currentUser() = authRepository.currentUser()

    fun observeMessages(channelId: String?) = messageRepository.messages(channelId)

    fun observeChannel(channelId: String?) = channelRepository.find(channelId)

    fun sendMessage(channelId: String?, text: String) {
        val currentUser = currentUser()
        val message = Message(
            currentUser?.uid,
            currentUser?.photoUrl.toString(),
            text,
            Date())

        messageRepository.create(channelId, message)
        channelRepository.updateLastMessage(channelId, text)
    }
}