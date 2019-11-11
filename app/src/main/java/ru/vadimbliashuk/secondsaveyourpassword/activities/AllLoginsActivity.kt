package ru.vadimbliashuk.secondsaveyourpassword.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_all_logins.*
import ru.vadimbliashuk.secondsaveyourpassword.R
import ru.vadimbliashuk.secondsaveyourpassword.adapter.UserListAdapter
import ru.vadimbliashuk.secondsaveyourpassword.data.UserViewModel
import ru.vadimbliashuk.secondsaveyourpassword.models.UserEntity
import ru.vadimbliashuk.secondsaveyourpassword.ui.RecyclerItemClickListener
import ru.vadimbliashuk.secondsaveyourpassword.ui.SwipeToDeleteCallback

class AllLoginsActivity : AppCompatActivity(),
    RecyclerItemClickListener.OnRecyclerClickListener {

    private lateinit var vm: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_logins)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent(this, AddNewLogin::class.java)
            startActivity(intent)
        }

        vm = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = UserListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                this
            )
        )

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
             //   val adapter = recyclerView.adapter as UserListAdapter

                val user: UserEntity = adapter.getUserAtPosition(viewHolder.adapterPosition)
                vm.delete(user)

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)


        vm.allUsers.observe(this, Observer { users ->
            users.let {
                adapter.setUsers(it)
            }
        })
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d("inItemClick", "ShortClick")
    }

    override fun onItemLongClick(view: View, position: Int) {
        Toast.makeText(
            this,
            "long",
            Toast.LENGTH_LONG
        ).show()

    }
}
