package com.example.aitravelplanner

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarItemView
import kotlinx.coroutines.launch

/**
 * MainActivity gestisce l'interfaccia utente principale dell'applicazione, inclusa la navigazione
 * tramite BottomNavigationView e l'inizializzazione dell'utente corrente.
*/
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val userRepository: UserRepository = UserRepository.getInstance()
    var user: User? = null;
    var isInit: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        if (b != null) {
            user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                b.getSerializable("user", User::class.java)
            } else {
                b.getSerializable("user") as User
            }
            isInit = b.getBoolean("isInit")
            lifecycleScope.launch {
                val likes = userRepository.getLikesByUser(user!!.idUser)
                for(like in likes)
                    user!!.likedTravels!!.add(like)
                userRepository.updateUser(user!!)
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera l'icona centrale della bottom navigation e imposta dimensioni diverse
        val navView: BottomNavigationView = binding.navView
        val menu = navView.menu
        val menuItem = menu.getItem(1)
        val navigationBarItemView: NavigationBarItemView = navView.findViewById(menuItem.itemId)
        val centralIconView: View = navigationBarItemView.findViewById(com.google.android.material.R.id.navigation_bar_item_icon_view)
        val centralIconViewParams: FrameLayout.LayoutParams = centralIconView.layoutParams as FrameLayout.LayoutParams
        centralIconViewParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65F, resources.displayMetrics).toInt()
        centralIconViewParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65F, resources.displayMetrics).toInt()
        centralIconViewParams.bottomMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4F, resources.displayMetrics).toInt()
        centralIconViewParams.topMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics).toInt()
        centralIconView.layoutParams = centralIconViewParams

        // Imposta il NavController per la BottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
    }
}
