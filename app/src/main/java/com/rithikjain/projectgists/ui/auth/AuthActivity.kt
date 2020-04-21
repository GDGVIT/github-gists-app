package com.rithikjain.projectgists.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.ybq.android.spinkit.style.CubeGrid
import com.github.ybq.android.spinkit.style.FadingCircle
import com.github.ybq.android.spinkit.style.FoldingCube
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.rithikjain.projectgists.R
import com.rithikjain.projectgists.model.Result
import com.rithikjain.projectgists.model.register.RegisterRequest
import com.rithikjain.projectgists.ui.PostAuthActivity
import com.rithikjain.projectgists.util.*
import com.rithikjain.projectgists.util.PrefHelper.set
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.viewmodel.ext.android.viewModel


class AuthActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        val firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, PostAuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authProgress.setIndeterminateDrawable(WanderingCubes())
        authProgress.hide()

        val authViewModel by viewModel<AuthViewModel>()
        val firebaseAuth = FirebaseAuth.getInstance()

        val provider = OAuthProvider.newBuilder("github.com")
        val scopes = listOf("gist")
        provider.setScopes(scopes)

        val pref = PrefHelper.customPrefs(this, Constants.PREF_NAME)

        loginButton.setOnClickListener {
            loginButton.disable()

            val pendingResultTask = firebaseAuth.pendingAuthResult
            if (pendingResultTask != null) {
                pendingResultTask.addOnSuccessListener(
                    OnSuccessListener {
                        shortToast("Success")
                    })
                    .addOnFailureListener {
                        shortToast("Failure")
                    }
            } else {
                firebaseAuth.startActivityForSignInWithProvider(
                    this,
                    provider.build()
                )
                    .addOnSuccessListener {
                        val oAuth = it.credential as OAuthCredential

                        val registerRequest = RegisterRequest(
                            it.user!!.email ?: "",
                            it.user!!.displayName ?: "",
                            oAuth.accessToken
                        )

                        authViewModel.registerUser(registerRequest)
                            .observe(this, Observer { response ->
                                when (response.status) {
                                    Result.Status.LOADING -> {
                                        titleText.hide()
                                        loginButton.hide()
                                        authProgress.show()
                                    }
                                    Result.Status.SUCCESS -> {
                                        pref[Constants.PREF_AUTH_TOKEN] = response.data!!.Token
                                        authProgress.hide()
                                        val intent = Intent(this, PostAuthActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    Result.Status.ERROR -> {
                                        titleText.show()
                                        loginButton.show()
                                        authProgress.hide()
                                        loginButton.enable()
                                        Log.d("esh", response.message)
                                        longToast("Login failed, please try again!")
                                    }
                                }
                            })
                    }
                    .addOnFailureListener {
                        titleText.show()
                        loginButton.show()
                        authProgress.hide()
                        loginButton.enable()
                        shortToast("Authentication Failed, Try Again!")
                    }
            }

        }
    }
}
