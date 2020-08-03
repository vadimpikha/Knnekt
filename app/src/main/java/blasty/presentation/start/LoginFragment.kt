package blasty.presentation.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import blasty.R
import blasty.di.viewModelInstance
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class LoginFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: LoginViewModel by viewModelInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }



}