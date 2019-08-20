package br.com.myslack.feature.channel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.myslack.R
import br.com.myslack.feature.Chat.ChatActivity
import br.com.myslack.feature.base.BaseActivity
import br.com.myslack.feature.contacts.ContactsActivity
import br.com.myslack.feature.myprofile.MyProfileActivity
import br.com.myslack.model.Channel
import br.com.myslack.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_channel.*
import kotlinx.android.synthetic.main.custom_toolbar_layout.view.*

class ChannelActivity : BaseActivity(), ChannelAdapter.ChannelsCallback {

    private val adapter = ChannelAdapter(this)

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ChannelViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        initActionBar()
        initUI()
        initObservables()
    }

    private fun initUI() {
        rvChannels.layoutManager = LinearLayoutManager(this)
        rvChannels.adapter = adapter
    }

    private fun initActionBar() {
        setSupportActionBar(toolbarChannels.toolbar)
        toolbarChannels.tvToolbarTitle.text = getString(R.string.channels)

        toolbarChannels.imgToolbarUser.setOnClickListener {
            startActivity(Intent(this, MyProfileActivity::class.java))
        }

        viewModel.currentUser()?.let { user ->
            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(toolbarChannels.imgToolbarUser)
        }
    }

    override fun onItemClick(channel: Channel) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("channelId", channel.id)
        }

        startActivity(intent)
    }

    private fun initObservables() {
        viewModel.observeChannels().observe(this, Observer {
            val currentUser = User(
                id = viewModel.currentUser()!!.uid,
                name = viewModel.currentUser()?.displayName,
                email = viewModel.currentUser()?.email,
                photoUrl = viewModel.currentUser()?.photoUrl.toString(),
                lastMessage = viewModel.currentUser()?.phoneNumber)

            adapter.updateItems(currentUser, it)
        })
    }
    fun addContact(view: View) {
        startActivity(Intent(this, ContactsActivity::class.java))
    }
}
