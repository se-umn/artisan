package org.totschnig.myexpenses.model

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.net.Uri
import androidx.core.util.Pair
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import org.totschnig.myexpenses.model.Plan.Recurrence
import org.totschnig.myexpenses.provider.DatabaseConstants
import org.totschnig.myexpenses.provider.TransactionProvider
import org.totschnig.myexpenses.viewmodel.data.Tag
import java.io.Serializable

interface ITransaction: IModel {
    var status: Int
    var methodId: Long?
    var catId: Long?
    var categoryIcon: String?
    var label: String?
    var crStatus: CrStatus
    var equivalentAmount: Money?
    var originalAmount: Money?
    var referenceNumber: String?
    var payee: String?
    var comment: String?
    var valueDate: Long
    var date: Long
    var originTemplateId: Long?
    var amount: Money
    var accountId: Long
    var parentId: Long?
    var pictureUri: Uri?
    var originPlanInstanceId: Long?

    val isTransfer: Boolean
    val isSplit: Boolean

    fun setDate(zonedDateTime: ZonedDateTime)
    fun setValueDate(zonedDateTime: ZonedDateTime)
    fun setInitialPlan(initialPlan: Pair<Recurrence?, LocalDate?>)
    fun save(withCommit: Boolean): Uri?

    fun linkedTagsUri(): Uri
    fun linkColumn(): String

    fun saveTags(tags: List<Tag>?, contentResolver: ContentResolver): Boolean
}