package com.example.rgr.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rgr.R
import com.example.rgr.databinding.FragmentWishlistBinding
import com.example.rgr.di.AppModule
import com.example.rgr.ui.home.BookAdapter
import kotlinx.coroutines.launch

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WishlistViewModel by viewModels {
        WishlistViewModelFactory(AppModule.provideBooksRepository(requireContext()))
    }

    private lateinit var adapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookAdapter(
            onItemClick = { book -> navigateToDetail(book.id) },
            onFavoriteClick = { book -> viewModel.removeFromWishlist(book.id) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WishlistFragment.adapter
        }

        viewModel.loadWishlist()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.books.collect { books ->
                adapter.submitList(books)
                binding.emptyState.visibility = if (books.isEmpty()) View.VISIBLE else View.GONE
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