package by.brstu.redlabrat.testlistapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.brstu.redlabrat.testlistapp.api.OmdbSearchResultMovie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.core.SingleOnSubscribe
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favorites_list.*

class FavoritesListFragment : Fragment() {

    private var listAdapter: MyListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_favorites_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity().application as? TestListApp)?.let {  app ->
            listAdapter = MyListAdapter(emptyList(),
                (activity?.application as? TestListApp)?.omdbDao,
                requireActivity() as MyListAdapter.MyItemClickListener)
            recyclerView.adapter = listAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            Single.create<List<OmdbSearchResultMovie>> { emiter ->
                SingleOnSubscribe<List<OmdbSearchResultMovie>> { single ->
                    val list = app.omdbDao.getAllSavedMovies()
                    single.onSuccess(list)
                }
                    .subscribe(emiter)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data, error ->
                    listAdapter?.setNewListOfItems(data)
                }
        }
    }
}