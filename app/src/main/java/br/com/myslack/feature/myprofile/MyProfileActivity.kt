package br.com.myslack.feature.myprofile

import android.content.Intent
import android.os.Bundle
import br.com.myslack.R
import br.com.myslack.feature.base.BaseActivity
import br.com.myslack.feature.login.LoginActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.custom_toolbar_layout.view.*

class MyProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        initActionbar()
        initUI()
    }

    private fun initUI() {
        auth.currentUser?.let { user ->
            tvName.text = user.displayName
            tvEmail.text = user.email
            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(imgUser)
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut().addOnSuccessListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish() }
        }
    }

    private fun initActionbar() {
        setSupportActionBar(toolbarMessages.toolbar)
        toolbarMessages.tvToolbarTitle.text = getString(R.string.my_profile)
    }
}
