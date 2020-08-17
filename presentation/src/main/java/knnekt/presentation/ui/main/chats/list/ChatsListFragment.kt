package knnekt.presentation.ui.main.chats.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import knnekt.R
import knnekt.databinding.FragmentChatsListBinding
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.util.setActionBar
import knnekt.presentation.util.toast
import knnekt.presentation.util.viewBinding
import kotlinx.android.synthetic.main.fragment_chats_list.*
import kotlinx.android.synthetic.main.item_chat_list.view.*
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
            onClick = { chat ->
                val action = ChatsListFragmentDirections.chatsListToChat(chat.id)
                findNavController().navigate(action)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar(toolbar)
        toolbar.setupWithNavController(findNavController())
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ChatsListFragment.viewModel
        }
        binding.chatsRecycler.adapter = chatsListAdapter
        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.chatsRecycler.addItemDecoration(divider)
        onBindLiveData()
    }

    private fun onBindLiveData() {
        viewModel.chats.observe(viewLifecycleOwner, chatsListAdapter::submitList)
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }


}