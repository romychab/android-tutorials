package ua.cn.stu.robolectric.apps.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.robolectric.CatsAdapterListener
import ua.cn.stu.robolectric.catsAdapter
import ua.cn.stu.robolectric.databinding.FragmentCatsBinding
import ua.cn.stu.robolectric.viewmodel.CatListItem
import ua.cn.stu.robolectric.viewmodel.CatsViewModel

@AndroidEntryPoint
class CatsListActivity : AppCompatActivity(), CatsAdapterListener {

    private val viewModel by viewModels<CatsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = FragmentCatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = catsAdapter(this)
        binding.catsRecyclerView.layoutManager = LinearLayoutManager(this)
        (binding.catsRecyclerView.itemAnimator as? DefaultItemAnimator)
            ?.supportsChangeAnimations = false
        binding.catsRecyclerView.adapter = adapter
        viewModel.catsLiveData.observe(this) {
            adapter.submitList(it)
        }
    }

    override fun onCatDelete(cat: CatListItem.Cat) {
        viewModel.deleteCat(cat)
    }

    override fun onCatToggleFavorite(cat: CatListItem.Cat) {
        viewModel.toggleFavorite(cat)
    }

    override fun onCatChosen(cat: CatListItem.Cat) {
        val intent = Intent(this, CatDetailsActivity::class.java)
        intent.putExtra(CatDetailsActivity.EXTRA_CAT_ID, cat.id)
        startActivity(intent)
    }

}