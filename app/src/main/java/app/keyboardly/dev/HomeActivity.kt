package app.keyboardly.dev

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import app.keyboardly.dev.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import timber.log.Timber


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_home)
//        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        val navView: NavigationView = binding.navView

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_addon
            ), drawerLayout
        )

        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener { item ->
            Timber.w("on click=> ${item.itemId}")
            when (item.itemId) {
                R.id.nav_home -> navController.navigate(R.id.nav_home)
                R.id.nav_addon -> navController.navigate(R.id.nav_addon)
                R.id.nav_blank -> navController.navigate(R.id.nav_blank)
                else -> {
                    Timber.w("unhandle - item = ${item.itemId}")
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)

            true
        }
    }

    private var isBack = false

    override fun onBackPressed() {
        binding.apply {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                if (isBack) {
                    super.onBackPressed()
                } else {
                    Toast.makeText(this@HomeActivity, "Press back again to exit",
                        Toast.LENGTH_SHORT).show()
                    isBack = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        isBack = false
                    }, 1500)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}