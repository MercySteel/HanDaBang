package com.example.myapp.model

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.databinding.FragmentExperienceBinding
import com.example.myapp.database.KaoyanDatabaseHelper
import com.example.myapp.ui.experience.ExperienceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ExperienceFragment : Fragment() {

    private var _binding: FragmentExperienceBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: KaoyanDatabaseHelper
    private lateinit var adapter: ExperienceAdapter
    private var selectedImageUri: Uri? = null
    private lateinit var imagesDir: File

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                binding.imagePreview.visibility = View.VISIBLE
                binding.imagePreview.setImageURI(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExperienceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatabase()
        setupRecyclerView()
        setupClickListeners()
        loadExperiences()
    }

    private fun setupDatabase() {
        dbHelper = KaoyanDatabaseHelper(requireContext())
        imagesDir = File(requireContext().filesDir, "experience_images").apply { mkdirs() }
    }

    private fun setupRecyclerView() {
        // 修正1：使用正确的ID experienceRecyclerView
        binding.experienceRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ExperienceAdapter { experience ->
            // 修正2：传递整个Experience对象
            showDeleteConfirmation(experience)
        }

        binding.experienceRecyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.addImageBtn.setOnClickListener { openImageChooser() }
        binding.submitBtn.setOnClickListener { submitExperience() }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun submitExperience() {
        val content = binding.experienceInput.text.toString().trim()
        if (content.isEmpty()) {
            Toast.makeText(requireContext(), "请输入经验内容", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val imagePath = selectedImageUri?.let { saveImageToInternalStorage(it) }
                val id = dbHelper.addExperience(content, imagePath)

                withContext(Dispatchers.Main) {
                    clearInputs()
                    loadExperiences()
                    Toast.makeText(requireContext(), "经验分享已发布", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "发布失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun saveImageToInternalStorage(uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap != null) {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "exp_img_$timestamp.jpg"
                val outputFile = File(imagesDir, fileName)

                FileOutputStream(outputFile).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                }
                outputFile.absolutePath
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showDeleteConfirmation(experience: Experience) {
        AlertDialog.Builder(requireContext())
            .setTitle("删除经验")
            .setMessage("确定要删除这条经验分享吗？")
            .setPositiveButton("删除") { _, _ ->
                deleteExperience(experience.id)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun deleteExperience(experienceId: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            val deletedRows = dbHelper.deleteExperience(experienceId)
            withContext(Dispatchers.Main) {
                if (deletedRows > 0) {
                    loadExperiences()
                    Toast.makeText(requireContext(), "已删除", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadExperiences() {
        lifecycleScope.launch(Dispatchers.IO) {
            val experiences = dbHelper.getAllExperiences()
            withContext(Dispatchers.Main) {
                adapter.submitList(experiences)
                // 修正3：添加emptyView或使用其他方式处理空状态
                if (experiences.isEmpty()) {
                    Toast.makeText(requireContext(), "暂无经验分享", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearInputs() {
        binding.experienceInput.text?.clear()
        binding.imagePreview.visibility = View.GONE
        selectedImageUri = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ExperienceFragment()
    }
}