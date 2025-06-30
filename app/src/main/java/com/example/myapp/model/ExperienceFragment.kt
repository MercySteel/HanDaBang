


package model

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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import model.Experience
import com.example.myapp.ExperienceDatabaseHelper
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ExperienceFragment : Fragment() {

    private lateinit var dbHelper: ExperienceDatabaseHelper
    private lateinit var experienceInput: EditText
    private lateinit var addImageBtn: Button
    private lateinit var submitBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExperienceAdapter
    private var selectedImageUri: Uri? = null
    private lateinit var imagesDir: File

    companion object {
        const val PICK_IMAGE_REQUEST = 100
        fun newInstance() = ExperienceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_experience, container, false)

        dbHelper = ExperienceDatabaseHelper(requireContext())
        imagesDir = File(requireContext().filesDir, "experience_images").apply { mkdirs() }

        initViews(view)
        setupRecyclerView()
        setupClickListeners()
        loadExperiences()

        return view
    }

    private fun initViews(view: View) {
        experienceInput = view.findViewById(R.id.experienceInput)
        addImageBtn = view.findViewById(R.id.addImageBtn)
        submitBtn = view.findViewById(R.id.submitBtn)
        imagePreview = view.findViewById(R.id.imagePreview)
        recyclerView = view.findViewById(R.id.experienceRecyclerView)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExperienceAdapter(emptyList()) { experienceId ->
            // 点击删除逻辑
            deleteExperience(experienceId)
        }
        recyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        addImageBtn.setOnClickListener { openImageChooser() }
        submitBtn.setOnClickListener { submitExperience() }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            imagePreview.visibility = View.VISIBLE
            imagePreview.setImageURI(selectedImageUri)
        }
    }

    private fun submitExperience() {
        val content = experienceInput.text.toString().trim()
        if (content.isEmpty()) {
            Toast.makeText(requireContext(), "请输入经验内容", Toast.LENGTH_SHORT).show()
            return
        }

        val imagePath = selectedImageUri?.let { saveImageToInternalStorage(it) }
        dbHelper.addExperience(content, imagePath)

        clearInputs()
        loadExperiences()
        Toast.makeText(requireContext(), "经验分享已发布", Toast.LENGTH_SHORT).show()
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
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

    private fun clearInputs() {
        experienceInput.text.clear()
        imagePreview.visibility = View.GONE
        selectedImageUri = null
    }

    private fun loadExperiences() {
        val experiences = dbHelper.getAllExperiences()
        adapter.updateExperiences(experiences)
    }

    private fun deleteExperience(id: Long) {
        // 从数据库中删除指定ID的经验记录
        val rowsDeleted = dbHelper.deleteExperience(id)

        if (rowsDeleted > 0) {
            // 删除成功，刷新列表
            Toast.makeText(requireContext(), "经验已删除", Toast.LENGTH_SHORT).show()
            loadExperiences()
        } else {
            // 删除失败
            Toast.makeText(requireContext(), "删除失败，请重试", Toast.LENGTH_SHORT).show()
        }
    }
}




