package com.presldn.pouch.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.presldn.pouch.R;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.databinding.PouchListItemBinding;
import com.presldn.pouch.ui.activities.PouchActivity;

import java.util.List;

public class PouchRecyclerViewAdapter extends RecyclerView.Adapter<PouchRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "PouchRecyclerViewAdapte";

    private List<Pouch> mPouches;
    private Context mContext;

    public PouchRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public void setPouches(List<Pouch> pouches) {
        this.mPouches = pouches;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PouchListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.pouch_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bindAdapter(this);

        final Pouch pouch = mPouches.get(holder.getAdapterPosition());
        holder.bind(pouch);
    }

    public void onPouchClick(Pouch pouch) {
        Intent intent = new Intent(mContext.getApplicationContext(), PouchActivity.class);
        intent.putExtra("pouch_name", pouch.getName());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mPouches != null)
            return mPouches.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final PouchListItemBinding binding;


        ViewHolder(PouchListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindAdapter(PouchRecyclerViewAdapter adapter) {
            binding.setAdapter(adapter);
        }

        public void bind(Pouch pouch) {
            binding.setPouch(pouch);
            binding.executePendingBindings();
        }
    }
}
