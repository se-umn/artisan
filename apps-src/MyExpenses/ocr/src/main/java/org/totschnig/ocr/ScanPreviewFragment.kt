package org.totschnig.ocr

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import org.totschnig.myexpenses.MyApplication
import org.totschnig.myexpenses.R
import org.totschnig.myexpenses.feature.OcrHost
import java.io.File
import javax.inject.Inject

class ScanPreviewFragment : DialogFragment() {
    @Inject
    lateinit var picasso: Picasso
    lateinit var imageView: ImageView
    lateinit var viewModel: ScanPreviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerOcrComponent.builder().appComponent(MyApplication.getInstance().appComponent).build().inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(this).get(ScanPreviewViewModel::class.java)
        viewModel.getResult().observe(this) { result ->
            (activity as? OcrHost)?.processOcrResult(result)
            dismiss()
        }
        val inflater: LayoutInflater = requireActivity().getLayoutInflater()
        val view: View = inflater.inflate(R.layout.scan_preview, null)
        val builder: AlertDialog.Builder = MaterialAlertDialogBuilder(requireActivity())
                .setView(view)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.button_scan, null)
        imageView = view.findViewById(R.id.imageView)
        return builder.create().apply {
            setOnShowListener {
                loadImage()
                view.findViewById<ImageView>(R.id.RotateRight).setOnClickListener { rotate(true) }
                view.findViewById<ImageView>(R.id.RotateLeft).setOnClickListener { rotate(false) }
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    getButton(AlertDialog.BUTTON_POSITIVE)?.let {
                        it.isEnabled = false
                    }
                    activity?.let { viewModel.runTextRecognition(scanFile, it) }
                }
            }
        }
    }

    private fun rotate(right: Boolean) {
        viewModel.rotate(right, scanFile.path) { loadImage() }
    }

    private fun loadImage() {
        scanFile.let {
            picasso.invalidate(it)
            picasso.load(it).into(imageView)
        }
    }

    fun handleData(intent: Intent) {
        viewModel.handleData(intent)
    }

    val scanFile: File
        get() = requireArguments().let {
            (it.getSerializable(KEY_FILE) as File)
        }

    companion object {
        const val KEY_FILE = "file"
        fun with(file: File) = ScanPreviewFragment().apply {
            isCancelable = false
            arguments = Bundle().apply {
                putSerializable(KEY_FILE, file)
            }
        }
    }
}