package com.tirupati.vendor.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tirupati.vendor.R


/*class ImageCamAdapter(private val images: MutableList<Uri>, private val onDeleteClickListener: OnDeleteClickListener) :
    RecyclerView.Adapter<ImageCamAdapter.ImageViewHolder>() {


    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri = images[position]
        holder.imageView.setImageURI(imageUri)
        holder.deleteButton.setOnClickListener {
            onDeleteClickListener.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun addImage(imageUri: Uri) {
        images.add(imageUri)
        notifyItemInserted(images.size - 1)
    }

    fun removeImage(position: Int) {

            images.removeAt(position)
            notifyItemRemoved(position)

    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }
}*/
class ImageCamAdapter(private val images: MutableList<Uri>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onAddImageClick()
        fun onDeleteImageClick(position: Int)
    }

    private companion object {
        private const val VIEW_TYPE_IMAGE = 0
        private const val VIEW_TYPE_ADD_IMAGE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ADD_IMAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
                AddImageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_image, parent, false)
                ImageViewHolder(view)
            }
        }
    }
    override fun getItemCount(): Int {
        return if (images.size < 4) images.size + 1 else images.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && images.size < 4) VIEW_TYPE_ADD_IMAGE else VIEW_TYPE_IMAGE
    }
   /* override fun getItemCount(): Int {
        return if (images.size <= 4) images.size + 1 else images.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && images.size <= 4) VIEW_TYPE_ADD_IMAGE else VIEW_TYPE_IMAGE
    }*/

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_IMAGE -> {
                val imageHolder = holder as ImageViewHolder
                val imageUri = images[position]
                imageHolder.imageView.setImageURI(imageUri)
                imageHolder.deleteButton.setOnClickListener {
                    onClickListener.onDeleteImageClick(position)
                }
            }
            VIEW_TYPE_ADD_IMAGE -> {
                val addImageHolder = holder as AddImageViewHolder
                addImageHolder.addImageButton.setOnClickListener {
                    onClickListener.onAddImageClick()
                }
            }
        }
    }

/*

    override fun getItemCount(): Int {
        return if (images.size < 4) images.size + 1 else images.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && images.size < 4) VIEW_TYPE_ADD_IMAGE else VIEW_TYPE_IMAGE
    }
*/

    fun addImage(imageUri: Uri) {
        images.add(imageUri)
        notifyItemInserted(images.size - 1)
    }

    fun removeImage(position: Int) {

        images.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewFB)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    inner class AddImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addImageButton: LinearLayout = itemView.findViewById(R.id.uploadFrntBackImg)
    }
}
