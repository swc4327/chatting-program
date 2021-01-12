package kr.ac.kumoh.s20150686.chatting.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_user_list.*
import kr.ac.kumoh.s20150686.chatting.R
import kr.ac.kumoh.s20150686.chatting.adapter.UserItem

class UserListActivity : AppCompatActivity() {

    private val TAG = UserListActivity::class.java.simpleName

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val adapter = GroupAdapter<GroupieViewHolder>()
        auth = Firebase.auth
        val myUid = auth.uid


        db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document.get("uid").toString() != myUid) {
                            adapter.add(
                                UserItem(
                                    document.get("username").toString(),
                                    document.get("uid").toString()
                                )
                            )
                            Log.d(TAG, "${document.id} => ${document.data}")
                        }
                    }
                    userList.adapter = adapter
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

        adapter.setOnItemClickListener { item, view ->
            val uid = (item as UserItem).uid
            val yourName = (item as UserItem).name
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("yourUid", uid)
            intent.putExtra("yourName", yourName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}