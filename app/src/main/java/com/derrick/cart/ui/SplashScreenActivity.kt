package com.derrick.cart.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.derrick.cart.R

class SplashScreenActivity : AppCompatActivity() {
    private val postHandler = Handler(Looper.getMainLooper())
    private val delayInMilliseconds = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        postHandler.postDelayed({
            val intent = Intent(this, ItemsActivity::class.java)
            startActivity(intent)
            finish()
        }, delayInMilliseconds)
    }
}