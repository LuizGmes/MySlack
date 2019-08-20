package br.com.myslack.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.myslack.model.Message
import com.google.firebase.firestore.FirebaseFirestore

class MessageRepository {

    fun messages(channelId: String?): LiveData<List<Message>> {
        val items = MutableLiveData<List<Message>>()

        FirebaseFirestore.getInstance()
            .collection("channels")
            .document(channelId!!)
            .collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    items.value = it.toObjects(Message::class.java)
                }
            }

        return items
    }

    fun create(channelId: String?, message: Message) {

        FirebaseFirestore.getInstance()
            .collection("channels")
            .document(channelId!!)
            .collection("messages")
            .add(message)

    }
}