package com.derrick.cart.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.derrick.cart.CHECKLIST
import com.derrick.cart.models.Checklist
import com.derrick.cart.ui.SubItemActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SubItemActivityContract : ActivityResultContract<Checklist, Checklist?>() {
    override fun createIntent(context: Context, input: Checklist) :Intent{
        val intent = Intent(context, SubItemActivity::class.java)
        intent.putExtra(CHECKLIST, Json.encodeToString(input))
        return intent
    }


    override fun parseResult(resultCode: Int, intent: Intent?) : Checklist? {
        return if (resultCode != Activity.RESULT_OK)
                    null
                else
                intent?.getStringExtra(CHECKLIST)?.let { Json.decodeFromString(it) }
    }
}