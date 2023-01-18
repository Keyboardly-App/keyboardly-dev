package app.keyboardly.dev

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import app.keyboardly.dev.databinding.ActivityHomeBinding
import timber.log.Timber


class HomeActivity : AppCompatActivity() {

    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarHome.toolbar)


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        binding.apply {
            val navController = findNavController(R.id.nav_host_fragment_content_home)
            NavigationUI.setupActionBarWithNavController(this@HomeActivity, navController, drawerLayout)

            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home, R.id.nav_addon
                ), drawerLayout
            )

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            initDrawerIcon()

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

    }

    private var isBack = false



    private fun ActivityHomeBinding.initDrawerIcon() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            mToggle =
                ActionBarDrawerToggle(
                    this@HomeActivity, drawerLayout,
                    R.string.open,
                    R.string.close
                )
        }
        /*mToggle.drawerArrowDrawable.color = ContextCompat.getColor(
            this@MainActivity,
            R.color.black
        )*/
//        mToggle.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        mToggle.isDrawerIndicatorEnabled = true

        drawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()
    }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_about->{
                showAboutDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAboutDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.action_about)
            .setMessage(R.string.text_about)
            .setPositiveButton(android.R.string.ok){ d, _ ->
                d.dismiss()
            }
        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}