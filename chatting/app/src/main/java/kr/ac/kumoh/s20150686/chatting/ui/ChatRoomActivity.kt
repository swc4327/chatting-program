package kr.ac.kumoh.s20150686.chatting.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_room.*
import kr.ac.kumoh.s20150686.chatting.MyChat
import kr.ac.kumoh.s20150686.chatting.R
import kr.ac.kumoh.s20150686.chatting.adapter.ChatModel
import kr.ac.kumoh.s20150686.chatting.adapter.YourChat

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    private val TAG = ChatRoomActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        auth = Firebase.auth

        val myUid = auth.uid
        val yourUid = intent.getStringExtra("yourUid")
        val yourName = intent.getStringExtra("yourName")
        Log.d(TAG, yourName.toString())


        val adapter = GroupAdapter<GroupieViewHolder>()

        val database = Firebase.database
        val myRef = database.getReference("message")
        val readRef = database.getReference("message").child(myUid.toString()).child(yourUid.toString())

        val childEventListener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val model = snapshot.getValue(ChatModel::class.java)

                val msg = model?.message.toString()
                val who = model?.who


                if (who == "me") {
                    adapter.add(MyChat(msg))

                }else {
                    adapter.add(
                        YourChat(
                            yourName.toString(),
                            msg
                        )
                    )
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        }
        readRef.addChildEventListener(childEventListener)
        recyclerView_chatRoom.adapter = adapter



        //전송
        button.setOnClickListener {
            val message = editText.text.toString()
            val chat = ChatModel(
                myUid.toString(), yourUid.toString(), message, System.currentTimeMillis()
                , "me"
            )
            myRef.child(myUid.toString()).child(yourUid.toString()).push().setValue(chat)

            val chat_get = ChatModel(
                yourUid.toString(), myUid.toString(), message, System.currentTimeMillis()
                , "you"
            )
            myRef.child(yourUid.toString()).child(myUid.toString()).push().setValue(chat_get)
            editText.setText("")
        }

        chatList_Button.setOnClickListener {
            val intent = Intent(this, ChatListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}