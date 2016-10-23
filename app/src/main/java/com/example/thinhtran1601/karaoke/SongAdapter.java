package com.example.thinhtran1601.karaoke;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinh on 27/09/2016.
 */
public class SongAdapter extends ArrayAdapter<Song> implements Filterable {
    ArrayList<Song> songs;
    ArrayList<Song> mask;
    SongFilter songFilter;

    public SongAdapter(Activity context, int resource, List<Song> objects) {
        super(context, resource, objects);
        songFilter = new SongFilter();
        mask = new ArrayList<>();
        this.songs = (ArrayList<Song>) objects;
        for(Song song : songs){
            mask.add(new Song(song.getCode(),song.getNameOfSong(),song.getSinger(),song.isLiked()));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        view = layoutInflater.inflate(R.layout.item,parent,false);
        Song song = getItem(position);
        TextView code = (TextView) view.findViewById(R.id.code_text_view);
        TextView songName = (TextView) view.findViewById(R.id.name_text_view);
        TextView singer = (TextView) view.findViewById(R.id.singer_text_view);

        code.setText(song.getCode());
        songName.setText(song.getNameOfSong());
        singer.setText(song.getSinger());

        return view;
    }

    @Override
    public Filter getFilter() {
        return songFilter;
    }

    public class SongFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() >0){
                ArrayList<Song> result = new ArrayList<>();
                for (Song song : mask){
                    if (song.getNameOfSong().toUpperCase().contains(constraint.toString().toUpperCase())){
                        result.add(song);
                    }
                }
                filterResults.count = result.size();
                filterResults.values = result;
            } else {
                filterResults.count = mask.size();
                filterResults.values = mask;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Song> songs = (ArrayList<Song>) results.values;
            clear();
            for(Song song : songs){
                add(song);
            }
            if (results.count > 0){
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
