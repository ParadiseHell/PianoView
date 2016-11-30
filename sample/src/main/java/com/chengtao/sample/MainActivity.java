package com.chengtao.sample;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.impl.OnLoadAudioListener;
import com.chengtao.pianoview.impl.OnPianoClickListener;
import com.chengtao.pianoview.view.PianoView;

public class MainActivity extends AppCompatActivity implements OnPianoClickListener,OnLoadAudioListener ,SeekBar.OnSeekBarChangeListener{
    private PianoView pianoView;
    private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pianoView = (PianoView) findViewById(R.id.pv);
        seekBar = (SeekBar) findViewById(R.id.sb);
        pianoView.setOnPianoClickListener(this);
        pianoView.setOnLoadMusicListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onPianoClick(Piano.PianoKeyType type, Piano.PianoVoice voice, int group, int positionOfGroup) {
        Log.e("TAG","Type:"+type +"---Voice:"+voice);
        Log.e("TAG","Group:"+group + "---" + "Position:"+positionOfGroup);
    }

    @Override
    public void loadPianoAudioStart() {
        Toast.makeText(getApplicationContext(),"loadPianoMusicStart",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadPianoAudioFinish() {
        Toast.makeText(getApplicationContext(),"loadPianoMusicFinish",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadPianoAudioError(Exception e) {
        Toast.makeText(getApplicationContext(),"loadPianoMusicError",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadPianoAudioProgress(int progress) {
        Log.e("TAG","progress:"+progress);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        pianoView.scroll(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
