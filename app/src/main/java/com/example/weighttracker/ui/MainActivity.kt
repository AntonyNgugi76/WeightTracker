package com.example.weighttracker.ui

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.weighttracker.R
import com.example.weighttracker.databinding.ActivityMainBinding
import com.example.weighttracker.firebase.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    val mainActBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainActBinding.root)

        mainActBinding.buttonSubmittodb.setOnClickListener{
            uploadData()
        }

        mainActBinding.buttonupdatetodb.setOnClickListener{
            updateData()
        }

        mainActBinding.btndelete.setOnClickListener{
            deleteData()
        }

    }

    private fun updateData() {
        if(mainActBinding.edName.text.toString().isNotEmpty()&&mainActBinding.edWeight.text.isNotEmpty()){
            val hashMap = hashMapOf<String, Any>(
                "name" to mainActBinding.edName.text.toString(),
                "weight" to mainActBinding.edWeight.text.toString(),
                "date" to Calendar.getInstance().time.toString(),
            )

            val query_db = FirebaseUtils().firestoredb.collection("weights")
                .whereEqualTo("name",mainActBinding.edName.text.toString())
                .get()
            query_db.addOnSuccessListener {
                for(document in it){
                    FirebaseUtils().firestoredb.collection("weights").document(document.id)
                        .set(hashMap, SetOptions.merge())

                }
                Toast.makeText(this, "Weight Updated Successfully",Toast.LENGTH_SHORT).show()
                mainActBinding.edName.text.clear()
                mainActBinding.edWeight.text.clear()
            }
        } else
        {
            Toast.makeText(this, "User not found",Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteData() {

        if(mainActBinding.edName.text.isNotEmpty()){
            //deleteFromDb()
            val query_db = FirebaseUtils().firestoredb.collection("weights").
            whereEqualTo("name",mainActBinding.edName.text.toString())
                .get()
            query_db.addOnSuccessListener {
                for(document in it){
                    FirebaseUtils().firestoredb.collection("weights").document(document.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                            mainActBinding.edName.text.clear()
                            mainActBinding.edWeight.text.clear()
                        }
                }
            }
        }else
        {
            Toast.makeText(this, "Name should not be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {
        val weight = mainActBinding.edWeight.text.toString()
        val name = mainActBinding.edName.text.toString()
        val date = Calendar.getInstance().time.toString()
        if(weight.isNotEmpty()&&name.isNotEmpty()){
            val hashMap = hashMapOf<String, Any>(
                "name" to name,
                "weight" to weight,
                "date" to date,
            )

            FirebaseUtils().firestoredb.collection("weights")
                .add(hashMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added Successfully",Toast.LENGTH_SHORT).show()
                    mainActBinding.edName.text.clear()
                    mainActBinding.edWeight.text.clear()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error in Adding Weight",Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(this, "Fill in the fields",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            R.id.logout->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.all_entries->{
                val intent= Intent(this, AllEntries::class.java)
                startActivity(intent)
                true
            }
            else-> super.onOptionsItemSelected(item)
        }

    }
}