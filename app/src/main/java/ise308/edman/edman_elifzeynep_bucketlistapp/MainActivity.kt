package ise308.edman.edman_elifzeynep_bucketlistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(),
    AddGoalDialog.AddGoalDialogListener {

    private val newGoalActivityRequestCode = 1


    companion object{
        lateinit var dbHandler: DBHandler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler =
            DBHandler(
                this,
                null,
                null,
                1
            )
       viewGoals()

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {

        R.id.add_action -> { // executes the add action and displays text at bottom of page
            Toast.makeText(this, "item Add Clicked", Toast.LENGTH_SHORT).show()
            AddGoalDialog().show(supportFragmentManager, AddGoalDialog.TAG)

            true
        }

        R.id.action_settings -> { //displays that the setting was clicked
            Toast.makeText(this,"Setting clicked", Toast.LENGTH_SHORT).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }

    }


     private fun viewGoals(){// Something is wrong with this function
         val goalslist= dbHandler.getGoals(this)
         val adapter= GoalAdapter(
             this,
             goalslist
         )

         val rv: RecyclerView = findViewById(R.id.rv)
         rv.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)
         rv.adapter=adapter
     }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Toast.makeText(this, "Add button in Add Goal Dialog Clicked", Toast.LENGTH_SHORT).show()
        finish();
        overridePendingTransition(0,0)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, "Exit button in Add Goal Dialog Clicked", Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        viewGoals()
        super.onResume()
    }



}