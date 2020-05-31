package com.example.vsco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsco.MenuActivity;
import com.example.vsco.R;
import com.example.vsco.fragment.BrightnessFragment;
import com.example.vsco.viewmodel.EditItem;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditItemAdapter extends RecyclerView.Adapter<EditItemAdapter.MyViewHolder> {

    private List<EditItem> editItems;

    private EditItemAdapterListener listener;
    private Context mContext;
    private int selectedIndex = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.edit_item)
        ImageView thumbnail;

        @BindView(R.id.edit_name)
        TextView editName;

        public MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }


    public EditItemAdapter(Context context, List<EditItem> editItems, EditItemAdapterListener listener) {
        mContext = context;
        this.editItems = editItems;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final EditItem editItem = editItems.get(position);

        holder.thumbnail.setImageDrawable(editItem.image);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditSelected(editItem);
                notifyDataSetChanged();
            }
        });

        holder.editName.setText(editItem.editName);

        if (selectedIndex == position) {
            holder.editName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_selected));
        } else {
            holder.editName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_normal));
        }
    }

    @Override
    public int getItemCount() {
        return editItems.size();
    }

    public interface EditItemAdapterListener {
        void onEditSelected(EditItem item);
    }
}