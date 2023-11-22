import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.cortez_llasus_finalproject.InventoryItem
import com.example.cortez_llasus_finalproject.R

class InventoryListAdapter(private val context: Context, private val items: List<InventoryItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            // Inflate the layout for each list item
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item_inventory, null)

            // Create a ViewHolder to hold references to the views
            viewHolder = ViewHolder()
            viewHolder.itemNameTextView = view.findViewById(R.id.itemNameTextView)
            viewHolder.categoryTextView = view.findViewById(R.id.categoryTextView)
            viewHolder.quantityTextView = view.findViewById(R.id.quantityTextView)

            // Set the ViewHolder as a tag for the view
            view.tag = viewHolder
        } else {
            // Reuse the recycled view
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Bind data to the views
        val item = items[position]
        viewHolder.itemNameTextView.text = item.itemName
        viewHolder.categoryTextView.text = item.category
        viewHolder.quantityTextView.text = item.quantity

        return view
    }

    // ViewHolder pattern to improve performance by recycling views
    private class ViewHolder {
        lateinit var itemNameTextView: TextView
        lateinit var categoryTextView: TextView
        lateinit var quantityTextView: TextView
    }
}
