package br.com.myslack.feature.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.myslack.R
import br.com.myslack.model.Message
import br.com.myslack.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_message_sender.view.*
import java.text.SimpleDateFormat

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var currentUser: User? = null
    private var items: List<Message>? = null

    private val SENDER = 0
    private val RECEIVER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layout = R.layout.row_message_receiver
        if ( SENDER.equals(viewType)) {
            layout = R.layout.row_message_sender
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        items?.let {
            if (it[position].userId.equals(currentUser?.id)) return SENDER
        }
        return RECEIVER
    }

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items?.let { holder.bind(currentUser, it[position]) }
    }

    fun updateItems(user: User?, items: List<Message>?) {
        this.currentUser = user
        this.items = items

        notifyDataSetChanged()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(user: User?, message: Message) {

            if ( user?.id != message.userId ) {
                val imgView = itemView.findViewById<ImageView>(R.id.ivMessage)
                if (imgView != null) {
                    Glide.with(itemView.context)
                        .load(message.imageUrl)
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgView)
                }

            }

            itemView.tvMessage.text = message.text
            itemView.tvTime.text = SimpleDateFormat("HH:mm").run({ format(message.time) })

        }
    }
}
