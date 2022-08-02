package com.example.weighttracker.ui

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weighttracker.R
import com.example.weighttracker.User
import com.example.weighttracker.adapter.EntriesAdapter
import com.example.weighttracker.databinding.ActivityAllEntriesBinding
import com.example.weighttracker.firebase.FirebaseUtils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class AllEntries : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var myadapter: EntriesAdapter
    private lateinit var rviewHome: RecyclerView
    private lateinit var arrWeight: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_entries)
        rviewHome= findViewById(R.id.recycler_home)
        rviewHome.layoutManager= LinearLayoutManager(this)
        rviewHome.setHasFixedSize(true)
        arrWeight= arrayListOf()
        myadapter= EntriesAdapter(arrWeight, this@AllEntries)
        rviewHome.adapter=myadapter

        getEntries()

    }
    private fun getEntries() {
        db= FirebaseFirestore.getInstance()
        db.collection("weights")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING )
            .addSnapshotListener(object : EventListener<QuerySnapshot>
                {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            Log.e("Error", error.message.toString())
                            return
                        }
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                arrWeight.add(dc.document.toObject(User::class.java))
                            }

                        }
                        myadapter.notifyDataSetChanged()
                    }

                    })
    }
}