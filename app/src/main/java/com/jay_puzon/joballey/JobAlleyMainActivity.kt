package com.jay_puzon.joballey

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JobAlleyMainActivity : AppCompatActivity() {
    private var FName: EditText? = null
    private var MName: EditText? = null
    private var LName: EditText? = null
    private var BtnAdd: Button? = null
    private var BtnView: Button? = null
    private var BtnClear: Button? = null
    private var Conn: SQLiteDB? = SQLiteDB(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_alley_activity_main)

        FName = findViewById(R.id.fName)
        MName = findViewById(R.id.mName)
        LName = findViewById(R.id.lName)
        val nameFields = listOf(
            FName, MName, LName
        )

        BtnAdd = findViewById(R.id.btnAdd)
        BtnView = findViewById(R.id.btnView)
        BtnClear = findViewById(R.id.btnClear)

        BtnView!!.setOnClickListener {
            Log.i("JobAlleyMainActivity", "BtnView clicked!")

            val callRecords = Intent(this, Records::class.java)
            startActivity(callRecords)
        }

        BtnClear!!.setOnClickListener {
            if (!Conn!!.NotEmpty()) {
                Log.i("DeleteRecords", "No records to clear")
                Toast.makeText(this, "No records to clear", Toast.LENGTH_LONG).show()
            }

            if (Conn!!.DeleteRecords()) {
                Log.i("DeleteRecords", "All records have been cleared!")
                Toast.makeText(this, "Records have been cleared!", Toast.LENGTH_LONG).show()
            } else {
                Log.e("DeleteRecords", "Error on clearing records!")
                Toast.makeText(
                    this,
                    "There has been an error on clearing records!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        BtnAdd!!.setOnClickListener {
            Log.i("JobAlleyMainActivity", "BtnAdd clicked!")

            val names = listOf(
                FName!!.text.toString() + "",
                MName!!.text.toString() + "",
                LName!!.text.toString() + ""
            )

            names.forEachIndexed { i, name ->
                if (name == "") {
                    nameFields[i]!!.error = "Please fill up this field!"
                    nameFields[i]!!.requestFocus();
                    return@setOnClickListener
                }
            }

            // check if there's existing record, if there is, don't add
            if (Conn!!.RecordExists(names[0], names[1], names[2])) {
                Log.i("AddRecord", "RECORD ALREADY EXISTS!");
                Toast.makeText(this, "Record already exists", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }

            // check if adding the record was successful
            if (Conn!!.AddRecord(names[0], names[1], names[2])) {
                Log.i("AddRecord", "Record saved!");
                Toast.makeText(this, "Record saved!", Toast.LENGTH_LONG).show()

                // clear all edit text fields
                nameFields.forEach { editText ->
                    editText!!.setText("")
                }
            } else {
                Log.e("AddRecord", "ERROR ON SAVING RECORD!");
                Toast.makeText(this, "ERROR ON SAVING RECORD!", Toast.LENGTH_LONG).show()
            }
        }
    }
}