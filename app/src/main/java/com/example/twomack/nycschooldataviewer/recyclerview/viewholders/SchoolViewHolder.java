package com.example.twomack.nycschooldataviewer.recyclerview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.twomack.nycschooldataviewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by twomack on 3/23/18.
 */

public class SchoolViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.listImageView)
    ImageView imageView;

    public SchoolViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public View getItemView() {
        return itemView;
    }
}
