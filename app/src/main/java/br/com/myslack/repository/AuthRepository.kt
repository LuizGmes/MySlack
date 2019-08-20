package br.com.myslack.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository() {

    fun currentUser() = FirebaseAuth.getInstance().currentUser

}