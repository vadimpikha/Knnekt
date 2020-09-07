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
import kotlinx.coroutines.flow.map
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
        fun whileSwipe() = swipeToArchiveCallback.whileSwipe
        override fun canSetStateForKey(key: Chat, nextState: Boolean) = !whileSwipe()
        override fun canSetStateAtPosition(position: Int, nextState: Boolean) = !whileSwipe()
        override fun canSelectMultiple() = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatsListAdapter = ChatsListAdapter(
            onClick = { chat ->
                if(chat.id == Chat.ARCHIVED_CHAT_ID) {
                    navController.navigate(R.id.action_chatsListFragment_to_archivedChatsFragment)
                } else {
                    val action = ChatsListFragmentDirections.chatsListToChat(chat)
                    navController.navigate(action)
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
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

            fun canBeSwiped(holder: ChatsListAdapter.ChatViewHolder): Boolean {
                return holder.recentItem?.id != Chat.ARCHIVED_CHAT_ID
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (canBeSwiped(viewHolder as ChatsListAdapter.ChatViewHolder))
                    super.getSwipeDirs(recyclerView, viewHolder)
                else
                    0
            }

            override fun isItemViewSwipeEnabled() = !selectionTracker.hasSelection()

            override fun onSwiped(position: Int) {
                val chat = chatsListAdapter.getItemAtPosition(position)
                if (chat != null)
                    viewModel.archiveChat(chat.id, true)
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
            viewModel.chatsPagingData
                .map {
                    it.insertHeaderItem(Chat.archivedSectionItem)
                }
                .collectLatest { pagingData ->
                chatsListAdapter.submitData(pagingData)
            }
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }


}