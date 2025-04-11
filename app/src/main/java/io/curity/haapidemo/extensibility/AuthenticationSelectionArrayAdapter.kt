package io.curity.haapidemo.extensibility

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.curity.haapidemo.R
import se.curity.identityserver.haapi.android.ui.widget.models.SelectorItemModel

/*
 * Android recycler view logic to manage a collection of view models
 */
class AuthenticationSelectionArrayAdapter(
    private val itemList: List<AuthenticationSelectionItemViewModel>,
    private val onSelect: (model: SelectorItemModel) -> Unit)
        : RecyclerView.Adapter<AuthenticationSelectionArrayAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.haapi_authentication_selector_item, parent, false)
        return ModelViewHolder(v)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val viewModel = itemList[position]
        holder.itemText.setText(viewModel.getDescriptiveText())
        holder.itemButton.setText(viewModel.getButtonText())
        holder.itemButton.tag = viewModel
    }

    override fun getItemCount(): Int {
        return  itemList.size
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemText: TextView
        val itemButton: Button

        init {
            itemText = itemView.findViewById(R.id.authentication_selection_item_text)
            itemButton = itemView.findViewById(R.id.authentication_selection_item_button)

            // When an item is selected, call back the fragment
            itemButton.setOnClickListener({
                val viewModel = it.tag as AuthenticationSelectionItemViewModel
                onSelect(viewModel.selectorItem)
            })
        }
    }
}
