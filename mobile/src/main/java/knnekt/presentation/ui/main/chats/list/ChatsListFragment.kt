package knnekt.presentation.ui.main.chats.list

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.*
import knnekt.R
import knnekt.databinding.FragmentChatsListBinding
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.ui.main.chats.list.helpers.ChatItemDetailsLookup
import knnekt.presentation.ui.main.chats.list.helpers.ChatItemKeyProvider
import knnekt.presentation.ui.main.chats.list.helpers.SwipeToArchiveCallback
import knnekt.presentation.util.dp
import knnekt.presentation.util.toast
import knnekt.presentation.util.viewBinding
import knnekt.shared.data.entity.Chat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class ChatsListFragment : Fragment(R.layout.fragment_chats_list), KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: ChatsListViewModel by viewModelInstance()
    private val binding by viewBinding(FragmentChatsListBinding::bind)

    private lateinit var chatsListAdapter: ChatsListAdapter
    private lateinit var navController: NavController
    private lateinit var selectionTracker: SelectionTracker<Chat>
    private lateinit var swipeToArchiveCallback: SwipeToArchiveCallback

    private val chatSelectionPredicate = object : SelectionTracker.SelectionPredicate<Chat>() {
        fun onSwipe() = swipeToArchiveCallback.whileSwipe
        override fun canSetStateForKey(key: Chat, nextState: Boolean) = !onSwipe()
        override fun canSetStateAtPosition(position: Int, nextState: Boolean) = !onSwipe()
        override fun canSelectMultiple() = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatsListAdapter = ChatsListAdapter(
            onClick = { chat ->
                val action = ChatsListFragmentDirections.chatsListToChat(chat)
                navController.navigate(action)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ChatsListFragment.viewModel
        }
        navController = findNavController()
        setupList()
        onBindData()
    }

    private fun setupList() {
        (binding.chatsRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.chatsRecycler.adapter = chatsListAdapter
        setupListDecorations()
        setupSelectionTracker()
    }

    private fun setupListDecorations() {
        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
            val newDrawable = InsetDrawable(drawable, requireContext().dp(66), 0, 0, 0)
            setDrawable(newDrawable)
        }
        binding.chatsRecycler.addItemDecoration(divider)

        swipeToArchiveCallback = object : SwipeToArchiveCallback(requireContext()) {
            override fun isItemViewSwipeEnabled() = !selectionTracker.hasSelection()
            override fun onSwiped(position: Int) {
                chatsListAdapter.notifyItemRemoved(position)
                chatsListAdapter.getItemAtPosition(position)
                    ?.let(viewModel::archiveChat)
            }
        }

        ItemTouchHelper(swipeToArchiveCallback).attachToRecyclerView(binding.chatsRecycler)
    }

    private fun setupSelectionTracker() {
        selectionTracker = SelectionTracker.Builder(
            "chats-selection",
            binding.chatsRecycler,
            ChatItemKeyProvider(chatsListAdapter),
            ChatItemDetailsLookup(binding.chatsRecycler),
            StorageStrategy.createParcelableStorage(Chat::class.java)
        ).withSelectionPredicate(chatSelectionPredicate)
            .build()

        chatsListAdapter.tracker = selectionTracker
    }


    private fun onBindData() {
        lifecycleScope.launch {
            viewModel.chatsPagingData.collectLatest { pagingData ->
                chatsListAdapter.submitData(pagingData)
            }
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }


}