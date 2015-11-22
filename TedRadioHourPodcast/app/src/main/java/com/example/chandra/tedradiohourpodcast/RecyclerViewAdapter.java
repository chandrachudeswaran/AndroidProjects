//HW 5
//RecyclerViewAdapter.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.tedradiohourpodcast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by chandra on 10/17/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemsHolder> {

    ArrayList<Item> itemsList;
    Context context;
    MainActivityUtility utility;
    public static final String REQUEST_INTENT = "Item";

    public RecyclerViewAdapter(ArrayList<Item> itemsList, Context context, MainActivityUtility utility) {
        this.itemsList = itemsList;
        this.context = context;
        this.utility = utility;
    }

    @Override
    public ItemsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycleview, viewGroup, false);
        ItemsHolder holder = new ItemsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ItemsHolder itemsHolder, final int i) {


        itemsHolder.title.setText(itemsList.get(i).getTitle());
        StringTokenizer stringTokenizer = new StringTokenizer(itemsList.get(i).getDate());

        itemsHolder.date.setText("posted:" + " " + stringTokenizer.nextElement().toString() + " " + stringTokenizer.nextElement().toString() + "," + " " + stringTokenizer.nextElement().toString() + "," + " " + stringTokenizer.nextElement().toString());
        itemsHolder.play.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        itemsHolder.now.setText("Play Now");

        Picasso.with(context).load(itemsList.get(i).getImage_url()).into(itemsHolder.image);

        itemsHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utility.display(itemsList.get(i).getAudio_url(), itemsList.get(i).getDuration());
            }
        });

        itemsHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
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


    public static class ItemsHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;
        ImageView play;
        TextView now;
        ImageView image;
        LinearLayout parentLayout;


        public ItemsHolder(View itemsView) {
            super(itemsView);

            parentLayout = (LinearLayout) itemsView.findViewById(R.id.parentLayout);
            title = (TextView) itemsView.findViewById(R.id.title);
            date = (TextView) itemsView.findViewById(R.id.date);
            play = (ImageView) itemsView.findViewById(R.id.playImage);
            now = (TextView) itemsView.findViewById(R.id.play);
            image = (ImageView) itemsView.findViewById(R.id.image);


        }
    }
}
