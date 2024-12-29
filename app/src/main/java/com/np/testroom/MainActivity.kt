package com.np.testroom
import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.np.testroom.databinding.ActivityMainBinding
import com.np.testroom.ui.HomeFragment
import com.np.testroom.ui.ProfileFragment
import com.np.testroom.ui.SettingsFragment
import com.np.testroom.ui.UserListFragment
import com.np.testroom.ui.AddUserFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(UserListFragment())

        /* Passing each menu ID as a set of Ids because each menu should be considered as top-level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.userListFragment, R.id.addUserFragment), binding.drawerLayout
        )*/

        /*
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        Log.d("MainActivity", "NavController: ${navController}")
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)*/
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_list -> replaceFragment(UserListFragment())
                R.id.nav_add -> replaceFragment(AddUserFragment())
                R.id.nav_settings -> replaceFragment(SettingsFragment())
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    /*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }*/
}
