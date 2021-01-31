package ise308.edman.edman_elifzeynep_bucketlistapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.activity_add_goal.*

class AddGoalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

            if(edit_desc.text.isEmpty()){

                Toast.makeText(this,"Enter goal description", Toast.LENGTH_SHORT).show()
            }
            else if(edit_place.text.isEmpty()){
                Toast.makeText(this,"Enter goal place", Toast.LENGTH_SHORT).show()
            }
            else{
                val goal= Goal()
                goal.goalName=edit_name.text.toString()
                if(edit_desc.text.isEmpty())
                    goal.goalDate= edit_date.text.toString()
                else
                    goal.goalDesc= edit_desc.text.toString(
                    )
                MainActivity.dbHandler.addGoal(this, goal)
                Toast.makeText(this,"Input fields saved", Toast.LENGTH_SHORT).show()
                //clearEdits()
                edit_name.requestFocus()
            }
        }


    }

