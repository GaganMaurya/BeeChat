package com.example.cryptoo

import android.content.Intent
import android.content.res.Resources.Theme
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoo.Adapter.totaluseradapter

import com.example.cryptoo.databinding.ActivitySearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class SearchActivity : AppCompatActivity() {
    lateinit var b: ActivitySearchBinding
    lateinit var totaluser: ArrayList<totaluserclass>
    lateinit var  databaseReference: FirebaseDatabase
    lateinit var adapter: totaluseradapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(b.root)
        setTheme(R.style.Theme_new)

        databaseReference =  FirebaseDatabase.getInstance()

       setSupportActionBar(b.toolbar)
       supportActionBar?.setDisplayHomeAsUpEnabled(true)


        totaluser =  ArrayList()
        totaluser()



    }

    fun totaluser(){

     b.chatrv.layoutManager =  LinearLayoutManager(this)

        databaseReference.reference.child("User")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        for(i in snapshot.children) {
                            val us = i.getValue(totaluserclass::class.java)
                            if (us?.uid != FirebaseAuth.getInstance().uid) {
                                totaluser.add(us!!)
                            }
                            adapter = totaluseradapter(this@SearchActivity, totaluser)
                            b.chatrv.adapter =  adapter
                            b.progressBar.visibility = View.GONE
                        }

                    }else {
                        Toast.makeText(this@SearchActivity, "Nothing to show", Toast.LENGTH_SHORT).show()
                    }


                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@SearchActivity,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
     menuInflater.inflate(R.menu.menu , menu)

        var mi  = menu!!.findItem(R.id.search1)
        val sv =  mi.actionView as SearchView
        sv.queryHint = "Search here"

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                nexttext(newText)
                return true
            }


        })




        return super.onCreateOptionsMenu(menu)



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.search1 ->{
//                val s = item.actionView as SearchView
//                s.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        return false
//                    }
//
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        nexttext(newText)
//                        return true
//                    }
//
//
//                })
            }
            R.id.settings ->{
                Toast.makeText(this@SearchActivity, "settings", Toast.LENGTH_SHORT).show()
            }
        }


        return super.onOptionsItemSelected(item)
    }

    private fun nexttext(newText: String?) {
        if(newText != null){
            val nlist =  ArrayList<totaluserclass>()
            for(i in  totaluser){
                if(i.name!!.contains(newText)||
                    i.name.toUpperCase(Locale.ROOT).contains(newText)||
                    i.name.toLowerCase(Locale.ROOT).contains(newText)){
                    nlist.add(i)
                }
            }
            adapter.setfilteredlit(nlist)
            b.chatrv.adapter =  adapter
        }else {
            Toast.makeText(this@SearchActivity, "please enter the name", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }
}