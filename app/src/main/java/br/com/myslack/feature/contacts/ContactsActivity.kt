package br.com.myslack.feature.contacts

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.myslack.R
import br.com.myslack.feature.Chat.ChatActivity
import br.com.myslack.feature.base.BaseActivity
import br.com.myslack.feature.channel.ChannelActivity
import br.com.myslack.feature.myprofile.MyProfileActivity
import br.com.myslack.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.custom_toolbar_layout.view.*

class ContactsActivity : BaseActivity(), ContactsAdapter.ContactsCallback {

    val adapter = ContactsAdapter(this)

    val viewModel by lazy {
        ViewModelProviders.of(this).get(ContactsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        initActionbar()
        initUI()
        initObservables()
    }

    private fun initUI() {
        rvContacts.layoutManager = LinearLayoutManager(this)
        rvContacts.adapter = adapter
    }

    private fun initActionbar() {
        setSupportActionBar(toolbarContacts.toolbar)

        toolbarContacts.tvToolbarTitle.text = getString(R.string.contacts)
        toolbarContacts.imgToolbarUser.setOnClickListener {
            startActivity(Intent(this, MyProfileActivity::class.java))
        }

        viewModel.currentUser()?.let { user ->
            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(toolbarContacts.imgToolbarUser)
        }
    }

    override fun onItemClick(user: User) {
        viewModel.getChannel(user).observe(this, Observer { channel ->

            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("channelId", channel.id)
            }

            startActivity(intent)
            finish()
        })
    }

    private fun initObservables() {
        viewModel.observerUsers().observe(this, Observer {
            adapter.updataItems(it)
        })
    }
}
