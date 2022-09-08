package com.elveum.elementadapter.app.sample1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.app.databinding.ActivityMainBinding
import com.elveum.elementadapter.app.model.Cat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatsActivity : AppCompatActivity() {

    private val viewModel by viewModels<CatsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CatsAdapter(object : CatsAdapter.Listener {
            override fun onChooseCat(cat: Cat) {
                Toast.makeText(
                    this@CatsActivity,
                    "${cat.name} meow-meows",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onToggleFavorite(cat: Cat) {
                viewModel.toggleFavorite(cat)
            }

            override fun onDeleteCat(cat: Cat) {
                viewModel.deleteCat(cat)
            }
        })
        (binding.catsRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
        binding.catsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.catsRecyclerView.adapter = adapter

        // use 'viewLifecycleOwner' instead of 'this' in fragments
        viewModel.catsLiveData.observe(this) {
            adapter.submitList(it)
        }
    }

}