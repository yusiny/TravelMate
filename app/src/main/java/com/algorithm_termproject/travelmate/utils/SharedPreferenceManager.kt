package com.algorithm_termproject.travelmate.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

/* SharedPreferenceManager*/

/** A function to save name */
fun saveName(context: Context, name: String){
    val spf = context.getSharedPreferences("travelMate", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putString("name", name)
    editor.apply()
}

/** A function to get name */
fun getName(context: Context): String {
    val spf = context.getSharedPreferences("travelMate", AppCompatActivity.MODE_PRIVATE)

    return spf.getString("name", "").toString()
}

/** A function to remove name*/
fun removeName(context: Context){
    val spf = context.getSharedPreferences("travelMate", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.remove("name")
    editor.apply()
}