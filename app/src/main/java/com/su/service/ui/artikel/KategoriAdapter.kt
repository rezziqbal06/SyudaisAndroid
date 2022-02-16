import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.su.service.R
import com.su.service.model.kategori.KategoriItem
import kotlinx.android.synthetic.main.item_kategori.view.*

class KategoriAdapter(private val list: ArrayList<KategoriItem?>?) :
    RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {
    lateinit var listener: OnItemClickListener
    private var index = -1
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(kategori: KategoriItem?,position: Int) {
            with(itemView) {
                tv_kategori.text = kategori?.nama
                setOnClickListener {
                    index = position
                    if(listener != null){
                        kategori?.nama?.let { it1 -> listener.onItemClick(position, it1) }
                    }
                    notifyDataSetChanged()
                }
            }

        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int, kategori: String)
    }

    fun removeItem(position: Int) {
        this.list?.removeAt(position)
        notifyItemRemoved(position)
        this.list?.size?.let { notifyItemRangeRemoved(position, it) }
    }

    fun updateItem(position: Int, Kategori: KategoriItem) {
        this.list?.set(position, Kategori)
        notifyItemChanged(position, Kategori)
    }

    fun addListItem(Kategori: List<KategoriItem>) {
        this.list?.addAll(Kategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Kategori = list?.get(position)
        holder.bind(Kategori, position)
        if(index == position){
            holder.itemView.panel_kategori.setBackgroundResource(R.drawable.enable_kategori)
        }else{
            holder.itemView.panel_kategori.setBackgroundResource(R.drawable.disable_kategori)
        }
    }
}