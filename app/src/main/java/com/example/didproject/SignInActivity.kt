package com.example.didproject

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignInActivity : AppCompatActivity() {
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var button:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        mFirebaseAuth= FirebaseAuth.getInstance()
        button=findViewById(R.id.sign_in_button)
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("336911064377-puvugkfbe72f2sp1p4at2f3s25r4e5nu.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val mSignInClient = GoogleSignIn.getClient(this,gso)
        button.setOnClickListener {
            signIn(mSignInClient)
        }
    }

    private fun signIn(gsic:GoogleSignInClient){
        val signInIntent = gsic.signInIntent
        startActivityForResult(signInIntent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                firebaseAuthWithGoogle(task.getResult(ApiException::class.java))
            }catch (e: ApiException){
                Log.w(TAG, "Google sign in failed", e)
            }
        }


    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener(this) {
                startActivity(Intent(this@SignInActivity, MainActivity::class.java),ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

            }
            .addOnFailureListener(
                this
            ) {
                Toast.makeText(
                    this@SignInActivity, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onBackPressed() {

    }
}