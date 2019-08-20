package br.com.myslack.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.myslack.model.Channel
import com.google.firebase.firestore.FirebaseFirestore

class ChannelRepository {

    fun channels(id: String?): LiveData<List<Channel>> {
        val items = MutableLiveData<List<Channel>>()

        FirebaseFirestore.getInstance()
            .collection("channels")
            .whereArrayContains("userIds", id!!)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    val channels = it.toObjects(Channel::class.java)
                    items.value = channels
                }
            }

        return items
    }

    fun find(channelId: String?): LiveData<Channel> {
        val item = MutableLiveData<Channel>()

        FirebaseFirestore
            .getInstance()
            .collection("channels")
            .whereEqualTo("id", channelId)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    val channels = it.toObjects(Channel::class.java)
                    item.value = channels.first()
                }
            }

        return item
    }

    fun findOrCreate(channel: Channel): LiveData<Channel> {
        val item = MutableLiveData<Channel>()

        val collection = FirebaseFirestore.getInstance().collection("channels")

        collection
            .whereEqualTo("userIds", channel.userIds)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    val channels = it.toObjects(Channel::class.java)

                    if (!channels.isEmpty()) {

                        item.value = channels.first()

                    } else {

                        collection
                            .document(channel.id!!)
                            .set(channel)
                            .addOnSuccessListener {
                                item.value = channel
                            }

                    }

                }
            }

        return item
    }

    fun updateLastMessage(channelId: String?, text: String) {

        FirebaseFirestore
            .getInstance()
            .collection("channels")
            .document(channelId!!)
            .update("lastMessage", text)

    }

}