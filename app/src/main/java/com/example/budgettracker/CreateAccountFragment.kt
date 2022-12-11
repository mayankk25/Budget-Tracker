package com.example.budgettracker

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.databinding.CreateAccountFragmentBinding
import com.example.budgettracker.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateAccountFragment: Fragment() {

    private val TAG = "CreateAccountFragment"
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var status: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding: CreateAccountFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.create_account_fragment, container, false)

        binding.CreateAccountButton2.setOnClickListener {
            val name = binding.createAccountNameInput.text.toString()
            val email = binding.createAccountEmailInput.text.toString()
            val password = binding.createAccountPasswordInput.text.toString()
            if (name != "" && email != "" && password != "") {
                createAccount(email, password, name)
            } else {
                if (name == "") {
                    binding.createAccountName.isErrorEnabled = true
                    binding.createAccountName.error = getString(R.string.empty_email)
                } else { binding.createAccountName.isErrorEnabled = false }
                if (email == "") {
                    binding.createAccountEmail.isErrorEnabled = true
                    binding.createAccountEmail.error = getString(R.string.empty_email)
                } else { binding.createAccountEmail.isErrorEnabled = false }
                if (password == "") {
                    binding.createAccountPassword.isErrorEnabled = true
                    binding.createAccountPassword.error = getString(R.string.empty_email)
                } else { binding.createAccountPassword.isErrorEnabled = false }
            }
        }

        binding.imageView.setImageResource(R.drawable.wallet)

        //Initialize Firebase
        auth = Firebase.auth
        return binding.root
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null) {
            currentUser?.reload();
        }
    }

    private fun createAccount(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                Toast.makeText(context, "Created Account.", Toast.LENGTH_SHORT).show()

                val userData = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "id" to user?.uid
                )

                db.collection("users")
                    .add(userData)
                    .addOnSuccessListener { documentReference ->
                        Log.i(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        Toast.makeText(context, "Created collection.", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(CreateAccountFragmentDirections.actionCreateAccountFragmentToLoginFragment())
                    }
                    .addOnFailureListener { e ->
                        Log.i(TAG, "Error adding document", e)
                        Toast.makeText(context, "problem with collection.", Toast.LENGTH_SHORT).show()
                    }
                //updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }
        }
    }
}