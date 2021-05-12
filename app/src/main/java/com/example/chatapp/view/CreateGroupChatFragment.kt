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
import com.example.chatapp.R
import com.example.chatapp.`object`.User
import com.example.chatapp.adapter.CreateGroupChatRecyclerViewAdapter
import com.example.chatapp.databinding.FragmentCreateGroupChatBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.PERMISSION_CODE_READ_CONTACTS
import com.example.chatapp.utilits.replaceFragment
import com.example.chatapp.utilits.showToast
import com.example.chatapp.viewmodel.CreateGroupChatViewModel

class CreateGroupChatFragment : Fragment(R.layout.fragment_create_group_chat), CreateGroupChatRecyclerViewAdapter.OnCheckboxClickListener {

    private lateinit var binding: FragmentCreateGroupChatBinding
    private val viewModel by viewModels<CreateGroupChatViewModel>()
    private lateinit var recyclerViewAdapter: CreateGroupChatRecyclerViewAdapter

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateGroupChatBinding.inflate(layoutInflater, container, false)

        //fragment appearance
        APP_ACTIVITY.setToolbarTitle("")
        APP_ACTIVITY.showNavigationIcon()
        APP_ACTIVITY.hideCreateChatIcon()
        APP_ACTIVITY.hideBottomNavBar()
        binding.btnCreateGroup.visibility = View.GONE

        observeViewModel()
        setUpClickListeners()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
    }

    fun observeViewModel(){

        // list of registered users
        viewModel.registerUserListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()){                              // list of register users is empty
                binding.tvEmpty.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else {                                        // show list of register users in recycler view
                binding.progressBar.visibility = View.GONE
                initRecyclerView(it)
            }
        })

        // list of users in group
        viewModel.groupListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()){
                binding.btnCreateGroup.visibility = View.GONE
            } else {
                binding.btnCreateGroup.visibility = View.VISIBLE
            }
        })

        // result of chat creating
        viewModel.isChatCreatingSuccessfulLiveData.observe(viewLifecycleOwner, Observer {
            if (it){
                showToast("Chat is successfully created")
                replaceFragment(ChatFragment())
            } else { showToast("Error occurred")}
        })
    }

    fun setUpClickListeners(){
        binding.btnCreateGroup.setOnClickListener{
            if (binding.etChatName.text.toString().trim { it <= ' ' }.isEmpty()){
                binding.tilChatName.error = "Fill in!"
            } else {
                viewModel.createGroup(binding.etChatName.text.toString().trim { it <= ' ' })
            }
        }
    }

    fun initRecyclerView(userList: List<User>){
        recyclerViewAdapter = CreateGroupChatRecyclerViewAdapter(userList, this)
        binding.createGroupRecyclerView.adapter = recyclerViewAdapter
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
            else -> {
                binding.progressBar.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
                showToast("Permission denied")
            }

        }
    }

    // interface implementation
    override fun onClick(position: Int, isChecked: Boolean) {
        viewModel.receiveCheckboxInfo(position, isChecked)
    }

    override fun onDestroy() {
        super.onDestroy()
        APP_ACTIVITY.showBottomnavBar()
    }
}