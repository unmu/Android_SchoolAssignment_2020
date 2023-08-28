package com.example.musicbox2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	TextView title, author; // 显示歌曲标题、作者文本框
	ImageButton play, stop; // 播放/暂停、停止按钮
	Button last, next;      // 上一首、下一首按钮

	ActivityReceiver activityReceiver;

	public static final String CTL_ACTION =	"org.crazyit.action.CTL_ACTION";
	public static final String UPDATE_ACTION = "org.crazyit.action.UPDATE_ACTION";

	int status = 0x11;  // 定义音乐的播放状态，0x11代表没有播放；0x12代表正在播放；0x13代表暂停
	String[] titleStrs = new String[] { "となりのトトロ", "いつでも谁かが", "君をのせて", "风の谷のナウシカ"};
	String[] authorStrs = new String[] {"久石譲", "紅龍", "久石譲", "細野晴臣"};

	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 获取程序界面界面中的4个按钮
		play = (ImageButton) this.findViewById(R.id.play);
		stop = (ImageButton) this.findViewById(R.id.stop);

		last = (Button) this.findViewById(R.id.btnLast);
		next = (Button) this.findViewById(R.id.btnNext);

        // 2个文本框
		title = (TextView) findViewById(R.id.title);
		author = (TextView) findViewById(R.id.author);

		// 为两个按钮的单击事件添加监听器
		play.setOnClickListener(this);
		stop.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);

		activityReceiver = new ActivityReceiver();
		IntentFilter filter = new IntentFilter();   // 创建IntentFilter
		filter.addAction(UPDATE_ACTION);    // 指定BroadcastReceiver监听的Action
		registerReceiver(activityReceiver, filter); // 注册BroadcastReceiver

		Intent intent = new Intent(this, MusicService.class);
		startService(intent);   // 启动后台Service
	}

	// 自定义的BroadcastReceiver，负责监听从Service传回来的广播
	public class ActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) 	{
            int update = intent.getIntExtra("update", -1);  // 获取Intent中的update消息，update代表播放状态
            int current = intent.getIntExtra("current", -1);    // 获取Intent中的current消息，current代表当前正在播放的歌曲
            if (current >= 0) {
                title.setText(titleStrs[current]);
                author.setText(authorStrs[current]);
            }
            switch (update) {
                case 0x11:  // 未播放状态
                    play.setImageResource(R.drawable.play);
                    status = 0x11;
                    break;
                case 0x12:  // 控制系统进入播放状态
                    play.setImageResource(R.drawable.pause);
                    status = 0x12;
                    break;
                case 0x13:  // 控制系统进入暂停状态
                    play.setImageResource(R.drawable.play);
                    status = 0x13;
                    break;
            }
        }
}
	@Override
	public void onClick(View source)
	{
		// 创建Intent
		Intent intent = new Intent("org.crazyit.action.CTL_ACTION");
		switch (source.getId())
		{
			case R.id.play: // 按下播放/暂停按钮
				intent.putExtra("control", 1);
				break;
			case R.id.stop: // 按下停止按钮
				intent.putExtra("control", 2);
				break;
            case R.id.btnLast:  // 按下上一首按钮
                intent.putExtra("control", 3);
                break;
            case R.id.btnNext:  //按下下一首按钮
                intent.putExtra("control", 4);
                break;
		}
		sendBroadcast(intent);  // 发送广播，将被Service组件中的BroadcastReceiver接收到
	}
}
