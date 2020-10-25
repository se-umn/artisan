package org.totschnig.myexpenses.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.totschnig.myexpenses.MyApplication
import org.totschnig.myexpenses.model.CurrencyContext
import org.totschnig.myexpenses.model.Transaction
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_PARENTID
import org.totschnig.myexpenses.provider.DatabaseConstants.KEY_ROWID
import org.totschnig.myexpenses.viewmodel.data.Transaction.Companion.fromCursor
import org.totschnig.myexpenses.viewmodel.data.Transaction.Companion.projection
import javax.inject.Inject
import org.totschnig.myexpenses.viewmodel.data.Transaction as TData

class TransactionDetailViewModel(application: Application) : TransactionViewModel(application) {
    @Inject
    lateinit var currencyContext: CurrencyContext

    init {
        (application as MyApplication).appComponent.inject(this)
    }

    private val transactionLiveData: Map<Long, LiveData<List<TData>>> = lazyMap { transactionId ->
        val liveData = MutableLiveData<List<TData>>()
        disposable =  briteContentResolver.createQuery(
                Transaction.EXTENDED_URI,
                projection(application), "%s = ? OR %s = ?".format(KEY_ROWID, KEY_PARENTID), Array(2) { transactionId.toString() }, KEY_PARENTID + " IS NULL DESC", false)
                .mapToList { fromCursor(it, currencyContext) }
                .subscribe { list ->
                    liveData.postValue(list)
                }
        return@lazyMap liveData
    }

    fun transaction(transactionId: Long): LiveData<List<TData>> = transactionLiveData.getValue(transactionId)
}