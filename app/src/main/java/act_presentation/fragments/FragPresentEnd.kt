package act_presentation.fragments

import android.os.Bundle
import android.view.View
import br.sha.commommodule.BaseFragment
import exemple.sha.horas.R
import utils.instanceBuilder

class FragPresentEnd : BaseFragment(R.layout.lay_act_presentation_frag_end) {

    companion object {
        fun newInstance() = instanceBuilder<FragPresentEnd> {  }
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {

    }
}