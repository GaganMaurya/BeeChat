package com.example.cryptoo

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoo.Adapter.ViewpagerAdapter
import com.example.cryptoo.Adapter.chatitemadapter
import com.example.cryptoo.Adapter.totaluseradapter
import com.example.cryptoo.databinding.ActivityMainBinding
import com.example.cryptoo.fragments.Call
import com.example.cryptoo.fragments.Chat
import com.example.cryptoo.fragments.Status
import com.google.android.play.integrity.internal.t
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
         setTheme(R.style.Theme_new)
       setSupportActionBar(b.toolbar)

         title = "BeeChat"

        val fragmentArrayList = ArrayList<Fragment>()
        fragmentArrayList.add(Chat())
        fragmentArrayList.add(Status())
        fragmentArrayList.add(Call())

        val adapter = ViewpagerAdapter(this, supportFragmentManager, fragmentArrayList)
        b.mainViewPager.adapter = adapter
        b.tablayout.setupWithViewPager(b.mainViewPager)

      //getFcmToken()

    }

//    private fun getFcmToken() {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener {
//            if (it.isSuccessful) {
//                val token = it.result
//
//               val tt =  FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().uid.toString())
//                  tt.child("fcmtoken").setValue(token)
//                    .addOnSuccessListener {
//                        Toast.makeText(this, "token updated", Toast.LENGTH_SHORT).show()
//                    }
//            }
//
//        }
//    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu2, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.search -> {
                startActivity(Intent(this , SearchActivity::class.java))

            }
           R.id.settings1 ->{
               Toast.makeText(this@MainActivity, "settings", Toast.LENGTH_SHORT).show()
           }
            R.id.Account1 ->{
                startActivity(Intent(this , ProfileActivity::class.java))
            }



}

        return super.onOptionsItemSelected(item)
    }

//    override fun onNavigateUp(): Boolean {
//        onBackPressed()
//        return super.onNavigateUp()
//    }
}

