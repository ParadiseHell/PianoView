package com.chengtao.sample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.chengtao.pianoview.entity.AutoPlayEntity;
import com.chengtao.pianoview.entity.Piano;
import com.chengtao.pianoview.impl.OnLoadAudioListener;
import com.chengtao.pianoview.impl.OnPianoAutoPlayListener;
import com.chengtao.pianoview.impl.OnPianoClickListener;
import com.chengtao.pianoview.view.PianoView;

import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends Activity implements OnPianoClickListener,OnLoadAudioListener ,SeekBar.OnSeekBarChangeListener,View.OnClickListener,OnPianoAutoPlayListener{
    private PianoView pianoView;
    private SeekBar seekBar;
    private Button leftArrow;
    private Button rightArrow;
    private Button btnMusic;
    private int scrollProgress = 0;
    private final static float SEEKBAR_OFFSET_SIZE = -12;
    //
    private boolean isPlay = false;
    private ArrayList<AutoPlayEntity> litterStarList = null;
    private static final long LITTER_STAR_BREAK_SHORT_TIME = 500;
    private static final long LITTER_STAR_BREAK_LONG_TIME = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pianoView = (PianoView) findViewById(R.id.pv);
        seekBar = (SeekBar) findViewById(R.id.sb);
        seekBar.setThumbOffset((int)convertDpToPixel(SEEKBAR_OFFSET_SIZE));
        leftArrow = (Button) findViewById(R.id.iv_left_arrow);
        rightArrow = (Button) findViewById(R.id.iv_right_arrow);
        btnMusic = (Button) findViewById(R.id.iv_music);
        pianoView.setOnPianoClickListener(this);
        pianoView.setOnLoadMusicListener(this);

        seekBar.setOnSeekBarChangeListener(this);
        rightArrow.setOnClickListener(this);
        leftArrow.setOnClickListener(this);
        btnMusic.setOnClickListener(this);
        initLitterStarList();
    }

    /**
     * 初始化小星星列表
     */
    private void initLitterStarList() {
        litterStarList = new ArrayList<>();
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,0,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,0,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,5,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,5,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_LONG_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,3,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,3,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,2,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,2,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,1,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,1,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,0,LITTER_STAR_BREAK_LONG_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,3,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,3,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,2,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,2,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,1,LITTER_STAR_BREAK_LONG_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,3,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,3,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,2,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,2,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,1,LITTER_STAR_BREAK_LONG_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,0,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,0,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,5,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,5,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,4,LITTER_STAR_BREAK_LONG_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,3,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,3,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,2,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,2,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,1,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,1,LITTER_STAR_BREAK_SHORT_TIME));
        litterStarList.add(new AutoPlayEntity(Piano.PianoKeyType.WHITE,4,0,LITTER_STAR_BREAK_LONG_TIME));
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


    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (scrollProgress == 0) {
            try{
                scrollProgress = (pianoView.getLayoutWidth() * 100) / pianoView.getPianoWidth();
            }catch (Exception e){

            }
        }
        int progress;
        switch (view.getId()){
            case R.id.iv_left_arrow:
                if (scrollProgress == 0){
                    progress = 0;
                }else {
                    progress = seekBar.getProgress() - scrollProgress;
                    if (progress < 0){
                        progress = 0;
                    }
                }
                seekBar.setProgress(progress);
                break;
            case R.id.iv_right_arrow:
                if (scrollProgress == 0){
                    progress = 100;
                }else {
                    progress = seekBar.getProgress() + scrollProgress;
                    if (progress > 100){
                        progress = 100;
                    }
                }
                seekBar.setProgress(progress);
                break;
            case R.id.iv_music:
                seekBar.setProgress(50);
                if (!isPlay){
                    pianoView.autoPlay(litterStarList);
                }
                break;
        }
    }

    /**
     * Dp to px
     * @param dp dp值
     * @return px 值
     */
    private  float convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public void onPianoAutoPlayStart() {
        Toast.makeText(this,"onPianoAutoPlayStart",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPianoAutoPlayEnd() {
        Toast.makeText(this,"onPianoAutoPlayStart",Toast.LENGTH_SHORT).show();
        isPlay = false;
    }
}
