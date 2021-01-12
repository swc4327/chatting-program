package kr.ac.kumoh.s20150686.chatting.adapter

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_chatlist.view.*
import kr.ac.kumoh.s20150686.chatting.R

class ChatUserItem(val name:String, val uid: String, val msg: String): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item_chatlist
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name.text = name
        viewHolder.itemView.msg.text = msg
    }

}