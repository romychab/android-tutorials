package ua.cn.stu.resultapi

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import ua.cn.stu.resultapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) {
        Log.d(MainActivity::class.java.simpleName, "Permission granted: $it")
        if (it) {
            Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show()
        }
    }

    private val editMessageLauncher = registerForActivityResult(SecondActivity.Contract()) {
        Log.d(MainActivity::class.java.simpleName, "Edit result: $it")
        if (it != null) {
            binding.valueTextView.text = it.message
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.requestPermissionButton.setOnClickListener { requestPermission() }
        binding.editButton.setOnClickListener { editMessage() }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun editMessage() {
        editMessageLauncher.launch(binding.valueTextView.text.toString())
    }

}