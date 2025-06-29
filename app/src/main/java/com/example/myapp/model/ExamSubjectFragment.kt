package com.example.myapp.model


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.databinding.FragmentExamSubjectBinding
import com.example.myapp.data.ExamDataProvider
import com.example.myapp.model.ExamSubjectAdapter

class ExamSubjectsFragment : Fragment() {
    private var _binding: FragmentExamSubjectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExamSubjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val examSubjects = ExamDataProvider.getExamSubjects()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ExamSubjectAdapter(examSubjects )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ExamSubjectsFragment()
    }
}