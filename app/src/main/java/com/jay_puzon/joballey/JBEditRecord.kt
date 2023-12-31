package com.jay_puzon.joballey

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JBEditRecord : AppCompatActivity() {
    private var FName: EditText? = null
    private var MName: EditText? = null
    private var LName: EditText? = null
    private var BtnUpdate: Button? = null
    private var BtnDelete: Button? = null
    private var BtnBack: Button? = null
    private var Conn: JBSQLiteDB? =
        JBSQLiteDB(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jb_edit_record_activity)

        // retrieve the extra values
        val extras = intent.extras;
        val id = extras!!.getInt(JBSQLiteDB.PROF_ID);
        val fName = extras.getString(JBSQLiteDB.PROF_FNAME);
        val mName = extras.getString(JBSQLiteDB.PROF_MNAME);
        val lName = extras.getString(JBSQLiteDB.PROF_LNAME);

        // set the values to the fields
        val fNameField = findViewById<EditText>(R.id.fName);
        val mNameField = findViewById<EditText>(R.id.mName);
        val lNameField = findViewById<EditText>(R.id.lName);
        fNameField.setText(fName);
        mNameField.setText(mName);
        lNameField.setText(lName);

        // assign edittext fields
        FName = findViewById(R.id.fName)
        MName = findViewById(R.id.mName)
        LName = findViewById(R.id.lName)
        val nameFields = listOf(
            FName, MName, LName
        )

        // assign button views
        BtnDelete = findViewById(R.id.btnDelete)
        BtnUpdate = findViewById(R.id.btnUpdate)
        BtnBack = findViewById(R.id.btnBack)

        BtnBack!!.setOnClickListener {
            Log.i("JobAlleyEditRecord", "BtnBack clicked!")
            finish()
        }

        BtnDelete!!.setOnClickListener {
            // delete the entry in db, check if it's successful
            if (Conn!!.DeleteRecord(id)) {
                Log.i("BtnEdit", "Record deleted!");
                Toast.makeText(this, "Record deleted!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.i("BtnEdit", "Record deletion unsuccessful!");
                Toast.makeText(this, "Record deletion unsuccessful!", Toast.LENGTH_SHORT).show()
            }
        }

        BtnUpdate!!.setOnClickListener {
            Log.i("JobAlleyEditRecord", "BtnEdit clicked!")

            // get the values from the edit text fields
            val names = listOf(
                FName!!.text.toString() + "",
                MName!!.text.toString() + "",
                LName!!.text.toString() + ""
            )

            // check if each fields have values
            names.forEachIndexed { i, name ->
                if (name == "") {
                    nameFields[i]!!.error = "Please fill up this field!"
                    nameFields[i]!!.requestFocus();
                    return@setOnClickListener
                }
            }

            // check if the new values conflict with existing record (except for the current record)
            if (Conn!!.RecordExists(names[0], names[1], names[2], id)) {
                Log.i("BtnUpdate", "Conflicts with existing record!");
                Toast.makeText(this, "Conflicts with existing record!", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }

            // update the entry in db, check if it's successful
            val success = Conn!!.UpdateRecord(
                id,
                names[0],
                names[1],
                names[2]
            )
            if (success) {
                Log.i("BtnEdit", "Record updated!");
                Toast.makeText(this, "Record updated!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.i("BtnEdit", "Error saving changes");
                Toast.makeText(this, "Error saving changes", Toast.LENGTH_SHORT).show()
            }
        }
    }
}