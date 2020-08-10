package blasty.presentation.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import blasty.R
import blasty.databinding.FragmentSendCodeBinding
import blasty.di.activityViewModelInstance
import blasty.lifecycle.observeEvent
import blasty.presentation.MainActivity
import blasty.utils.onActionDone
import blasty.utils.toast
import blasty.utils.viewBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class SendConfirmCodeFragment : Fragment(R.layout.fragment_send_code), KodeinAware {

    override val kodein by closestKodein()

    private val binding by viewBinding(FragmentSendCodeBinding::bind)
    private val viewModel: AuthViewModel by activityViewModelInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SendConfirmCodeFragment.viewModel
        }
        binding.editTextPhone.onActionDone {
            viewModel.sendConfirmCode()
        }
        onBindLiveData()
    }

    private fun onBindLiveData() {
        viewModel.codeSentEvent.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(R.id.sendCode_to_signIn)
        }

        viewModel.userSignedInEvent.observeEvent(viewLifecycleOwner) {
            startActivity(MainActivity.intent(requireContext()))
            activity?.finish()
        }

        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }

}