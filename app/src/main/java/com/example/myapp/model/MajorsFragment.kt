package com.example.myapp.model



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.databinding.FragmentMajorsBinding
import com.example.myapp.data.MajorDataProvider
import com.example.myapp.model.MajorCategoryAdapter

class MajorsFragment : Fragment() {
    private var _binding: FragmentMajorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMajorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val majorsByCategory = MajorDataProvider.getMajorsByCategory()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MajorCategoryAdapter(majorsByCategory)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MajorsFragment()
    }
}