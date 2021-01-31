package ise308.edman.edman_elifzeynep_bucketlistapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_goal.view.*
import kotlinx.android.synthetic.main.layout_goal_edit.view.*

class GoalAdapter(mCtx: Context, val goals: ArrayList<Goal>): RecyclerView.Adapter<GoalAdapter.ViewHolder>(){

    val mCtx=mCtx

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txt_name= itemView.txt_name
        val txt_description=itemView.txt_description
        val txt_place=itemView.txt_place
        val txt_date=itemView.txt_date
        val imageEdit=itemView.imageView_edit
        val imageDelete=itemView.imageView_delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.layout_goal,parent,false)

        return ViewHolder(
            v
        )

    }

    override fun getItemCount(): Int {
        return goals.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal: Goal = goals[position]
        holder.txt_name.text=goal.goalName
        holder.txt_description.text=goal.goalDesc
        holder.txt_place.text=goal.goalPlace
        holder.txt_date.text=goal.goalDate.toString()

        holder.imageDelete.setOnClickListener {
            val goalName=goal.goalName
            var alertDialog= AlertDialog.Builder(mCtx)
                .setTitle("Warning")
                .setMessage("Are You Sure You Want To delete: $goalName")
                .setPositiveButton("yes", DialogInterface.OnClickListener{ dialog, which->
                    if(MainActivity.dbHandler.deleteGoal(goal.goalName)){
                        goals.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, goals.size)
                        Toast.makeText(mCtx,"Goal $goalName Deleted", Toast.LENGTH_SHORT).show()
                    }else
                        Toast.makeText(mCtx,"Error Deleting", Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener{dialog, which ->  })
              //  .setIcon(R.drawable.ic_baseline_warning_24)
                .show()
        }

        holder.imageEdit.setOnClickListener {
            val inflater= LayoutInflater.from(mCtx)
            val view= inflater.inflate(R.layout.layout_goal_edit, null)

            val txtGoalName: TextView =view.findViewById(R.id.editUpGoalName)
            val txtGoalDesc: TextView=view.findViewById(R.id.editUpGoalDesc)
            val txtGoalPlace: TextView=view.findViewById(R.id.editUpGoalPlace)
            val txtGoalDate: TextView=view.findViewById(R.id.editUpGoalDate)

            txtGoalName.text=goal.goalName
            txtGoalDesc.text=goal.goalDesc
            txtGoalPlace.text=goal.goalPlace
            txtGoalDate.text=goal.goalDate.toString()

            val builder= AlertDialog.Builder(mCtx)
                .setTitle("Edit Goal Information")
                .setView(view)
                .setPositiveButton("Edit", DialogInterface.OnClickListener{dialog, which ->
                    val isEdit=
                        MainActivity.dbHandler.updateGoal(
                        view.editUpGoalName.text.toString(),
                        view.editUpGoalDesc.text.toString(),
                        view.editUpGoalPlace.text.toString(),
                        view.editUpGoalDate.text.toString())
                    if(isEdit==true)
                    {
                        goals[position].goalName=view.editUpGoalName.text.toString()
                        goals[position].goalDesc=view.editUpGoalDesc.text.toString()
                        goals[position].goalPlace=view.editUpGoalPlace.text.toString()
                        goals[position].goalDate=view.editUpGoalDate.text.toString();
                        notifyDataSetChanged()
                        Toast.makeText(mCtx,"Updated Successfully",Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(mCtx,"Error Updating", Toast.LENGTH_SHORT).show()
                    }

                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

                })
            val alert=builder.create()
            alert.show()
        }
    }

}