package com.example.weighttracker.adapter

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.weighttracker.R
import com.example.weighttracker.User

class EntriesAdapter (private  val arrWeight: ArrayList<User>, val activity: AppCompatActivity):RecyclerView.Adapter<EntriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = from(parent.context).inflate(R.layout.recycler_view,parent, false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem =arrWeight[position]
        holder.name.text= currentItem.name
        holder.weight.text= currentItem.weight + " Kgs"
        holder.date.text=currentItem.date

        /*holder.constraintentries.setOnClickListener {
            val intent = Intent(activity,Details::class.java)
            activity.startActivity(intent)
            activity.finish()
        }*/
    }

    override fun getItemCount(): Int {
        return arrWeight.size
    }

    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {
        val weight : TextView =itemview.findViewById(R.id.textView_weight)
        val name : TextView =itemview.findViewById(R.id.textView_name)
        val date : TextView =itemview.findViewById(R.id.textView_date)



        val constraintentries: ConstraintLayout= itemview.findViewById(R.id.constraint_entries)
    }
}