package com.example.incollege.main.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.incollege.R
import com.example.incollege.main.ui.event.EventFragment
import com.example.incollege.main.ui.home.HomeFragment
import com.example.incollege.main.ui.ormawa.Communicator
import com.example.incollege.main.ui.ormawa.OrmawaFragment
import com.example.incollege.main.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener,Communicator{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
        initiateUI()
    }

    private fun initiateUI() {
        val navHome: BottomNavigationView = findViewById(R.id.nav_view_home)
        AppBarConfiguration.Builder(
            R.id.home_navigation,
            R.id.event_navigation,
            R.id.ormawa_navigation,
            R.id.profile_navigation
        ).build()
        val navController = Navigation.findNavController(this, R.id.navHostFragmentHomePage)
        NavigationUI.setupWithNavController(navHome, navController)
        navHome.itemIconTintList = null
        navHome.setOnNavigationItemSelectedListener(this)

    }


    private fun loadFragment(fragment: Fragment): Boolean {
        val nameFragment=fragment.javaClass.name.toString()
        supportFragmentManager.popBackStackImmediate(nameFragment,0)
        supportFragmentManager.beginTransaction().apply {
            replace(
                    R.id.navHostFragmentHomePage, fragment
                )

            addToBackStack(null)
            commit()
        }

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment:Fragment?=null
        when(item.itemId){
            R.id.home_navigation->{
                fragment=HomeFragment()
            }
            R.id.ormawa_navigation->{
                fragment=OrmawaFragment()

            }
            R.id.event_navigation->{
                fragment=EventFragment()

            }
            R.id.profile_navigation->{
                fragment=ProfileFragment()

            }
        }
        return loadFragment(fragment!!)

    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount==1){
          finish()
        }
        else if(nav_view_home.selectedItemId==R.id.home_navigation){
            finish()
        }
        else{
            nav_view_home.selectedItemId=R.id.home_navigation
        }
    }

    override fun passdata(title: String, fragment: Fragment) {
        val bundle=Bundle()
        bundle.putString("title",title)
        fragment.arguments=bundle
        supportFragmentManager.beginTransaction().apply {
            add(R.id.navHostFragmentHomePage,fragment)
            addToBackStack(null)
            commit()

        }
    }


}