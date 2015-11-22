//HW 5
//GridAdapter.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.tedradiohourpodcast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by chandra on 10/17/2015.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridHolder> {

    ArrayList<Item> itemsList;
    Context context;
    public static final String REQUEST_INTENT = "Item";
    MainActivityUtility utility;

    public GridAdapter(ArrayList<Item> itemsList, Context context,MainActivityUtility utility) {
        this.itemsList = itemsList;
        this.context = context;
        this.utility = utility;
    }

    @Override
    public GridHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview, viewGroup, false);

        GridHolder holder = new GridHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GridHolder gridHolder, final int i) {

        gridHolder.title.setText(itemsList.get(i).getTitle());
        Picasso.with(context).load(itemsList.get(i).getImage_url()).into(gridHolder.image);
        gridHolder.play.setImageResource(R.drawable.ic_play_circle_outline_white_24dp);

        gridHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utility.display(itemsList.get(i).getAudio_url(), itemsList.get(i).getDuration());
            }
        });

        gridHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Play.class);
                intent.putExtra(REQUEST_INTENT, itemsList.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public static class GridHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;
        LinearLayout parentLayout;
        ImageView play;

        public GridHolder(View itemsView) {
            super(itemsView);
            parentLayout = (LinearLayout) itemsView.findViewById(R.id.parentLayout);
            title = (TextView) itemsView.findViewById(R.id.title);
            image = (ImageView) itemsView.findViewById(R.id.image);
            play = (ImageView) itemsView.findViewById(R.id.playImage);

        }
    }
}
