package br.com.generic.base.ui.activities.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.generic.base.R
import br.com.generic.base.data.extensions.createChannel
import br.com.generic.base.data.extensions.isChannelCreated
import br.com.generic.base.data.extensions.loadUserLogo
import br.com.generic.base.data.extensions.serverURL
import br.com.generic.base.data.extensions.userCode
import br.com.generic.base.data.extensions.userExhibitionName
import br.com.generic.base.databinding.ActivityMainBinding
import br.com.generic.base.ui.activities.settings.SettingsActivity
import br.com.generic.base.utils.Constants.Companion.EVENT_CHANNEL_ID
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        enableEdgeToEdge()

        binding.appBarMain.fab.setOnClickListener {
            showNotification()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * Cria a barra de opções e carrega a foto
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val userLogo : ImageView = findViewById(R.id.ivUserLogo)
        val userName : TextView = findViewById(R.id.tvUserName)
        userName.text = userExhibitionName

        mainViewModel.serverData.observe(this) {serverData ->
            if (serverData.serverData.isNotEmpty()) {
                serverURL = serverData.serverData
                Picasso.get().load(loadUserLogo(serverURL,userCode)).resize(60,75).into(userLogo)
            }
        }

        return true
    }

    /**
     * Abre as configurações
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }

    /**
     * Configura os fragmentos
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Exibe a notificalçao
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification() {
        if (!isChannelCreated) {
            createChannel(this)
        }
        val builder = NotificationCompat.Builder(this, EVENT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Mensagem de Teste")
            .setContentText("Essa é uma mensagem de teste")
            .setLights(0x0000ff,2000,2000)
        val notification = builder.build()
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, notification)
        playSound(R.raw.notification)
    }

    private fun playSound(sound: Int) {
        val mediaPlayer = MediaPlayer.create(this, sound)
        mediaPlayer.start()
    }
}