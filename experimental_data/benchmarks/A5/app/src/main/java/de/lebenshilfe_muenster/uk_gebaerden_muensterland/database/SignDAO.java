package de.lebenshilfe_muenster.uk_gebaerden_muensterland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import androidx.annotation.NonNull;

import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.BOOLEAN_TRUE;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.EQUAL_SIGN;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.LIKE;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.OR;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.QUESTION_MARK;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.ALL_COLUMNS;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.COLUMN_NAME_LEARNING_PROGRESS;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.COLUMN_NAME_MNEMONIC;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.COLUMN_NAME_SIGN_NAME;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.COLUMN_NAME_SIGN_NAME_DE;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.COLUMN_NAME_STARRED;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.COLUMN_NAME_TAG1;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.COLUMN_NAME_TAG2;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.COLUMN_NAME_TAG3;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.IS_STARRED;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.NAME_LOCALE_DE_LIKE;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.ORDER_BY_LEARNING_PROGRESS_ASC;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.ORDER_BY_NAME_DE_ASC;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.TABLE_NAME;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.TAG1_LOCALE_DE_LIKE;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.TAG2_LOCALE_DE_LIKE;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable.TAG3_LOCALE_DE_LIKE;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.DbContract.SignTable._ID;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SignDAO {

    private static final int RANDOM_SIGNS_QUEUE_MAX_SIZE = 5;
    private static final String TAG = SignDAO.class.getSimpleName();
    private static final boolean FIFO_ORDER = true;
    private static SignDAO instance;
    private final SQLiteOpenHelper openHelper;
    private final Queue<Sign> randomSignsQueue = new ArrayBlockingQueue<>(RANDOM_SIGNS_QUEUE_MAX_SIZE, FIFO_ORDER);
    private SQLiteDatabase database;

    /**
     * Private constructor
     */
    private SignDAO(Context context) {
        this.openHelper = new DbHelper(context);
    }

    /**
     * Singleton instance of the SignDAO
     */
    public static synchronized SignDAO getInstance(Context context) {
        if (null == instance) {
            instance = new SignDAO(context);
        }
        return instance;
    }

    public void open() throws SQLException {
        Log.d(TAG, "Opening database.");
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        Log.d(TAG, "Closing database.");
        if (null != this.database) {
            this.openHelper.close();
        }
    }

    /**
     * Persist a list of signs. For <strong>testing</strong> purposes only.
     *
     * @param signs a list of signs, which hast not been persisted yet.
     * @return a list of persisted signs.
     */
    @SuppressWarnings("UnusedReturnValue")
    List<Sign> create(List<Sign> signs) {
        final List<Sign> createdSigns = new ArrayList<>();
        for (Sign sign : signs) {
            createdSigns.add(create(sign));
        }
        return createdSigns;
    }

    /**
     * Persist a sign. For <strong>testing</strong> purposes only.
     *
     * @param sign a Sign, which has not been persisted yet.
     * @return the persisted sign, <code>null</code> if persisting failed.
     */
    Sign create(Sign sign) {
        Log.d(TAG, "Creating sign: " + sign);
        this.database.beginTransaction();
        Sign createdSign;
        try {
            final ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_SIGN_NAME, sign.getName());
            values.put(COLUMN_NAME_SIGN_NAME_DE, sign.getNameLocaleDe());
            values.put(COLUMN_NAME_MNEMONIC, sign.getMnemonic());
            // For testing purposes, the tags are saved in a single column here
            values.put(COLUMN_NAME_TAG1, sign.getTags());
            if (sign.isStarred()) {
                values.put(COLUMN_NAME_STARRED, 1);
            } else {
                values.put(COLUMN_NAME_STARRED, 0);
            }
            values.put(COLUMN_NAME_LEARNING_PROGRESS, sign.getLearningProgress());
            final long insertId = this.database.insert(TABLE_NAME, null,
                    values);
            if (-1 == insertId) {
                throw new IllegalStateException(MessageFormat.format("Inserting sign: {0} failed due to" +
                        " a database error!", sign));
            }
            createdSign = readSingleSign(insertId);
            this.database.setTransactionSuccessful();
            Log.d(TAG, "Created sign: " + createdSign);
        } finally {
            this.database.endTransaction();
        }
        return createdSign;
    }

    /**
     * Read all signs.
     *
     * @return a list of signs, may be empty but not null.
     */
    public List<Sign> read() {
        return readInternal(StringUtils.EMPTY, false, false);
    }

    /**
     * Read the signs where the name_locale_de or the tags matches the parameter.
     *
     * @param whereSignNameLocaleDeOrTagsLike the sign name or the tags with locale de to look for.
     * @return a list of signs, may be empty but not null.
     */
    public List<Sign> read(String whereSignNameLocaleDeOrTagsLike) {
        return readInternal(whereSignNameLocaleDeOrTagsLike, false, false);
    }

    /**
     * Read the signs which have been starred by the user.
     *
     * @return all the signs, which have been starred by the user.
     */
    public List<Sign> readStarredSignsOnly() {
        return readInternal(StringUtils.EMPTY, true, false);
    }

    /**
     * From the signs with the least learning progress a random sign will be returned.
     * The random sign will never be the same as the last five non-null signs provided via the
     * currentSign parameter before.
     *
     * @param currentSign the current sign shown to the user. Can be <code>null</code>.
     * @return a random sign, null if no or less than seven signs exists in the database.
     */
    public Sign readRandomSign(Sign currentSign) {
        final List<Sign> signsOrderedByLearningProgress = readInternal(StringUtils.EMPTY, false, true);
        if (signsOrderedByLearningProgress.size() < 7) {
            return null;
        }
        if (RANDOM_SIGNS_QUEUE_MAX_SIZE == this.randomSignsQueue.size()) {
            this.randomSignsQueue.poll();
        }
        if (null != currentSign) {
            this.randomSignsQueue.offer(currentSign);
        }
        final Sign[] upToTheLastFiveRandomSigns = this.randomSignsQueue.toArray(new Sign[0]);
        for (Sign upToTheLastFiveRandomSign : upToTheLastFiveRandomSigns) {
            signsOrderedByLearningProgress.remove(upToTheLastFiveRandomSign);
        }
        final Sign signWithLeastLearningProgress = signsOrderedByLearningProgress.get(0);
        signsOrderedByLearningProgress.remove(signWithLeastLearningProgress);
        final List<Sign> signsWithLeastLearningProgress = new ArrayList<>();
        for (int i = 0; i < signsOrderedByLearningProgress.size(); i++) {
            if (signWithLeastLearningProgress.getLearningProgress()
                    == signsOrderedByLearningProgress.get(i).getLearningProgress()) {
                signsWithLeastLearningProgress.add(signsOrderedByLearningProgress.get(i));
            } else {
                break;
            }
        }
        signsWithLeastLearningProgress.add(signWithLeastLearningProgress);
        final int randomInt = new Random().nextInt(signsWithLeastLearningProgress.size());
        return signsWithLeastLearningProgress.get(randomInt);
    }

    @NonNull
    private List<Sign> readInternal(String signNameLocaleDeOrTagsLike, boolean readStarredSignsOnly, boolean readOrderedByLearningProgress) {
        final List<Sign> signs = new ArrayList<>();
        Cursor cursor;
        if (StringUtils.isNotEmpty(signNameLocaleDeOrTagsLike)) {
            Log.d(TAG, MessageFormat.format("Reading signs with name_locale_de like: {0}", signNameLocaleDeOrTagsLike));
            String selectionArgument = "%" + signNameLocaleDeOrTagsLike + "%";
            String[] selectionArgs = {selectionArgument, selectionArgument, selectionArgument, selectionArgument};
            cursor = database.query(TABLE_NAME,
                    ALL_COLUMNS, NAME_LOCALE_DE_LIKE + OR + TAG1_LOCALE_DE_LIKE + OR + TAG2_LOCALE_DE_LIKE + OR + TAG3_LOCALE_DE_LIKE,
                    selectionArgs, null, null, ORDER_BY_NAME_DE_ASC);
        } else if (readStarredSignsOnly) {
            Log.d(TAG, "Reading starred signs only");
            cursor = database.query(TABLE_NAME,
                    ALL_COLUMNS, IS_STARRED,
                    new String[]{BOOLEAN_TRUE}, null, null, ORDER_BY_NAME_DE_ASC);
        } else if (readOrderedByLearningProgress) {
            Log.d(TAG, "Reading signs ordered by learning progress ascending");
            cursor = database.query(TABLE_NAME,
                    ALL_COLUMNS, null,
                    null, null, null, ORDER_BY_LEARNING_PROGRESS_ASC);
        } else {
            Log.d(TAG, "Reading all signs");
            cursor = database.query(TABLE_NAME,
                    ALL_COLUMNS, null, null, null, null, ORDER_BY_NAME_DE_ASC);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final Sign sign = cursorToSign(cursor);
            signs.add(sign);
            cursor.moveToNext();
        }
        cursor.close();
        return signs;
    }

    public Sign update(Sign sign) {
        Log.d(TAG, "Updating sign: " + sign);
        this.database.beginTransaction();
        Sign updatedSign;
        try {
            final ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_LEARNING_PROGRESS, sign.getLearningProgress());
            values.put(COLUMN_NAME_STARRED, sign.isStarred());
            final String selection = _ID + LIKE;
            final String[] selectionArgs = {String.valueOf(sign.getId())};
            int rowsUpdated = this.database.update(
                    TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            if (0 == rowsUpdated) {
                throw new IllegalStateException(MessageFormat.format("Updating sign {0} updated no rows!", sign));
            }
            if (1 > rowsUpdated) {
                throw new IllegalStateException(MessageFormat.format("Updating sign {0} updated more than " +
                        "one row. {1} rows were updated.", sign, rowsUpdated));
            }
            updatedSign = readSingleSign(sign.getId());
            this.database.setTransactionSuccessful();
        } finally {
            this.database.endTransaction();
        }
        return updatedSign;
    }

    /**
     * For <strong>testing</strong> purposes only!
     */
    void delete(List<Sign> signs) {
        for (Sign sign : signs) {
            delete(sign);
        }
    }

    /**
     * For <strong>testing</strong> purposes only!
     */
    void delete(Sign sign) {
        Log.d(TAG, MessageFormat.format("Deleting sign {0}", sign));
        this.database.beginTransaction();
        try {
            this.database.delete(TABLE_NAME,
                    COLUMN_NAME_SIGN_NAME + EQUAL_SIGN + QUESTION_MARK,
                    new String[]{sign.getName()});
            this.database.setTransactionSuccessful();
        } finally {
            this.database.endTransaction();
        }
    }

    private Sign readSingleSign(long id) {
        final Sign createdSign;
        final Cursor cursor = this.database.query(TABLE_NAME,
                ALL_COLUMNS, _ID + EQUAL_SIGN + id, null,
                null, null, null);
        if (0 == cursor.getCount()) {
            throw new IllegalStateException(MessageFormat.format("Querying for sign with id: {1} " +
                    "yielded no results!", id));
        }
        cursor.moveToFirst();
        createdSign = cursorToSign(cursor);
        cursor.close();
        return createdSign;
    }

    private Sign cursorToSign(Cursor cursor) {
        final Sign.Builder signBuilder = new Sign.Builder();
        signBuilder.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
        signBuilder.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SIGN_NAME)));
        signBuilder.setNameLocaleDe(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SIGN_NAME_DE)));
        signBuilder.setMnemonic(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MNEMONIC)));
        signBuilder.setTags(getTagsText(cursor));
        signBuilder.setLearningProgress(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_LEARNING_PROGRESS)));
        final int starred = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STARRED));
        if (1 == starred) {
            signBuilder.setStarred(true);
        } else {
            signBuilder.setStarred(false);
        }
        return signBuilder.create();
    }

    private String getTagsText(Cursor cursor) {

        final List<String> tagColumns = new ArrayList<>();
        tagColumns.add(COLUMN_NAME_TAG1);
        tagColumns.add(COLUMN_NAME_TAG2);
        tagColumns.add(COLUMN_NAME_TAG3);

        final StringBuilder tags = new StringBuilder();
        final String separator = ", ";
        for (String tagColumn : tagColumns) {
            final String tagColumnValue = cursor.getString(cursor.getColumnIndex(tagColumn));
            if (StringUtils.isNotEmpty(tagColumnValue)) {
                tags.append(tagColumnValue);
                tags.append(separator);
            }
        }
        final String tag = tags.toString();
        return StringUtils.removeEnd(tag, separator);
    }

}
