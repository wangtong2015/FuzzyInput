package com.example.wangtong.fuzzyinput;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public int[] voice=new int[26];
    public EditText Name_Input;
    public Button Begin=null;
    public TextView showXY=null;
    public TextView charView=null;
    public LinearLayout main_layout = null;
    public String filename=null;
    public String[] character = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    public int TestNum=26;
    public boolean ActivityBegin=false;
    public int turn=0;
    public ImageView keyboard;
    MediaPlayer player;
    MediaPlayer blank;
    public String DataToSave=null;
    public ArrayList<Integer> testList=new ArrayList<>();
    private View.OnClickListener BeginOnClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String inputName=Name_Input.getText().toString();
            if (inputName.isEmpty()){
                Name_Input.requestFocus();
                Name_Input.setError("请输入姓名");
            }
            else{//开始检测
                //输入框设置
                filename=inputName+getTime();
                DataToSave=filename+"\n";
                showXY.setText(filename);
                Name_Input.setText(null);//清空输入框
                Name_Input.setFocusable(false);//设置不可编辑状态
                Name_Input.setFocusableInTouchMode(false);
                InputMethodManager systemKeyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (systemKeyboard != null) {
                    systemKeyboard.hideSoftInputFromWindow(Name_Input.getWindowToken(), 0);
                }
                //键盘设置
                keyboard.requestFocus();
                //测试程序参数设置
                ActivityBegin=true;
                turn=0;//重置状态
                play(testList.get(turn));
                charView.setText(character[testList.get(turn)]);
            }
        }
    };
    private View.OnTouchListener KeyboardOnTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub

            if (ActivityBegin && turn < TestNum) {
                Float x;
                Float y;
                String showData;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        showData = character[testList.get(turn)] + ":(" + x.toString() + "," + y.toString() + ")";
                        DataToSave += showData + "\n";
                        showXY.setText(showData);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = event.getX();
                        y = event.getY();
                        showData = character[testList.get(turn)] + ":(" + x.toString() + "," + y.toString() + ")";
                        DataToSave += showData + "\n";
                        showXY.setText(showData);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        x = event.getX();
                        y = event.getY();
                        showData = character[testList.get(turn)] + ":(" + x.toString() + "," + y.toString() + ")";
                        DataToSave += showData + "\n";
                        showXY.setText(showData);
                        turn++;
                        if(turn>=TestNum){
                            turn = 0;
                            ActivityBegin = false;
                            save();
                            //恢复编辑框状态
                            Name_Input.setFocusableInTouchMode(true);
                            Name_Input.setFocusable(true);

                        }else{
                            play(testList.get(turn));
                            charView.setText(character[testList.get(turn)]);
                        }
                    default:
                }
            }
            return true;
        }

    };

    public void save(){
        if(isExternalStorageWritable()){
            //String directory="/storage/emulated/0/Android/data/FuzzyInput/";
            //File sdCard = Environment.getExternalStorageDirectory();
            File DirectoryFolder = this.getExternalFilesDir(null);
            if(!DirectoryFolder.exists())
            { //如果该文件夹不存在，则进行创建
                DirectoryFolder.mkdirs();//创建文件夹
            }
            File file = new File(DirectoryFolder, filename+".txt");
            if(!file.exists()){
                try {
                    file.createNewFile() ;
                    //file is create
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fos=new FileOutputStream(file);
                fos.write(DataToSave.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            showXY.setText("测试完成！");

        }else{
            showXY.setText("不能存储文件!");
        }
    }
    public String getTime()
    {
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date d1=new Date(time);
        return format.format(d1);
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
    public void imageClick(View view) {
        //Implement image click function
    }
    //todo reconstruct
    public void voideInit() {
        voice[ 0] = R.raw.ios11_50_a;
        voice[ 1] = R.raw.ios11_50_b;
        voice[ 2] = R.raw.ios11_50_c;
        voice[ 3] = R.raw.ios11_50_d;
        voice[ 4] = R.raw.ios11_50_e;
        voice[ 5] = R.raw.ios11_50_f;
        voice[ 6] = R.raw.ios11_50_g;
        voice[ 7] = R.raw.ios11_50_h;
        voice[ 8] = R.raw.ios11_50_i;
        voice[ 9] = R.raw.ios11_50_j;
        voice[10] = R.raw.ios11_50_k;
        voice[11] = R.raw.ios11_50_l;
        voice[12] = R.raw.ios11_50_m;
        voice[13] = R.raw.ios11_50_n;
        voice[14] = R.raw.ios11_50_o;
        voice[15] = R.raw.ios11_50_p;
        voice[16] = R.raw.ios11_50_q;
        voice[17] = R.raw.ios11_50_r;
        voice[18] = R.raw.ios11_50_s;
        voice[19] = R.raw.ios11_50_t;
        voice[20] = R.raw.ios11_50_u;
        voice[21] = R.raw.ios11_50_v;
        voice[22] = R.raw.ios11_50_w;
        voice[23] = R.raw.ios11_50_x;
        voice[24] = R.raw.ios11_50_y;
        voice[25] = R.raw.ios11_50_z;

    };
    public void play(int index){
        blank=MediaPlayer.create(this,R.raw.blank);
        blank.start();
        player=MediaPlayer.create(this,voice[index]);
        blank.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                player.start();
            }
        });
    };
    public void testListInit(){
        for (int i=0;i<TestNum;i++) {
            testList.add(i);
        }
        Collections.shuffle(testList);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name_Input=(EditText)findViewById(R.id.Name_Input);
        Begin=(Button)findViewById(R.id.Begin_Test);
        Begin=(Button)findViewById(R.id.Begin_Test);
        showXY=findViewById(R.id.showXY);
        charView=findViewById(R.id.character);
        main_layout=(LinearLayout)findViewById(R.id.Main_View);
        keyboard = (ImageView)findViewById(R.id.keyboard);
        testListInit();
        voideInit();
        Begin.setOnClickListener(BeginOnClickListener);

        keyboard.setOnTouchListener(KeyboardOnTouchListener);
        //showXY.setText(String.valueOf(this.getExternalFilesDir(null)));
        Name_Input.setFocusableInTouchMode(true);
        Name_Input.setFocusable(true);

    }
}
