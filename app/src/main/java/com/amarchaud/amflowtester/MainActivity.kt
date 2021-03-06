package com.amarchaud.amflowtester

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.amarchaud.amflowtester.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.listingFragment
                )
        )

        // nav host
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_first_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}