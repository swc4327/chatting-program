package kr.ac.kumoh.s20150686.chatting.adapter

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.your_chat.view.*
import kr.ac.kumoh.s20150686.chatting.R

class YourChat(val name: String, val msg : String): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.your_chat
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.left_msg.text = msg
        viewHolder.itemView.chatRoom_yourName.text = name
    }
}