package kr.ac.kumoh.s20150686.chatting.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_list.*
import kr.ac.kumoh.s20150686.chatting.R
import kr.ac.kumoh.s20150686.chatting.adapter.ChatModel
import kr.ac.kumoh.s20150686.chatting.adapter.ChatUserItem

class ChatListActivity : AppCompatActivity() {

    private val TAG = ChatListActivity::class.java.simpleName

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        val adapter = GroupAdapter<GroupieViewHolder>()
        auth = Firebase.auth
        val myUid = auth.uid

        val database = Firebase.database



        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.get("uid").toString() != myUid) {
                        val readRef = database.getReference("message").child(myUid.toString()).
                        child(document.get("uid").toString()).limitToLast(1)

                        val childEventListener = object : ChildEventListener {
                            override fun onCancelled(error: DatabaseError) {
                            }
                            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                            }
                            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                            }
                            override fun onChildRemoved(snapshot: DataSnapshot) {
                            }
                            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                                val model = snapshot.getValue(ChatModel::class.java)

                                val msg = model?.message.toString()

                                adapter.add(
                                    ChatUserItem(
                                        document.get("username").toString(),
                                        document.get("uid").toString(),
                                        msg
                                    )
                                )
                            }
                        }
                        readRef.addChildEventListener(childEventListener)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                }
                chatList.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        adapter.setOnItemClickListener { item, view ->
            val uid = (item as ChatUserItem).uid
            val yourName = (item as ChatUserItem).name
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("yourUid", uid)
            intent.putExtra("yourName", yourName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        userList_Button.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }
}