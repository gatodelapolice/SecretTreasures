package com.cuty.secrettreasures

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth= FirebaseAuth.getInstance()

    }

    //esto es el login
    fun BuLoginEvent(view: View){
        loginToFirebase(etEmail.text.toString(),etPassword.text.toString())
    }
    fun loginToFirebase(email:String,password:String){
        mAuth!!.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext,"Login exitoso",Toast.LENGTH_SHORT).show()
                    var currentUser = mAuth!!.currentUser
                    if (currentUser !=null) {
                        //myRef.child("Users").child(currentUser!!.uid).setValue(currentUser.email)
                        myRef.child("Users").child(SplitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                    }

                    LoadMain()


                }
                else
                {   Toast.makeText(applicationContext,"Login falló",Toast.LENGTH_SHORT).show()}

            }

    }
    override fun onStart() {
        super.onStart()
        //revisar


        LoadMain()


    }

    fun LoadMain(){
        var currentUser = mAuth!!.currentUser
        if (currentUser!=null){
            //save in database

            var intent = Intent(this,Mapa::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)

            startActivity(intent)
        }

    }
    fun SplitString(str:String):String{
        var split = str.split("@")
        return split[0]

    }

}
