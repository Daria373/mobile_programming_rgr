package com.example.rgr.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rgr.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Заголовок
        binding.aboutTitle.text = "Про додаток \"Моя Бібліотека\""

        // Опис додатка
        binding.appDescription.text = """
            «Моя Бібліотека» — сучасний мобільний додаток для любителів книг.
            
            Дозволяє переглядати популярні та нові книги з усього світу, додавати їх до списку «Хочу прочитати», читати описи, дивитися обкладинки та дізнаватися про авторів.
        """.trimIndent()

        // Технічна інформація
        binding.techInfo.text = """
            Технології та джерела даних:
            • Google Books API
            • Room Database (локальне кешування)
            • Retrofit + Gson
            • MVVM архітектура
            • Material Design 3
        """.trimIndent()

        binding.versionAuthor.text = """
            Версія: 1.0
            Розробник: Цуркан Дар'я
            Група: КІ-221
            2025 рік
        """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}