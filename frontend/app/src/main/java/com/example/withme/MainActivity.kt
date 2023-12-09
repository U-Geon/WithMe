package com.example.withme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.withme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var homeFragment: HomeFragment
    private lateinit var myFragment: MyFragment
    private lateinit var settingFragment: SettingFragment

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기본 화면을 Home으로 설정
        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment, fragment.javaClass.simpleName)
                .commit()
        }

        val bottomNav = binding.bottomNavigation // BottomNavigationView 찾기

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    homeFragment = HomeFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_frame, homeFragment).commit()
                    true
                }

                R.id.navigation_my -> {
                    myFragment = MyFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_frame, myFragment).commit()
                    true
                }

                R.id.navigation_setting -> {
                    settingFragment = SettingFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_frame, settingFragment).commit()
                    true
                }

                else -> false
            }
        }
    }
}