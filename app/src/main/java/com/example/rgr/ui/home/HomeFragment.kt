package com.example.rgr.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rgr.R
import com.example.rgr.databinding.FragmentHomeBinding
import com.example.rgr.di.AppModule
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(AppModule.provideBooksRepository(requireContext()))
    }

    private lateinit var adapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookAdapter(
            onItemClick = { book -> navigateToDetail(book.id) },
            onFavoriteClick = { book -> viewModel.toggleFavorite(book.id) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HomeFragment.adapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.books.collect { books ->
                adapter.submitList(books)
                binding.swipeRefresh.isRefreshing = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.swipeRefresh.isRefreshing = isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.info.collect { info ->
                info?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearInfo()
                }
            }
        }
    }

    private fun navigateToDetail(bookId: String) {
        val bundle = Bundle().apply {
            putString("bookId", bookId)
        }
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}