package com.test.nikeapplication.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.nikeapplication.R
import com.test.nikeapplication.databinding.ActivityMainBinding
import com.test.nikeapplication.utils.ViewUtils
import com.test.nikeapplication.utils.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch


@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        setupFilters()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = MainAdapter()
        binding.rvWord.layoutManager = LinearLayoutManager(this)
        binding.rvWord.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.doAfterTextChanged {
            lifecycleScope.launch {
                viewModel.queryChannel.send(it.toString())
            }
        }
    }

    private fun setupFilters() {
        setFilterView(viewModel.getCurrentFilter())

        binding.ivFilterThumbsUp.setOnClickListener {
            viewModel.setFilter(FilterEnum.THUMBS_UP)
            setFilterView(FilterEnum.THUMBS_UP)
        }

        binding.ivFilterThumbsDown.setOnClickListener {
            viewModel.setFilter(FilterEnum.THUMBS_DOWN)
            setFilterView(FilterEnum.THUMBS_DOWN)
        }
    }

    private fun setFilterView(filter: FilterEnum) {
        val activeView: View
        val inactiveView: View

        when (filter) {
            FilterEnum.THUMBS_UP -> {
                activeView = binding.ivFilterThumbsUp
                inactiveView = binding.ivFilterThumbsDown
            }
            FilterEnum.THUMBS_DOWN -> {
                activeView = binding.ivFilterThumbsDown
                inactiveView = binding.ivFilterThumbsUp
            }
        }

        ViewUtils.setViewSize(
            activeView,
            resources.getDimension(R.dimen.active_icon_height).toInt(),
            resources.getDimension(R.dimen.active_icon_width).toInt()
        )

        ViewUtils.setViewSize(
            inactiveView,
            resources.getDimension(R.dimen.inactive_icon_height).toInt(),
            resources.getDimension(R.dimen.inactive_icon_width).toInt()
        )
    }

    private fun setupObservers() {
        viewModel.orderedWords.observe(this, {
            if (it.isNotEmpty())
                binding.root.hideKeyboard()
            adapter.setItems(it)
        })

        viewModel.feedBackMsg.observe(this, {
            if (it != null) {
                binding.root.hideKeyboard()
                val message = if (it is String) it else getString(it as Int)
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.loading.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}