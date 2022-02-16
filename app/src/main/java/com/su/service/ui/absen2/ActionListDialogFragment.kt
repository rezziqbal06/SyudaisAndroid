package com.su.service.ui.absen2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.su.service.R
import kotlinx.android.synthetic.main.fragment_action_list_dialog.*

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ActionListDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [ActionListDialogFragment.Listener].
 */
class ActionListDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_action_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = arguments?.getString("email")
        val position = arguments?.getInt("position")
        val idKegiatan = arguments?.getInt("idKegiatan")
        val utypeKegiatan = arguments?.getString("utypeKegiatan")
        tv_hadir?.setOnClickListener {
            position?.let { it1 -> (activity as Absen2Activity?)?.absen(it1, email, "hadir", idKegiatan, utypeKegiatan) }
            dismiss()
        }
        tv_sakit?.setOnClickListener {
            position?.let { it1 -> (activity as Absen2Activity?)?.absen(it1, email, "sakit", idKegiatan, utypeKegiatan) }
            dismiss()
        }
        tv_izin?.setOnClickListener {
            position?.let { it1 -> (activity as Absen2Activity?)?.absen(it1, email, "izin", idKegiatan, utypeKegiatan) }
            dismiss()
        }
        tv_alpa?.setOnClickListener {
            position?.let { it1 -> (activity as Absen2Activity?)?.absen(it1, email, "alpa", idKegiatan, utypeKegiatan) }
            dismiss()
        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }



}
