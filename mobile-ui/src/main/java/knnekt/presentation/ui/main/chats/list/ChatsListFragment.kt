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
import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.databinding.FragmentChatsListBinding
import knnekt.domain.entity.Chat
import knnekt.presentation.chats.ChatsListViewModel
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.ui.main.chats.list.helpers.ChatItemDetailsLookup
import knnekt.presentation.ui.main.chats.list.helpers.ChatItemKeyProvider
import knnekt.presentation.ui.main.chats.list.helpers.SwipeToDismissCallback
import knnekt.presentation.util.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI


class ChatsListFragment : Fragment(R.layout.fragment_chats_list), DIAware {

    override val di by closestDI()

    private val viewModel: ChatsListViewModel by viewModelInstance()
    private val binding by viewBinding(FragmentChatsListBinding::bind)

    private lateinit var chatsListAdapter: ChatsListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var navController: NavController

    private lateinit var selectionTracker: SelectionTracker<String>
    private lateinit var swipeToDismissCallback: SwipeToDismissCallback

    private val chatSelectionPredicate = object : SelectionTracker.SelectionPredicate<String>() {
        fun whileSwipe() = swipeToDismissCallback.whileSwipe
        override fun canSetStateForKey(key: String, nextState: Boolean): Boolean {
//            return !whileSwipe() && key != ChatEntity.ARCHIVED_SECTION_ID
            return false
        }
        override fun canSetStateAtPosition(position: Int, nextState: Boolean) = false//!whileSwipe()
        override fun canSelectMultiple() = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatsListAdapter = ChatsListAdapter(
            onClick = { chatId ->
                if (selectionTracker.hasSelection()) return@ChatsListAdapter

                if (chatId == ChatEntity.ARCHIVED_SECTION_ID) {
                    navController.navigate(R.id.action_chatsListFragment_to_archivedChatsFragment)
                } else {
                    val action = ChatsListFragmentDirections.chatsListToChat(chatId)
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
        layoutManager = LinearLayoutManager(requireContext())
        with( binding.chatsRecycler) {
            setHasFixedSize(true)
            layoutManager = this@ChatsListFragment.layoutManager
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapter = chatsListAdapter
        }

        setupSelectionTracker()
        setupListDecorations()

        chatsListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {

            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
//                scrollUp()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0 && isStartOfList())
                    scrollUp()
            }
        })
    }

    private fun isStartOfList(): Boolean {
        return  layoutManager.findFirstVisibleItemPosition() == 0
    }

    private fun scrollUp() {
        binding.chatsRecycler.scrollToPosition(0)
    }

    private fun setupListDecorations() {
        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
            val newDrawable = InsetDrawable(drawable, requireContext().dp(66), 0, 0, 0)
            setDrawable(newDrawable)
        }
        binding.chatsRecycler.addItemDecoration(divider)

        swipeToDismissCallback = object : SwipeToDismissCallback(
            R.drawable.ic_outline_archive_24,
            requireContext().themeColor(R.attr.colorSecondary),
            getString(R.string.archive),
            requireContext().themeColor(android.R.attr.textColorPrimaryInverse)
        ) {

            fun canBeSwiped(holder: ChatsListAdapter.ChatViewHolder): Boolean {
                return holder.recentItem?.id != ChatEntity.ARCHIVED_SECTION_ID
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
                    viewModel.archiveChat(chat.id)
            }
        }

        ItemTouchHelper(swipeToDismissCallback).attachToRecyclerView(binding.chatsRecycler)
    }

    private fun setupSelectionTracker() {
        selectionTracker = SelectionTracker.Builder(
            "chats-selection",
            binding.chatsRecycler,
            ChatItemKeyProvider(chatsListAdapter),
            ChatItemDetailsLookup(binding.chatsRecycler),
            StorageStrategy.createStringStorage()
        ).withSelectionPredicate(chatSelectionPredicate)
            .build()

        chatsListAdapter.tracker = selectionTracker
    }


    private fun onBindData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chatsPagingData.collectLatest(chatsListAdapter::submitData)
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }


}