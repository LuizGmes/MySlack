package br.com.myslack.model

import java.util.*

data class Message( val userId: String? = "",
                    val imageUrl: String? = "",
                    val text: String? = null,
                    val time: Date? = null)