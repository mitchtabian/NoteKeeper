package com.jwhh.notekeeper

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*

class NoteListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val activityIntent = Intent(this, NoteActivity::class.java)
            startActivity(activityIntent)
        }

        listNotes.adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                DataManager.notes)

        listNotes.setOnItemClickListener{parent, view, position, id ->
            val activityIntent = Intent(this, NoteActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, position)
            startActivity(activityIntent)
        }


    }

    override fun onResume() {
        super.onResume()
        (listNotes.adapter as ArrayAdapter<NoteInfo>).notifyDataSetChanged()
    }
}






