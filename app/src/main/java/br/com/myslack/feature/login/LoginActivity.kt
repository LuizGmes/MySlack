package br.com.myslack.feature.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.myslack.R
import br.com.myslack.feature.base.BaseActivity
import br.com.myslack.feature.channel.ChannelActivity
import br.com.myslack.feature.contacts.ContactsActivity
import br.com.myslack.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignIn.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, GOOGLE_AUTH_REQUEST)
        }

        val fb = FirebaseFirestore.getInstance()

        fb.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    if (!querySnapshot.isEmpty) {

                        val users = querySnapshot.toObjects(User::class.java)

                        users.forEach {
                            Log.d("LULA", it.name)
                        }
                    }
                }

            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_AUTH_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    updateUI(null)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.let { user ->
                        updateUI(user)
                        saveUser(user)
                    }
                } else {
                    updateUI(null)
                }
                hideProgressBar()
            }
    }

    private fun saveUser(firebaseUser: FirebaseUser) {

        val user = User(
            firebaseUser.uid,
            firebaseUser.displayName,
            firebaseUser.email,
            firebaseUser.photoUrl.toString()
        )

        FirebaseFirestore.getInstance().collection("users")
            .document(firebaseUser.uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("LULA", "Cadastrou")
            }
            .addOnFailureListener {
                Log.e("LULA", "Erro", it)
            }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        hideProgressBar()
        if (firebaseUser != null) {
            startActivity(Intent(this, ChannelActivity::class.java))
            finish()
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    companion object {
        const val GOOGLE_AUTH_REQUEST = 123
    }



}
