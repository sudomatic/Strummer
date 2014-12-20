package net.gulluman.strummer.strummer;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StrummerActivity extends Activity {

    List<StrumPattern> model = new ArrayList<StrumPattern>();
    StrumPatternAdapter adapter = null;
    MediaPlayer player = null;
    Button stopButton = null;
    CheckBox loopCheckBox = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strummer);

        // Assign objects
        ListView list = (ListView)findViewById(R.id.mainListView);
        stopButton = (Button)findViewById(R.id.stopButton);
        loopCheckBox = (CheckBox)findViewById(R.id.loopCheckBox);
        adapter = new StrumPatternAdapter();
        player = new MediaPlayer();

        // Making connections
        list.setAdapter(adapter);
        list.setOnItemClickListener(onPatternClick);
        stopButton.setOnClickListener(stopButtonClickListener);

        // now feed the data
        StrumPattern strum = null;
        String [] imageAssets = listAssetFiles("images");
        for(String imageFile : imageAssets) {
            strum = new StrumPattern();
            strum.setStrumImageFile("images/" + imageFile);
            strum.setStrumAudioFile("sounds/" + imageFile.replace(".gif", ".mp3"));
            adapter.add(strum);
        }
    }

    private View.OnClickListener stopButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            player.stop();
        }
    };

    private AdapterView.OnItemClickListener onPatternClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Test for click
            // Toast.makeText(view.getContext(), "hi", Toast.LENGTH_LONG).show();
            StrumPattern pattern = model.get(position);
            //play music
            player.stop();
            player.seekTo(0);
            try {
                AssetFileDescriptor afd = getAssets().openFd(pattern.getStrumAudioFile());
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                player.setLooping(loopCheckBox.isChecked());
                player.prepare();
                player.start();
            } catch (IOException e) {
                Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private String[] listAssetFiles(String dirFrom) {
        Resources res = getResources(); //if you are in an activity
        AssetManager am = res.getAssets();
        try {
            return am.list(dirFrom);
        } catch(Exception e) {
            return null;
        }
    }

    class StrumPatternAdapter extends ArrayAdapter<StrumPattern> {
        StrumPatternAdapter() {
            super(StrummerActivity.this, R.layout.pattern, model);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            StrumPatternHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.pattern, parent, false);
                holder = new StrumPatternHolder(row);
                row.setTag(holder);
            } else {
                holder = (StrumPatternHolder)row.getTag();
            }
            holder.populateFrom(model.get(position), row);
            return(row);
        }

    }

    static class StrumPatternHolder {
        private ImageView icon = null;

        StrumPatternHolder(View row) {
            icon = (ImageView)row.findViewById(R.id.icon);
        }
        public void populateFrom(StrumPattern pattern, View view) {
            try {
                InputStream imgFile = view.getContext().getAssets().open(pattern.getStrumImageFile());
                //afd = view.getContext().getAssets().openFd(pattern.getStrumImageFile());
                Bitmap myBitmap = BitmapFactory.decodeStream(imgFile);     //.d.decodeFileDescriptor(afd);         //.decodeFile(imgFile.getAbsolutePath());
                imgFile.close();
                icon.setImageBitmap(myBitmap);
            } catch (Exception e) {
                Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
