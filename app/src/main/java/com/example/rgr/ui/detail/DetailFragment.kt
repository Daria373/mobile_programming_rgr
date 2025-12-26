package com.example.rgr.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rgr.R
import com.example.rgr.databinding.FragmentDetailBinding
import com.example.rgr.di.AppModule
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // Отримуємо bookId з аргументів
    private val bookId: String by lazy {
        arguments?.getString("bookId") ?: throw IllegalArgumentException("bookId is required")
    }

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(AppModule.provideBooksRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Клік по сердечку — додати/видалити з wishlist
        binding.favoriteButton.setOnClickListener {
            viewModel.toggleFavorite(bookId)
        }

        // Клік по backdrop — повноекранне зображення
        binding.backdropImage.setOnClickListener {
            val backdropUrl = viewModel.book.value?.backdropPath
            backdropUrl?.let { url ->
                ImageFullscreenDialog.newInstance(url).show(parentFragmentManager, "fullscreen_image")
            }
        }

        // Завантажуємо книгу
        viewModel.loadBook(bookId)

        // Спостерігаємо за даними книги
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.book.collect { book ->
                book?.let { it ->
                    // Назва та автори
                    binding.titleText.text = it.title
                    binding.authorsText.text = it.authors.ifEmpty { "Автор невідомий" }

                    // Рейтинг
                    val ratingText = if (it.voteCount > 0) {
                        "${String.format("%.1f", it.voteAverage)} (${it.voteCount} оцінок)"
                    } else {
                        "Рейтинг відсутній"
                    }
                    binding.ratingText.text = ratingText

                    // Дата видання + кількість сторінок
                    val year = it.releaseDate?.substringBefore("-") ?: "Рік невідомий"
                    val pages = if (it.pageCount > 0) "${it.pageCount} сторінок" else "Кількість сторінок невідома"
                    binding.infoText.text = "$year • $pages"

                    // Опис
                    binding.overviewText.text = it.overview.ifEmpty { "Опис відсутній" }

                    // Обкладинка (poster)
                    Glide.with(this@DetailFragment)
                        .load(it.posterPath)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.posterImage)

                    // Фонове зображення (backdrop)
                    Glide.with(this@DetailFragment)
                        .load(it.backdropPath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(binding.backdropImage)

                    // Сердечко (заповнене чи порожнє)
                    binding.favoriteButton.setImageResource(
                        if (it.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_empty
                    )

                    // Приховуємо прогрес-бар
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        // Обробка помилок
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }

        // Показуємо прогрес-бар на початку
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}