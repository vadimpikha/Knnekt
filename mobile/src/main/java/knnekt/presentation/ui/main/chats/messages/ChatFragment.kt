package knnekt.presentation.ui.main.chats.messages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import knnekt.R
import knnekt.databinding.FragmentChatBinding
import knnekt.presentation.di.activityViewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.ui.HoldListener
import knnekt.presentation.ui.MarginItemDecorator
import knnekt.presentation.ui.main.MainViewModel
import knnekt.presentation.ui.setOnHoldListener
import knnekt.presentation.util.disableWhileAnimation
import knnekt.presentation.util.hideKeyboard
import knnekt.presentation.util.toast
import knnekt.presentation.util.viewBinding
import knnekt.presentation.viewmodelfactory.ChatViewModelFactory
import kotlinx.android.synthetic.main.message_input.view.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

class ChatFragment : Fragment(R.layout.fragment_chat), KodeinAware {

    override val kodein by closestKodein()
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by viewModels {
        ChatViewModelFactory(args.chat, kodein.direct)
    }
    private val binding by viewBinding(FragmentChatBinding::bind)
    private val mainViewModel: MainViewModel by activityViewModelInstance()
    private lateinit var messagesAdapter: ChatMessagesAdapter
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messagesAdapter = ChatMessagesAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ChatFragment.viewModel
            chat = args.chat
            binding.messagePad.viewModel = this@ChatFragment.viewModel
            binding.messagePad.lifecycleOwner = viewLifecycleOwner
        }
        navController = findNavController()
        initViews()
        bindData()
    }

    override fun onStart() {
        mainViewModel.setCurrentChat(args.chat)
        super.onStart()
    }

    override fun onStop() {
        hideKeyboard(binding.messagePad.messageInput)
        mainViewModel.setCurrentChat(null)
        super.onStop()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            true
        )

        binding.messagesRecycler.apply {
            setLayoutManager(layoutManager)
            adapter = messagesAdapter
            addItemDecoration(MarginItemDecorator(requireContext(), 8, true))
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        with(binding.messagePad) {
            btnRecordVideoMsg.apply {
                setOnClickListener { swapSecondaryInputButtons() }
                setOnHoldListener(object : HoldListener {
                    override fun onHold(view: View) {
                        toast("btn_record_video_msg hold")
                    }

                    override fun onReleased(view: View) {
                        toast("btn_record_video_msg release")
                    }
                })
            }


            btnRecordVoiceMsg.apply {
                setOnClickListener { swapSecondaryInputButtons() }
                setOnHoldListener(object : HoldListener {
                    override fun onHold(view: View) {
                        toast("btn_record_voice_msg hold")
                    }

                    override fun onReleased(view: View) {
                        toast("btn_record_voice_msg release")
                    }
                })
            }
        }

    }

    private fun swapSecondaryInputButtons() {
        with(binding.messagePad) {
            val translationXTmp = videoMsgWrapper.translationX

            videoMsgWrapper.animate()
                .translationX(voiceMsgWrapper.translationX)
                .disableWhileAnimation(btnRecordVideoMsg)


            voiceMsgWrapper.animate()
                .translationX(translationXTmp)
                .disableWhileAnimation(btnRecordVoiceMsg)
        }
    }

    private fun bindData() {
        viewModel.toast.observeEvent(viewLifecycleOwner) {
            toast(it)
        }

        lifecycleScope.launch {
            viewModel.messagesPagingData.collectLatest { data ->
                messagesAdapter.submitData(data)
            }
        }
    }

}
