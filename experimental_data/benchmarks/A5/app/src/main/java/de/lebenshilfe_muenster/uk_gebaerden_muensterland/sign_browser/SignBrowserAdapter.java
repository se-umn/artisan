package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.Validate;

import java.text.DecimalFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SignBrowserAdapter extends RecyclerView.Adapter<SignBrowserAdapter.ViewHolder> {

    private final List<Sign> dataSet;
    private final Context context;
    private final OnSignBrowserAdapterSignClickedListener onSignBrowserAdapterSignClickedListener;

    public SignBrowserAdapter(OnSignBrowserAdapterSignClickedListener onSignBrowserAdapterSignClickedListener, Context context, List<Sign> dataSet) {
        Validate.notNull(onSignBrowserAdapterSignClickedListener, "Calling activity or fragment " +
                "has to implement the OnSignBrowserAdapterSignClickedListener");
        Validate.notNull(context);
        Validate.notNull(dataSet);
        this.onSignBrowserAdapterSignClickedListener = onSignBrowserAdapterSignClickedListener;
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public SignBrowserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.browser_row_layout, parent, false));
    }

    @SuppressLint("RecyclerView") // Suggested improvement does not work.
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imgSignIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnIconOrTxtSignName(dataSet.get(position));
            }
        });
        final String nameLocaleDe = dataSet.get(position).getNameLocaleDe();
        holder.txtSignName.setText(nameLocaleDe);
        holder.txtSignName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnIconOrTxtSignName(dataSet.get(position));
            }
        });
        final DecimalFormat decimalFormat = new DecimalFormat("  0; -0");
        holder.txtSignLearningProgress.setText(decimalFormat.format(dataSet.get(position).getLearningProgress()));
        holder.checkBoxStarred.setChecked(dataSet.get(position).isStarred());
        holder.checkBoxStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnCheckBoxStarred(dataSet.get(position));
            }
        });
    }

    private void handleClickOnIconOrTxtSignName(Sign sign) {
        this.onSignBrowserAdapterSignClickedListener.onSignBrowserAdapterSignClicked(sign);
    }

    private void handleClickOnCheckBoxStarred(Sign sign) {
        new UpdateSignTask().execute(sign);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    /**
     * Has to be implemented by activity or fragment using the {@link RecyclerView}.
     */
    public interface OnSignBrowserAdapterSignClickedListener {
        void onSignBrowserAdapterSignClicked(Sign sign);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imgSignIcon;
        public final TextView txtSignName;
        public final TextView txtSignLearningProgress;
        public final CheckBox checkBoxStarred;

        public ViewHolder(View v) {
            super(v);
            this.imgSignIcon = (ImageView) v.findViewById(R.id.icon);
            this.txtSignName = (TextView) v.findViewById(R.id.signName);
            this.txtSignLearningProgress = (TextView) v.findViewById(R.id.learningProgressValue);
            this.checkBoxStarred = (CheckBox) v.findViewById(R.id.starred);
        }
    }

    private class UpdateSignTask extends AsyncTask<Sign, Void, Void> {

        @Override
        protected Void doInBackground(Sign... params) {
            Validate.exclusiveBetween(0, 2, params.length, "Exactly one sign as a parameter allowed.");
            final Sign sign = params[0];
            if (sign.isStarred()) {
                sign.setStarred(false);
            } else {
                sign.setStarred(true);
            }
            final SignDAO signDAO = SignDAO.getInstance(SignBrowserAdapter.this.context);
            signDAO.open();
            signDAO.update(sign);
            signDAO.close();
            return null;
        }
    }

}
