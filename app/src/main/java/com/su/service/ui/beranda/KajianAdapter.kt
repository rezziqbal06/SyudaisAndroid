import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.su.service.R
import com.su.service.model.beranda.KajianItem
import com.su.service.ui.kegiatan.DetailKegiatanActivity
import com.su.service.utils.DateGenerator
import kotlinx.android.synthetic.main.item_kajian.view.*

class KajianAdapter(private val list: ArrayList<KajianItem?>?) :
    RecyclerView.Adapter<KajianAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(kajianItem: KajianItem) {
            with(itemView) {
                tv_kajian.text = kajianItem.judul
                tv_alamat_kajian.text = kajianItem.tempat
                var tanggal = ""
                val tgl = DateGenerator.getTanggal("yy-MM-d HH:mm:ss","E, dd MMMM yyyy", kajianItem.sdate)
                if(kajianItem.sdate == kajianItem.edate){
                    tanggal = "dari $tgl"
                }else{
                    tanggal = tgl.toString()
                }
                tv_tanggal_kajian.text = tanggal

                setOnClickListener {
                    val intent = Intent(context, DetailKegiatanActivity::class.java)
                    intent.putExtra(DetailKegiatanActivity.EXTRA_DATA, kajianItem)
                    intent.putExtra(DetailKegiatanActivity.EXTRA_TAG,"kajian")
                    context.startActivity(intent)
                }
            }

        }
    }

    fun removeItem(position: Int) {
        this.list?.removeAt(position)
        notifyItemRemoved(position)
        this.list?.size?.let { notifyItemRangeRemoved(position, it) }
    }

    fun updateItem(position: Int, kajianItem: KajianItem) {
        this.list?.set(position, kajianItem)
        notifyItemChanged(position, kajianItem)
    }

    fun addListItem(kajianItem: List<KajianItem>) {
        this.list?.addAll(kajianItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kajian, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kajianItem = list?.get(position)
        kajianItem?.let { holder.bind(it) }
    }
}