package app.keyboardly.dev.ui.addon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.keyboardly.dev.R
import app.keyboardly.dev.databinding.FragmentAddonBinding
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuAdapter
import app.keyboardly.lib.navigation.NavigationMenuModel
import timber.log.Timber

const val SAMPLE_ID = "app.keyboardly.sample"

class AddOnFragment : Fragment() {

    private lateinit var viewModel: AddOnViewModel
    private var _binding: FragmentAddonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[AddOnViewModel::class.java]

        _binding = FragmentAddonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.list.observe(viewLifecycleOwner){ list ->
                addOnList.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = AddOnListAdapter(requireActivity(), list, object : AddOnListAdapter.OnClickCallback {
                        override fun onClick(data: AddOnModel) {
                            Timber.d("data="+data.featurePackageId)
                            getListNavigation().firstOrNull { it.name == data.featurePackageId }?.apply {
                                try {
                                    findNavController().navigate(id_nav)
                                }catch (e: java.lang.Exception){
                                    e.printStackTrace()
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    private fun getListNavigation(): MutableList<Navigation> {
        val list = mutableListOf<Navigation>()

        list.add(
            Navigation(SAMPLE_ID,
                R.id.sample_default_nav))

        return list
    }


    data class Navigation(
        val name: String,
        val id_nav:Int)
}