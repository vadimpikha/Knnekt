package blasty.presentation.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import blasty.R
import blasty.databinding.FragmentSignInBinding
import blasty.di.activityViewModelInstance
import blasty.lifecycle.observeEvent
import blasty.presentation.MainActivity
import blasty.utils.toast
import blasty.utils.viewBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class SignInFragment : Fragment(R.layout.fragment_sign_in), KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: AuthViewModel by activityViewModelInstance()
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = this@SignInFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        onBindLiveData()
    }

    private fun onBindLiveData() {
        viewModel.userSignedInEvent.observeEvent(viewLifecycleOwner) {
            startActivity(MainActivity.intent(requireContext()))
            activity?.finish()
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }

}