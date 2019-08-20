package br.com.myslack.feature.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.myslack.model.Channel
import br.com.myslack.model.User
import br.com.myslack.myLib.MD5Encript
import br.com.myslack.repository.AuthRepository
import br.com.myslack.repository.ChannelRepository
import br.com.myslack.repository.UserRepository

class ContactsViewModel : ViewModel() {

    private val usersData : LiveData<List<User>>
    private val userRepository = UserRepository()
    private val authRepository = AuthRepository()
    private val channelRepository = ChannelRepository()

    init {
        usersData = userRepository.users()
    }

    fun observerUsers() = usersData
    fun currentUser() = authRepository.currentUser()


    fun getChannel(user: User): LiveData<Channel> {

        val currentUser = User(
            id = authRepository.currentUser()!!.uid,
            name = authRepository.currentUser()?.displayName,
            email = authRepository.currentUser()?.email,
            photoUrl = authRepository.currentUser()?.photoUrl.toString(),
            lastMessage = authRepository.currentUser()?.phoneNumber)

        val id = MD5Encript.encrypt(currentUser.id + user.id)

        val channel = Channel(
            id,
            "",
            listOf(currentUser.id, user.id),
            listOf(currentUser, user)
        )

        return channelRepository.findOrCreate(channel)
    }


}