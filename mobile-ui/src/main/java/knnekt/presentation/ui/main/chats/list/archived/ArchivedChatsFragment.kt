package knnekt.presentation.ui.main.chats.list.archived

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
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
import knnekt.presentation.chats.ArchivedChatsViewModel
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.ui.main.chats.list.ChatsListAdapter
import knnekt.presentation.ui.main.chats.list.helpers.SwipeToDismissCallback
import knnekt.presentation.util.dp
import knnekt.presentation.util.themeColor
import knnekt.presentation.util.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

class ArchivedChatsFragment : Fragment(R.layout.fragment_archived_chats), DIAware {

    override val di by closestDI()
    private val viewModel: ArchivedChatsViewModel by viewModelInstance()
    private val binding by viewBinding(FragmentArchivedChatsBinding::bind)
    private lateinit var chatsListAdapter: ChatsListAdapter

    lateinit var navController: NavController
    lateinit var swipeToDismissCallback: SwipeToDismissCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatsListAdapter = ChatsListAdapter { chat ->
            val action =
                ArchivedChatsFragmentDirections.actionArchivedChatsFragmentToChatFragment(chat)
            navController.navigate(action)
        }

        chatsListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                if (chatsListAdapter.itemCount - itemCount == 0)
                    ViewCompat.postOnAnimation(binding.chatsRecycler) {
                        navController.navigateUp()
                    }
            }

        })
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
        viewLifecycleOwner.lifecycleScope.launch {
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

        swipeToDismissCallback = object : SwipeToDismissCallback(
            R.drawable.ic_outline_unarchive_24,
            requireContext().themeColor(R.attr.colorSecondary),
            getString(R.string.unarchive),
            requireContext().themeColor(android.R.attr.textColorPrimaryInverse)
        ) {

            override fun onSwiped(position: Int) {
                val chat = chatsListAdapter.getItemAtPosition(position)
                if (chat != null)
                    viewModel.unarchiveChat(chat.id)
            }
        }

        ItemTouchHelper(swipeToDismissCallback).attachToRecyclerView(binding.chatsRecycler)
    }

}