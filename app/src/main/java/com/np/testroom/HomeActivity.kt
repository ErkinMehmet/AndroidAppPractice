package com.np.testroom
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.np.testroom.databinding.ActivityHomeBinding
import androidx.navigation.ui.AppBarConfiguration
import com.np.testroom.ui.AddUserFragment
import com.np.testroom.ui.CalculateLoanFragment
import com.np.testroom.ui.SettingsFragment
import com.np.testroom.ui.UserListFragment
import androidx.room.Room
import com.np.testroom.data.AppDatabase

class HomeActivity : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(CalculateLoanFragment())

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_list -> replaceFragment(UserListFragment())
                R.id.nav_add -> replaceFragment(AddUserFragment())
                R.id.nav_settings -> replaceFragment(SettingsFragment())
            }
            true
        }

        logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    // Logout logic
    private fun logout() {
        // Clear the SharedPreferences login state
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Navigate back to the login screen
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
