package app.keyboardly.dev.ui.addon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.keyboardly.dev.databinding.FragmentAddonBinding

class AddOnFragment : Fragment() {

    private var _binding: FragmentAddonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this)[AddOnViewModel::class.java]

        _binding = FragmentAddonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.text
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}