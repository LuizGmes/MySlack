package br.com.myslack.feature.Chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.myslack.R
import br.com.myslack.feature.message.MessageAdapter
import br.com.myslack.feature.message.MessageViewModel
import br.com.myslack.feature.myprofile.MyProfileActivity
import br.com.myslack.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.custom_toolbar_chat.view.*


class ChatActivity : AppCompatActivity() {

    private val adapter = MessageAdapter()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MessageViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val channelId = intent.extras?.getString("channelId")

        initUI(channelId)
        initObservers(channelId)
    }

    private fun initUI(channelId: String?) {

        rvChat.adapter = adapter
        rvChat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
            scrollToPosition(adapter.itemCount - 1);
        }

        fbSend.setOnClickListener {
            val message = tvChatMessage.text.toString()
            if (message.isNotBlank()) {
                tvChatMessage.text.clear()
                viewModel.sendMessage(channelId, message)
            }

            tvChatMessage.findFocus()

            (rvChat.layoutManager as LinearLayoutManager).scrollToPosition(adapter.getItemCount() - 1);
        }

    }

    private fun initActionBar(user: User?) {
        setSupportActionBar(toolbar_chat as Toolbar)

        val intent = Intent(this, MyProfileActivity::class.java).apply {
            putExtra("userId", user?.id)
        }

        toolbar_chat.imgToolbarUser.setOnClickListener { startActivity(intent) }

        toolbar_chat.tvToolbarUserName.text = user?.name

        Glide.with(this)
            .load(user?.photoUrl)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .apply(RequestOptions.circleCropTransform())
            .into(toolbar_chat.imgToolbarUser)
    }

    private fun initObservers(channelId: String?) {

        viewModel.observeChannel(channelId).observe(this, Observer { channel ->
                val user = channel.users?.filterNot { it.id == viewModel.currentUser()?.uid }?.firstOrNull()
                initActionBar(user)
            })

        viewModel.observeMessages(channelId).observe(this, Observer {

            val currentUser = User(
                id = viewModel.currentUser()!!.uid,
                name = viewModel.currentUser()?.displayName,
                email = viewModel.currentUser()?.email,
                photoUrl = viewModel.currentUser()?.photoUrl.toString(),
                lastMessage = viewModel.currentUser()?.phoneNumber)


            adapter.updateItems( currentUser, it)

           (rvChat.layoutManager as LinearLayoutManager).scrollToPosition(adapter.getItemCount() - 1);

        })

    }
}
