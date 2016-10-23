package com.example.thinhtran1601.karaoke;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //item of layout
    ArrayList<Song> songs,favoriteSongs;
    SongAdapter songAdapter,favoriteAdapter;
    TabHost tabHost;
    ListView songListView, favoriteListView;

    //use for database connection
    String DATABASE_NAME = "Arirang.sqlite";
    String DB_Path = "/databases/";
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDatabase();
        addControls();
        loadItemFromDatabase();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.searching_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.karaoke_search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                songAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void loadItemFromDatabase() {
        Cursor cursor = database.rawQuery("select * from ArirangSongList",null);
        while(cursor.moveToNext()){
            String code = cursor.getString(0);
            String nameOfSong = cursor.getString(1);
            String singer = cursor.getString(3);
            int isLiked = cursor.getInt(5);
            if (isLiked == 0) {
                songs.add(new Song(code, nameOfSong, singer));
            } else {
                Song newSong = new Song(code, nameOfSong, singer, true);
                songs.add(newSong);
                favoriteSongs.add(newSong);
            }
            songAdapter.notifyDataSetChanged();
            favoriteAdapter.notifyDataSetChanged();
        }
    }

    private void setupDatabase() {
        File dbFile = new File(getApplicationInfo().dataDir + DB_Path + DATABASE_NAME);
        if (!dbFile.exists()){
            copyDatabaseFromAsset();
        }
    }

    private void copyDatabaseFromAsset() {
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            String outFileName = getApplicationInfo().dataDir + DB_Path + DATABASE_NAME;

            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_Path);
            if (!f.exists()) {
                f.mkdir();
            }

            OutputStream outputStream = new FileOutputStream(outFileName);

            //
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
        }
    }

    private void addControls() {
        songs = new ArrayList<>();
        favoriteSongs = new ArrayList<>();

        songAdapter = new SongAdapter(MainActivity.this, R.layout.item,songs);
        favoriteAdapter = new SongAdapter(MainActivity.this, R.layout.item,favoriteSongs);

        songListView = (ListView) findViewById(R.id.song_list_view);
        favoriteListView = (ListView) findViewById(R.id.favorite_song_list_view);

        songListView.setAdapter(songAdapter);
        favoriteListView.setAdapter(favoriteAdapter);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("songs");
        tab1.setIndicator("",getResources().getDrawable(R.drawable.music));
        tab1.setContent(R.id.song_list_view);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("favoriteSongs");
        tab2.setIndicator("",getResources().getDrawable(R.drawable.favorite));
        tab2.setContent(R.id.favorite_song_list_view);
        tabHost.addTab(tab2);

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
    }

    private void addEvents(){
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Song song = songs.get(position);
                ImageButton likeButton = (ImageButton)view.findViewById(R.id.like_image_button);
                likeButton.setVisibility(View.VISIBLE);
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!favoriteSongs.contains(song)) {
                            song.setLiked(true);
                            String updateQuery = "update ArirangSongList set YEUTHICH = '1' " +
                                                 "where MABH = '" + song.getCode() + "'";
                            database.execSQL(updateQuery);
                            favoriteSongs.add(song);
                            favoriteAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Added favourite song successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "This song was a favourite song", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Song song = favoriteSongs.get(position);
                ImageButton disDikeButton = (ImageButton)view.findViewById(R.id.dislike_image_button);
                disDikeButton.setVisibility(View.VISIBLE);
                disDikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        song.setLiked(false);
                        String updateQuery = "update ArirangSongList set YEUTHICH = '0' " +
                                             "where MABH = '" + song.getCode() + "'";
                        database.execSQL(updateQuery);
                        favoriteSongs.remove(song);
                        favoriteAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Removed favourite song successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



}
