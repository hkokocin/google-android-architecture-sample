package com.example.hkokocin.gaa.adapter

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class Widget<T>(@field:LayoutRes private val layoutId: Int) {

    abstract fun setData(data: T)

    protected lateinit var view: View

    fun createView(inflater: LayoutInflater, parent: ViewGroup?): View {
        view = inflater.inflate(layoutId, parent, false)
        onViewCreated(view)
        return view
    }

    open protected fun onViewCreated(view: View) {}

    protected fun <T : View> viewId(@IdRes id: Int): Lazy<T> = lazy { view.findViewById<T>(id) }
}

class WidgetViewHolder<T>(val widget: Widget<T>, view: View) : RecyclerView.ViewHolder(view)

class WidgetAdapter(
        val layoutInflater: LayoutInflater
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var widgetProviders = LinkedHashMap<Class<out Any>, () -> Widget<*>>()

    private var items = listOf<Any>()

    inline fun <reified T : Any> addWidget(noinline widgetProvider: () -> Widget<T>) {
        widgetProviders.put(T::class.java, widgetProvider)
    }

    fun setItems(items: List<Any>) {
        this.items = items.filter { widgetProviders[it.javaClass] != null }
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = widgetProviders.keys.indexOf(items[position].javaClass)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindViewHolder(holder, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val provider = widgetProviders.values.elementAt(viewType)
        val widget = provider()
        return WidgetViewHolder(widget, widget.createView(layoutInflater, parent))
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> bindViewHolder(holder: RecyclerView.ViewHolder, item: T) {
        val viewHolder = holder as WidgetViewHolder<T>
        viewHolder.widget.setData(item)
    }
}