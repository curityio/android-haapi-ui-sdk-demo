package io.curity.haapidemo.extensibility

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.curity.haapidemo.R
import se.curity.identityserver.haapi.android.ui.widget.models.SelectorItem

/*
 * Standard Android recycler view handling
 */
class AuthenticationSelectionArrayAdapter(private val itemList: List<SelectorItem>)
    : RecyclerView.Adapter<AuthenticationSelectionArrayAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.haapi_authentication_selector_item, parent, false)
        return ModelViewHolder(v)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {

        println("GJA: Binding text: ${itemList[position].title}")
        holder.itemText.setText(itemList[position].title)
    }

    override fun getItemCount(): Int {
        return  itemList.size
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var constraintLayout: ConstraintLayout
        var itemText: TextView

        init {
            constraintLayout = itemView.findViewById(R.id.authentication_selection_item_constraint_layout) as ConstraintLayout
            itemText = itemView.findViewById(R.id.authentication_selection_item_text) as TextView
        }
    }
}
