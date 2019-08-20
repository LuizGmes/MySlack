package br.com.myslack.model

import java.io.Serializable

data class User(val id: String = "",
                val name: String? = null,
                val email: String? = null,
                val photoUrl: String? = null,
                val lastMessage: String? = null):Serializable