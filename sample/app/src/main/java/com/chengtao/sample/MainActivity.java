package com.chengtao.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.impl.OnPianoClickListener;
import com.chengtao.pianoview.view.PianoView;

public class MainActivity extends AppCompatActivity implements OnPianoClickListener{
    private PianoView pianoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pianoView = (PianoView) findViewById(R.id.pv);
        pianoView.setOnPianoClickListener(this);
    }

    @Override
    public void onPianoClick(Piano.PianoKeyType type, Piano.PianoVoice voice, int group, int positionOfGroup) {
        Log.e("TAG","Type:"+type +"---Voice:"+voice);
        Log.e("TAG","Group:"+group + "---" + "Position:"+positionOfGroup);
    }
}
