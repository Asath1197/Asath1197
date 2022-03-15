package tp.edu.mozik30;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import tp.edu.mozik30.util.AppUtil;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerlayout ;
    private ActionBarDrawerToggle mToggle;

    private SongCollection songCollection = new SongCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login_form.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawermenu, menu);
        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {

           return true;
       }

        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            Logout();
            return true;

        }

        DrawerLayout drawer= (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
//        if (mToggle.onOptionsItemSelected(item)) {
//
//            return true;
//        }
//
//
//
//
//
//
//        return super.onOptionsItemSelected(item);



    }













    public void handleSelection(View view) {
        String resourceId = AppUtil.getResourceId(this, view);

        Song selectedSong = songCollection.searchById(resourceId);

        AppUtil.popMessage(this, "Streaming song: " + selectedSong.getTitle());

        sendDataToActivity(selectedSong);
    }

    public void sendDataToActivity(Song song){
        Intent intent = new Intent(this, PlaySongActivity.class);
        intent.putExtra("id", song.getId());
        intent.putExtra("title", song.getTitle());
        intent.putExtra("artist", song.getArtist());
        intent.putExtra("fileLink", song.getFileLink());
        intent.putExtra("coverArt", song.getCoverArt());

        startActivity(intent);
    }
}
