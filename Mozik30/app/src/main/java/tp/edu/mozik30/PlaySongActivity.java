package tp.edu.mozik30;


import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;


import tp.edu.mozik30.util.AppUtil;

public class PlaySongActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://p.scdn.co/mp3-preview/";

    private String songId = " ";
    private String title = " ";
    private String artist = " ";
    private String fileLink = " ";
    private String coverArt = " ";
    private String url = " ";

    private double startTime = 0;
    private double finalTime = 0;
    private Handler handler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    public static int oneTimeOnly = 0;

    private MediaPlayer player = null;
    private Button btnPlayPause = null;
    private Button btnRepeat;
    private Button repeatButton;
    private int pro = 0;



    private SongCollection songCollection = new SongCollection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        seekbar = findViewById(R.id.Seekbar);
        seekbar.setMax(100);
        seekbar.setProgress(0);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        retrieveData();
        displaySong(title, artist, coverArt);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (player != null)
                        player.seekTo(progress);
                    else {
                        pro = progress;
                        Log.d("myapp", "onProgressChanged: " + pro);

                    }
                }
//


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (player != null) {
                startTime = player.getCurrentPosition();
                seekbar.setProgress((int) startTime);
                handler.postDelayed(this, 100);
            }
        }
    };

    public void Repeat(View view) {






        int i = 0;



        if (i == 0) {


            btnRepeat.setText("");
            player.setLooping(true);



        }
        if (i == 1){



            btnRepeat.setText("");
            player.setLooping(false);


        }
    }




    public void playPrevious(View view) {
        Song prevSong = songCollection.getPrevSong(songId);
        if (prevSong != null) {
            songId = prevSong.getId();
            title = prevSong.getTitle();
            artist = prevSong.getArtist();
            fileLink = prevSong.getFileLink();
            coverArt = prevSong.getCoverArt();

            url = BASE_URL + fileLink;
            displaySong(title, artist, coverArt);
            stopActivities();
            playOrPauseMusic(view);

            int temp = (int) startTime;

            if ((temp - backwardTime) > 0) {
                startTime = startTime - backwardTime;
                player.seekTo((int) startTime);
            } else {

            }
        }

    }


    private void stopActivities() {
        if (player != null) {
            player.stop();
            player = null;
        }
    }

    public void playShuffle(View view) {
        Song playshuffle = songCollection.shuffleSong(songId);
        if (playshuffle != null) {
            songId = playshuffle.getId();
            title = playshuffle.getTitle();
            artist = playshuffle.getArtist();
            fileLink = playshuffle.getFileLink();
            coverArt = playshuffle.getCoverArt();

            url = BASE_URL + fileLink;
            displaySong(title, artist, coverArt);
            stopActivities();
            playOrPauseMusic(view);

        }
    }


    public void playNext(View view) {
        Song nextSong = songCollection.getNextSong(songId);
        if (nextSong != null) {
            songId = nextSong.getId();
            title = nextSong.getTitle();
            artist = nextSong.getArtist();
            fileLink = nextSong.getFileLink();
            coverArt = nextSong.getCoverArt();

            url = BASE_URL + fileLink;
            displaySong(title, artist, coverArt);
            stopActivities();
            playOrPauseMusic(view);

            int temp = (int) startTime;

            if ((temp + forwardTime) <= finalTime) {
                startTime = startTime - forwardTime;
                player.seekTo((int) startTime);

            } else {
                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    player.seekTo((int) startTime);
                } else {

                }

            }


        }

    }


    public void playOrPauseMusic(View view) {
        if (player == null) {
            preparePlayer();
        }
        if (!player.isPlaying()) {

            player.start();


            seekbar.setMax((player.getDuration()));
            finalTime = player.getDuration();
            startTime = player.getCurrentPosition();
            btnPlayPause.setText("");
            btnPlayPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);

            if (oneTimeOnly == 0) {
                oneTimeOnly = 1;
            }
            if (pro > 0) player.seekTo(pro / 100 * player.getDuration());
            pro = 0;
            seekbar.setProgress((int) startTime);
            handler.postDelayed(UpdateSongTime, 100);


            setTitle("Now Playing: " + title + " - " + artist);
            gracefullyStopWhenMusicEnds();
        } else {
            pauseMusic();
            btnPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
        }


    }


    private void gracefullyStopWhenMusicEnds() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopActivities();

                if (player != null) {
                    btnPlayPause.setText("");
                    setTitle("");
                    player.stop();
                    player.release();
                    player = null;
                    seekbar.setMax(player.getDuration());

                }

            }
        });
    }

    private void pauseMusic() {
        player.pause();

        btnPlayPause.setText("");
    }


    private void preparePlayer() {
        player = new MediaPlayer();

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void displaySong(String title, String artist, String coverArt) {
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);

        TextView txtArtist = findViewById(R.id.txtArtist);
        txtArtist.setText(artist);

        int imageId = AppUtil.getImageIdFromDrawable(this, coverArt);

        ImageView ivCoverArt = findViewById(R.id.imgCoverArt);
        ivCoverArt.setImageResource(imageId);
    }

    private void retrieveData() {
        Bundle songData = this.getIntent().getExtras();
        songId = songData.getString("id");
        title = songData.getString("title");
        artist = songData.getString("artist");
        fileLink = songData.getString("fileLink");
        coverArt = songData.getString("coverArt");

        url = BASE_URL + fileLink;
    }
}


