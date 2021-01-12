package kr.ac.kumoh.s20150686.chatting.adapter

class ChatModel(val myUid : String,
                   val yourUid : String,
                   val message : String,
                   val time : Long,
                   val who : String) {
    constructor() : this("", "", "", 0, "")
}