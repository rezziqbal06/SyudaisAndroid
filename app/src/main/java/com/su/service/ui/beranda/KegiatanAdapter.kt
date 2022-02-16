import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.su.service.R
import com.su.service.model.beranda.KegiatanItem
import com.su.service.utils.DateGenerator
import kotlinx.android.synthetic.main.item_kegiatan.view.*

class KegiatanAdapter(context: Context, list: List<KegiatanItem?>?) : PagerAdapter() {
    val context = context
    val list = list
    lateinit var listener: OnItemClickListener
    fun setOnItemClickListener(mListener: OnItemClickListener) {
        listener = mListener
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, kegiatan: KegiatanItem)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_kegiatan, container, false)
        //init view
        val kegiatan = list?.get(position)
        layout.tv_kegiatan.text = kegiatan?.judul
        layout.tv_alamat_kegiatan.text = kegiatan?.tempat
        var tanggal  = ""
        val formatTglAwal = DateGenerator.getTanggal("yy-MM-d HH:mm:ss","E dd MMMM yyyy", kegiatan?.sdate)
        val formatTglAkhir = DateGenerator.getTanggal("yy-MM-d HH:mm:ss","E dd MMMM yyyy", kegiatan?.edate)
        val tgl = DateGenerator.getTanggal("yy-MM-d HH:mm:ss","E dd MMMM yyyy", kegiatan?.sdate)
        if(!formatTglAwal.equals(formatTglAkhir)){
            tanggal = "dari $tgl"
        }else{
            tanggal = tgl.toString()
        }
        layout.tv_tanggal_kegiatan.text = tanggal
        layout.setOnClickListener({
            if (listener != null) {
                list?.get(position)?.let { it1 -> listener.onItemClick(position, it1) }
            }
        })
        container.addView(layout)
        return layout
    }

    override fun getCount(): Int {
        return list?.size!!
    }

}