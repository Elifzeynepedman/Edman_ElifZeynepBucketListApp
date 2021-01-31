package ise308.edman.edman_elifzeynep_bucketlistapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException
import java.lang.IllegalStateException
import java.util.logging.Logger

class AddGoalDialog: DialogFragment() {

    companion object {
        const val TAG = "AddGoalDialog"
        val LOG = Logger.getLogger(AddGoalDialog::class.java.name)
    }

    internal lateinit var listener: AddGoalDialogListener

    interface AddGoalDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            var builder = AlertDialog.Builder(context)

            val inflater =requireActivity().layoutInflater

            val dialogView = inflater.inflate(R.layout.activity_add_goal, null)
            val editName = dialogView.findViewById<EditText>(R.id.edit_name)
            val editDesc = dialogView.findViewById<EditText>(R.id.edit_desc)
            val editPlace = dialogView.findViewById<EditText>(R.id.edit_place)
            val editDate = dialogView.findViewById<EditText>(R.id.edit_date)

            builder.setView(dialogView)
                .setPositiveButton(R.string.add,
                    DialogInterface.OnClickListener { dialog, id ->
                        // Persist to DB Action
                        var goalFormInfo = "name: "+editName.text.toString()+", "+
                                "description: "+editDesc.text.toString()+", "
                                "place: "+editPlace.text.toString()+", "
                                "date: "+editDate.text.toString()

                        LOG.warning(goalFormInfo)

                        val goal=
                            Goal()
                        goal.goalName=editName.text.toString()
                        goal.goalDesc=editDesc.text.toString()
                        goal.goalPlace=editPlace.text.toString()
                        goal.goalDate= editDate.text.toString()

                        MainActivity.dbHandler.addGoal(it, goal)
                        Toast.makeText(context,"Input fields saved", Toast.LENGTH_SHORT).show()

                        listener.onDialogPositiveClick(this)
                    })
                .setNegativeButton(R.string.exit,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogNegativeClick(this)
                        getDialog()?.cancel()
                    })
            builder.create()



        } ?: throw IllegalStateException("Activity cannot be null")
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddGoalDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() + " must implement NoticeDialogListener")
            )
        }
    }
}