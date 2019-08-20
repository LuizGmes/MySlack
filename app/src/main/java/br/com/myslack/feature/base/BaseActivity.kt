package br.com.myslack.feature.base

import androidx.appcompat.app.AppCompatActivity
import br.com.myslack.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    protected val auth = FirebaseAuth.getInstance()

    protected val googleSignInClient by lazy {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso)
    }

}