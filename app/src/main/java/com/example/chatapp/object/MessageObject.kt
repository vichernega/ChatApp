package com.example.chatapp.`object`

object MessageObject {
    var authorId: String = ""
    var authorName: String = ""
    var time: String = ""
    var text: String = ""
    var image: String = ""

    fun set(authorId: String, authorName: String, time: String, text: String, image: String){
        this.authorId = authorId
        this.authorName = authorName
        this.time = time
        this.text = text
        this.image = image
    }

}