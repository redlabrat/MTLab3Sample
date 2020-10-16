package by.brstu.redlabrat.testlistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.brstu.redlabrat.testlistapp.api.OmdbSearchResultMovie
import by.brstu.redlabrat.testlistapp.db.MovieDao
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.MaybeOnSubscribe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleOnSubscribe
import io.reactivex.rxjava3.schedulers.Schedulers

class MyListAdapter(private var listOfItems: List<OmdbSearchResultMovie>,
                    private val dbDao: MovieDao?,
                    private val onItemClickListener: MyItemClickListener
) : RecyclerView.Adapter<MyListAdapter.MyTestItemViewHolder>() {

    interface MyItemClickListener {
        fun onMyItemClick(selectedStr: OmdbSearchResultMovie)
        fun addToFavorites(selectedMovie: OmdbSearchResultMovie)
    }

    fun setNewListOfItems(newList: List<OmdbSearchResultMovie>) {
        listOfItems = newList
        notifyDataSetChanged()
    }

    class MyTestItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.listItemImageView)
        val textView: TextView = itemView.findViewById(R.id.listItemTextView)
        val button: ImageButton = itemView.findViewById(R.id.favoritesImageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTestItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyTestItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: MyTestItemViewHolder, position: Int) {
        val movie = listOfItems[position]
        holder.textView.text = movie.title
        Glide.with(holder.itemView.context)
            .load(movie.posterUrl)
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            onItemClickListener.onMyItemClick(movie)
        }
        holder.button.setOnClickListener {
            onItemClickListener.addToFavorites(movie)
            this.notifyItemChanged(position)
        }
        findMovieById(movie.imdbId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isMovieFavorite, error ->
                if (isMovieFavorite) {
                    holder.button.setImageResource(R.drawable.heart_red)
                }
            }
    }

    private fun findMovieById(id: String): Single<Boolean> {
        return Single.create<Boolean> {
            SingleOnSubscribe<Boolean> { single ->
                val movie = dbDao?.getMovieById(id)
                if (movie != null) {
                    single.onSuccess(true)
                } else {
                    single.onSuccess(false)
                }
            }
                .subscribe(it)
        }
    }
}