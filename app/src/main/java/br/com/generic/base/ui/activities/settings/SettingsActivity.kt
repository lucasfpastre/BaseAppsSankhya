package br.com.generic.base.ui.activities.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.generic.base.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity: AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        insertListeners()

    }

    // Retorna a tela anterior
    private fun insertListeners() {
        binding.ibSettingsBack.setOnClickListener {
            finish()
        }
    }

}