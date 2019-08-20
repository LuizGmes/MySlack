package br.com.myslack.feature.channel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.myslack.R
import br.com.myslack.model.Channel
import br.com.myslack.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_contact.view.*

class ChannelAdapter(private val callback: ChannelsCallback? = null) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    private var currentUser: User? = null
    private var items: List<Channel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_contact, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items?.let { holder.bind(currentUser, it[position], callback) }
    }

    fun updateItems(currentUser: User?, items: List<Channel>?) {
        this.currentUser = currentUser
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {

        fun bind(currentUser: User?, channel: Channel, callback: ChannelsCallback?) {
            val user: User? = channel.users?.filterNot { it.id == currentUser?.id }?.firstOrNull()

            Glide.with(itemView.context)
                .load(user?.photoUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.imgUser)

            itemView.tvName.text = user?.name
            itemView.tvMessageSent.text = channel.lastMessage

            itemView.setOnClickListener {
                if (callback != null) {
                    callback.onItemClick(channel)
                }
            }
        }
    }

    interface ChannelsCallback {
        fun onItemClick(channel: Channel)
    }

}