package com.example.cryptoo.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.cryptoo.Adapter.searchbaradapter
import com.example.cryptoo.ContactModel
import com.example.cryptoo.databinding.FragmentCallBinding

import java.util.Locale


class Call : Fragment() {

   lateinit var b : FragmentCallBinding
    lateinit var adapter: searchbaradapter
    var list =  ArrayList<ContactModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentCallBinding.inflate(inflater, container, false)

       adapter = searchbaradapter(requireContext() ,list)

        b.callrv.layoutManager = LinearLayoutManager(requireContext())

        if( permissions()){
            contactsupdate()
        }



        b.searchbar.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                nextdd(newText)
                return true
            }

        })

        return b.root
    }

    private fun updateadapter(list2: List<ContactModel>) {
        list2.sortedBy { it.name }
        adapter = searchbaradapter(requireContext(), list2)
        b.callrv.adapter = adapter
        adapter.notifyDataSetChanged()

    }


    private fun nextdd(newText: String?) {
        if (newText != null) {
            val newlist = ArrayList<ContactModel>()
            for(i in list){
                if( i.name!!.contains(newText)
                    ||i.name?.toLowerCase(Locale.ROOT)!!.contains(newText)
                    || i.name?.toUpperCase(Locale.ROOT)!!.contains(newText)){
                    newlist.add(i)
                }
            }
            adapter.setfilteredlit(newlist)

        }else {
            Toast.makeText(requireContext(), "enter the data", Toast.LENGTH_SHORT).show()
        }

    }
    private fun contactsupdate(){


            b.card.visibility = View.GONE

            val cr = requireContext().contentResolver
            val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor != null && cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneno =
                        cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val photo =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                    if (phoneno > 0) {
                        val tcur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id), ""
                        )
                        if (tcur != null && tcur.count > 0) {
                            while (tcur != null && tcur.moveToNext()) {
                                val pno =
                                    tcur.getString(tcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                list.add(ContactModel(name, pno, photo))
                            }
                            tcur.close()
                        }


                        val list2 =  list.sortedBy {it.name }
                        updateadapter(list2)

                    }
                }
                if (cursor != null) {
                    cursor.close()
                }


        }

    }
    private fun permissions() : Boolean{
         if(ContextCompat.checkSelfPermission(requireContext() ,android.Manifest.permission.READ_CONTACTS ) == PackageManager.PERMISSION_GRANTED){
             Toast.makeText(requireContext(), "Permission Granted ", Toast.LENGTH_SHORT).show()
              contactsupdate()

             return  true
         } else {
             ActivityCompat.requestPermissions(requireActivity() ,
                 arrayOf(android.Manifest.permission.READ_CONTACTS), 1 )
         }
            return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1){
            if(ContextCompat.checkSelfPermission(requireContext() ,
                    android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(requireContext(), "permission is not give ", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Permission is given", Toast.LENGTH_SHORT).show()
            }
        }
    }


}