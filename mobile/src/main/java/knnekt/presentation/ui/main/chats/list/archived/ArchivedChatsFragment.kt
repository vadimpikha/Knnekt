package knnekt.presentation.ui.main.chats.list.archived

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import knnekt.R
import knnekt.databinding.FragmentArchivedChatsBinding
import knnekt.databinding.FragmentChatsListBinding
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.ui.main.chats.list.ChatsListAdapter
import knnekt.presentation.ui.main.chats.list.ChatsListFragmentDirections
import knnekt.presentation.ui.main.chats.list.helpers.SwipeToArchiveCallback
import knnekt.presentation.util.dp
import knnekt.presentation.util.viewBinding
import knnekt.shared.data.entity.Chat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class ArchivedChatsFragment : Fragment(R.layout.fragment_archived_chats), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModel: ArchivedChatsViewModel by viewModelInstance()
    private val binding by viewBinding(FragmentArchivedChatsBinding::bind)
    private lateinit var chatsListAdapter: ChatsListAdapter

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatsListAdapter = ChatsListAdapter { chat ->
            val action =
                ArchivedChatsFragmentDirections.actionArchivedChatsFragmentToChatFragment(chat)
            navController.navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
        }
        navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        setupList()
        bindData()
    }

    private fun bindData() {
        lifecycleScope.launch {
            viewModel.archivedChatsPagingData.collectLatest { pagingData ->
                chatsListAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupList() {
        binding.chatsRecycler.adapter = chatsListAdapter
        setupListDecorations()
    }


    private fun setupListDecorations() {
        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
            val newDrawable = InsetDrawable(drawable, requireContext().dp(66), 0, 0, 0)
            setDrawable(newDrawable)
        }
        binding.chatsRecycler.addItemDecoration(divider)
    }

}