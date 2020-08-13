package knnekt.presentation.ui.main.chats.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import knnekt.R
import knnekt.databinding.FragmentChatsListBinding
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.util.toast
import knnekt.presentation.util.viewBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class ChatsListFragment : Fragment(R.layout.fragment_chats_list), KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: ChatsListViewModel by viewModelInstance()
    private val binding by viewBinding(FragmentChatsListBinding::bind)
    private lateinit var chatsListAdapter: ChatsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatsListAdapter = ChatsListAdapter(
            onClick = { openChat(it.id) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ChatsListFragment.viewModel
        }
        binding.chatsRecycler.adapter = chatsListAdapter
        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.chatsRecycler.addItemDecoration(divider)
        onBindLiveData()
    }

    private fun openChat(id: String) {
        val action = ChatsListFragmentDirections.chatsListToChat(id)
        findNavController().navigate(action)
    }

    private fun onBindLiveData() {
        viewModel.chats.observe(viewLifecycleOwner, chatsListAdapter::submitList)
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }


}