package act_presentation.fragments

import android.os.Bundle
import android.view.View
import br.sha.commommodule.BaseFragment
import exemple.sha.horas.R
import utils.instanceBuilder

class FragPresentStart : BaseFragment(R.layout.lay_act_presentation_frag_start) {

    companion object {

        fun newInstance() = instanceBuilder<FragPresentStart> {
        }
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {
    }
}