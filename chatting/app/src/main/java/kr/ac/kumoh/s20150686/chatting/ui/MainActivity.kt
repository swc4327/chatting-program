package kr.ac.kumoh.s20150686.chatting.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kr.ac.kumoh.s20150686.chatting.R
import kr.ac.kumoh.s20150686.chatting.adapter.User


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private var TAG = MainActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        join_button.setOnClickListener {
            val email = email_area.text.toString()
            val password = password_area.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Log.d(TAG, "성공")


                        val uid = Firebase.auth.uid ?:""
                        val user = User(
                            uid,
                            username.text.toString()
                        )
                        val db = Firebase.firestore
                            .collection("users")
                        db.document(uid).set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "데이터베이스 성공")
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "데이터베이스 실패")
                            }

                        val intent = Intent(this, ChatListActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Log.d(TAG, "실패")
                    }
                }
        }
        login_button_main.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}