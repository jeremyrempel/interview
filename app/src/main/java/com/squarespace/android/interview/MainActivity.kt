package com.squarespace.android.interview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squarespace.android.interview.api.FakeGetPhotosRepository
import com.squarespace.android.interview.api.Result
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val repo = FakeGetPhotosRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = PhotoAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        recyclerView.adapter = adapter

        val cancelJobCallback = repo.getPhotosList { response ->
            when (response) {
                is Result.Success -> adapter.submitList(response.successResult)
                is Result.Failure -> throw RuntimeException("failwhale")
            }
        }
    }

    inner class PhotoAdapter : ListAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PhotoDiff) {
        inner class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val description = view.findViewById<TextView>(R.id.row_description)
            private val imageView = view.findViewById<ImageView>(R.id.row_image)

            fun bind(id: Int) {
                // get the photo details from id
                val cancelCallback = repo.getPhotoById(id) { result ->
                    when (result) {
                        is Result.Success -> {
                            val photo = result.successResult

                            Picasso.get().load(photo.thumb).into(imageView)

                            description.text = photo.description
                        }
                        is Result.Failure -> description.text = "failwhale"
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_photo, parent, false)
            return PhotoViewHolder(view)
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            holder.bind(getItem(position).id)
        }
    }
}

object PhotoDiff : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}