package com.github.codandotv.craftd.xml.ui

import android.util.SparseArray
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.util.forEach
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.github.codandotv.craftd.androidcore.data.model.base.SimpleProperties
import com.github.codandotv.craftd.androidcore.presentation.CraftDSimplePropertiesItemCallback

/**
 * The creation I use with base/inspiration tree papers :
 * https://medium.com/@rodrigo.vianna.oliveira/the-perfect-d-d-party-for-your-android-application-8b5919661605
 * https://medium.com/android-news/simplifying-the-work-with-recyclerview-a64027bca8c3
 * https://medium.com/gustavo-santorio/android-dynamic-views-with-recyclerview-c2974c96a85f
 * Adapter that use viewRenders with type from ViewType
 */
class CraftDViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CraftDView {
    /**
     * Helper for computing the difference between two lists via DiffUtil on a background thread
     */
    private val differ: AsyncListDiffer<SimpleProperties?> =
        AsyncListDiffer(this, CraftDSimplePropertiesItemCallback)

    /**
     * List from Renderers to each component with SparseArray is intended to be more memory-efficient than a HashMap
     */
    private var renderers = SparseArray<CraftDViewRenderer<RecyclerView.ViewHolder>>()

    /**
     * method that take each viewRenderer until viewType from CraftDComponent
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = renderers.get(viewType)?.createViewHolder(parent) ?: CraftDEmptyViewHolder(
            FrameLayout(parent.context)
        )
        holder.itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return holder
    }

    override fun getItemCount(): Int = differ.currentList.size

    /**
     * Take the item reference that was set in DiffUtils
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        differ.currentList.getOrNull(position)?.let { vo ->
            findRenderOrNull(vo.key)?.bindView(vo, holder, position)
        }
    }

    /**
     * Tke current SimpleProperties list
     */
    override fun getItemViewType(position: Int): Int {
        return differ.currentList.getOrNull(position)?.let {
            findRenderOrNull(it.key)?.viewType ?: super.getItemViewType(position)
        } ?: super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    /**
     * Is required register the ViewRender to show on the screen
     */
    override fun registerRenderer(renderer: CraftDViewRenderer<*>) {
        if (renderers.get(renderer.viewType) == null) {
            renderers.put(renderer.viewType, renderer as CraftDViewRenderer<RecyclerView.ViewHolder>)
        }
    }

    /**
     * Is required register the ViewRender to show on the screen and received a list of renders
     */
    override fun registerRenderers(renderers: List<CraftDViewRenderer<*>>) {
        renderers.forEach { registerRenderer(it) }
    }

    override fun setViewObjectDiff(properties: List<SimpleProperties>) {
        differ.submitList(properties)
    }

    override fun updateViewAt(properties: SimpleProperties, index: Int) {
        val list = differ.currentList.toMutableList()
        list[index] = properties
        setViewObjectDiff(list.toList().toList() as List<SimpleProperties>)
    }

    override fun notifyPositionRemovedAt(position: Int) {
        val list = differ.currentList.toMutableList()
        list.removeAt(position)
        setViewObjectDiff(list.toList().toList() as List<SimpleProperties>)
    }

    override fun notifyItemChangeAt(position: Int, payload: Any?) {
        payload?.let {
            notifyItemChanged(position, payload)
        } ?: notifyItemChanged(position)
    }

    private fun findRenderOrNull(key: String): CraftDViewRenderer<RecyclerView.ViewHolder>? {
        renderers.forEach { _, value ->
            if (value.key == key) return value
        }
        return null
    }

    override fun clear() {
        differ.submitList(emptyList())
    }
}
