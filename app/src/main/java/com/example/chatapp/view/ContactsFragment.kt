package com.example.chatapp.view

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.chatapp.`object`.User
import com.example.chatapp.adapter.ContactsRecyclerViewAdapter
import com.example.chatapp.databinding.FragmentContactsBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.PERMISSION_CODE_READ_CONTACTS
import com.example.chatapp.utilits.showToast
import com.example.chatapp.viewmodel.ContactsViewModel


class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding               // binding initialization
    private val viewModel by viewModels<ContactsViewModel>()            // vieModel
    private lateinit var recyclerViewAdapter: ContactsRecyclerViewAdapter      // recyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater, container, false)

        observeViewModel()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
    }

    fun observeViewModel(){
        viewModel.registerUserList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()){                              // list of register users is empty
                binding.tvEmpty.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else {                                        // show list of register users in recycler view
                binding.progressBar.visibility = View.GONE
                initRecyclerView(it)
            }
        })
    }

    fun initRecyclerView(userList: List<User>){
        recyclerViewAdapter = ContactsRecyclerViewAdapter(userList)
        binding.contactsRecyclerView.adapter = recyclerViewAdapter
    }

    // check permission for reading contacts on device
    @RequiresApi(Build.VERSION_CODES.O)         // to use cursor
    fun checkPermission(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){     // handle permission for devices with marshmallow and above
            // if permission is denied - request
            if (APP_ACTIVITY.checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(android.Manifest.permission.READ_CONTACTS)
                APP_ACTIVITY.requestPermissions(permissions, PERMISSION_CODE_READ_CONTACTS)

            } else {            // permission already granted
                viewModel.getUsers()
            }
        } else {                // system OS < marshmellow
            viewModel.getUsers()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)         // to use cursor
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            PERMISSION_CODE_READ_CONTACTS -> viewModel.getUsers()       // if permission is granted get register users list
            else -> showToast("Permission denied")
        }
    }

}