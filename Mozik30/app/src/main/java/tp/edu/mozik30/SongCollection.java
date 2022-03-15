package tp.edu.mozik30;


import android.view.View;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class SongCollection {

    private  Song[] songs = new Song[6];

    public SongCollection(){

        prepareSongs();
    }

    public Song getPrevSong(String currentSongId){
        Song song = null;
        for(int index = 0; index < songs.length; index++){
            String tempSongId = songs[index].getId();
            if(tempSongId.equals(currentSongId) && (index > 0)){
                song = songs[index - 1];
                break;
            }
        }
        return song;
    }
    public Song getNextSong(String currentSongId){
        Song song = null;
        for(int index = 0; index < songs.length; index++){
            String tempSongId = songs[index].getId();
            if(tempSongId.equals(currentSongId) && (index < songs.length -1)){
                song = songs[index + 1];
                break;
            }
        }
        return song;
    }

    public Song shuffleSong(String currentSongId){
        Song song = null;
        for(int index = 0; index < songs.length; index++){
            Collections.shuffle(Arrays.asList(songs));
            song = songs [index];
                break;

        }
        return song;
    }






    public Song searchById(String id){
        Song song = null;

        for (int index = 0; index < songs.length; index++) {
            song = songs[index];
            if(song.getId().equals(id)){
                return song;
            }
        }
        return song;
    }



    private void prepareSongs(){

        Song path = new Song("S1001", "Path", "Apocalyptica ",
                "1d270fc54c6c779f7feeea61af30863f8947c23f?cid=2afe87a64b0042dabf51f37318616965",
                3.1, "apocalyptica_path");
        Song spaceOddity = new Song( "S1002", "Space Oddity", "David Bowie",
                "deccb21d2fdbb555e5831b790ba9b56de1dba309?cid=2afe87a64b0042dabf51f37318616965",
                4.1, "cracked_actor");
        Song cheapThrill = new Song( "S1003", "Cheap Thrill", "Sia",
                "88816b2040a092aa99d5b0e42945d79dc5027c1a?cid=2afe87a64b0042dabf51f37318616965",
                3.53, "sia");
        Song bohemianRhapsody = new Song( "S1004", "Bohemian Rhapsody", "Queen",
                "05a688419454f37ecac7617703f84d55d5e71c0c?cid=2afe87a64b0042dabf51f37318616965",
                6.19, "bohemian_rapsody");
        Song home = new Song( "S1005", "Home", "Marshmello",
                "8c82617666cb4787b1a1260d509845f01edcd1a4?cid=2afe87a64b0042dabf51f37318616965",
                3.81, "marshmello");
        Song alone = new Song( "S1006", "Alone", "Alan Walker",
                "7919623d0a448486305fe5a7f8a1459e9a72f535?cid=2afe87a64b0042dabf51f37318616965",
                2.67, "alan_walker");
        songs[0] = path;
        songs[1] = spaceOddity;
        songs[2] = cheapThrill;
        songs[3] = bohemianRhapsody;
        songs[4] = home;
        songs[5] = alone;

    }
}
