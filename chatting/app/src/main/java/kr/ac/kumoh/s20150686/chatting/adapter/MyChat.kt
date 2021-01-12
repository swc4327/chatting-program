package kr.ac.kumoh.s20150686.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.my_chat.view.*

class MyChat(val msg : String): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.my_chat
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.right_msg.text = msg
    }
}