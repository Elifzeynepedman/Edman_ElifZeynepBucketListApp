package ise308.edman.edman_elifzeynep_bucketlistapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version : Int)
    : SQLiteOpenHelper(context,
    DATABASE_NAME, factory,
    DATABASE_VERSION
){

    companion object{
        private val DATABASE_NAME= "MyData.db"
        private val DATABASE_VERSION=1

        val GOAL_TABLE_NAME="goal"
        val COLUMN_ID="id"
        val COLUMN_GOALNAME="goalname"
        val COLUMN_GOALDESC="goalDesc"
        val COLUMN_GOALPLACE="goalplace"
        val COLUMN_GOALDATE="goaldate"
    }

    override fun onCreate(db: SQLiteDatabase?) { // creates the sql table for the goal database
        val CREATE_GOALS_TABLE=("CREATE TABLE $GOAL_TABLE_NAME (" +
                "$COLUMN_ID INTEGER  PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_GOALNAME STRING, " +
                "$COLUMN_GOALDESC TEXT," +
                "$COLUMN_GOALPLACE TEXT," +
                "$COLUMN_GOALDATE STRING)")
        db?.execSQL(CREATE_GOALS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun getGoals(mCtx: Context): ArrayList<Goal>{
        val qry="Select * From $GOAL_TABLE_NAME" //selects everything from GOAL_TABLE_NAME
        val db=this.readableDatabase
        val cursor=db.rawQuery(qry,null)
        val goals=ArrayList<Goal>()

        if(cursor.count==0) // if the table is empty displays "no records found"
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show()
        else
        {
            cursor.moveToFirst()
            while (!cursor.isAfterLast()){
                val goal= Goal()
                goal.goalName=cursor.getString(cursor.getColumnIndex(COLUMN_GOALNAME))
                goal.goalDesc=cursor.getString(cursor.getColumnIndex(COLUMN_GOALDESC))
                goal.goalPlace=cursor.getString(cursor.getColumnIndex(COLUMN_GOALPLACE))
                goal.goalDate=cursor.getString(cursor.getColumnIndex(COLUMN_GOALDATE))
                goals.add(goal)
                cursor.moveToNext()
            }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return goals
    }
    fun addGoal(mCtx:Context, goal: Goal){ //adds the goals
        val values= ContentValues()
        values.put(COLUMN_GOALNAME,goal.goalName)
        values.put(COLUMN_GOALDESC,goal.goalDesc)
        values.put(COLUMN_GOALPLACE,goal.goalPlace)
        values.put(COLUMN_GOALDATE,goal.goalDate)
        val db= this.writableDatabase
        try { // if this does not work catch an exception
            db.insert(GOAL_TABLE_NAME,null,values)
            //   db.rawQuery("Insert Into $GOAL_TABLE_NAME ($GOAL_NAME, $GOAL_DESCRIPTION, $GOAL_PLACE, $GOAL_DATE) Values(?,?)")
            Toast.makeText(mCtx, "Goal Added", Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            Toast.makeText(mCtx,e.message,Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteGoal(goalName: String): Boolean { // deletes from the database using the goal name
        val qry="Delete From $GOAL_TABLE_NAME where $COLUMN_GOALNAME=$COLUMN_GOALNAME"
        val db=this.writableDatabase
        var result: Boolean= false
        try{
            val cursor=db.execSQL(qry)
            result =true
        }catch (e: Exception){// if cannot delete for a reason tells that there is an "error deleting"
            Log.e(ContentValues.TAG, "Error Deleting: " )
        }
        db.close()
        return result
    }

    fun updateGoal(name: String, description: String, place: String, date: String): Boolean { //updates the goals
        val db=this.writableDatabase
        val contentValues=ContentValues()
        var result: Boolean=false
        contentValues.put(COLUMN_GOALNAME, name)
        contentValues.put(COLUMN_GOALDESC,description)
        contentValues.put(COLUMN_GOALPLACE,place)
        contentValues.put(COLUMN_GOALDATE, date.toInt())
        try {
            db.update(GOAL_TABLE_NAME, contentValues, "$COLUMN_GOALNAME=?", arrayOf(name))
            result=true
        }catch (e: Exception){
            result=false
        }
        return result

    }
}