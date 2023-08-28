package com.example.musicbox2;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {
	MyReceiver serviceReceiver;
	AssetManager am;
	String[] musics = new String[] { "totoro.mp3", "pclhz.mp3", "tkzc.mp3",	"naushika.mp3" };
	MediaPlayer mPlayer;

	int status = 0x11;	// 当前的状态，0x11代表没有播放；0x12代表正在播放；0x13代表暂停
	int current = 0;	// 记录当前正在播放的音乐

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		am = getAssets();

		serviceReceiver = new MyReceiver();	// 创建BroadcastReceiver
		IntentFilter filter = new IntentFilter();	// 创建IntentFilter
		filter.addAction(MainActivity.CTL_ACTION);
		registerReceiver(serviceReceiver, filter);

		mPlayer = new MediaPlayer();	// 创建MediaPlayer
		mPlayer.setOnCompletionListener(new OnCompletionListener() {	// 为MediaPlayer播放完成事件绑定监听器
			@Override
			public void onCompletion(MediaPlayer mp) {
				current++;//自动跳到下一首
				if (current >= musics.length) {
					current = 0;
				}

				Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);	//发送广播通知Activity更改文本框
				sendIntent.putExtra("current", current);
				sendBroadcast(sendIntent);	// 发送广播，将被Activity组件中的BroadcastReceiver接收到
				prepareAndPlay(musics[current]);	// 准备并播放音乐
			}
		});
	}
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, Intent intent) {
			int control = intent.getIntExtra("control", -1);
			switch (control) {
				case 1:	// 播放或暂停
					if (status == 0x11) {	// 原来处于没有播放状态
						prepareAndPlay(musics[current]);	// 准备并播放音乐
						status = 0x12;	// 播放
					}
					else if (status == 0x12) {// 原来处于播放状态
						mPlayer.pause();	// 暂停音乐
						status = 0x13; // 暂停
					}
					else if (status == 0x13) {// 原来处于暂停状态
						mPlayer.start();	// 播放音乐
						status = 0x12; // 播放
					}
					break;
				case 2:	// 停止声音
					if (status == 0x12 || status == 0x13) {	// 原来正在播放或暂停
						mPlayer.stop();	// 停止播放
						status = 0x11;	// 无播放
					}
					break;
				case 3:	// 上一首
					if (status == 0x12 || status == 0x13) {	// 原来正在播放或暂停
						mPlayer.stop();// 停止播放
					}
					current = (current+musics.length-1)%musics.length;	// current后移
					prepareAndPlay(musics[current]);	// 准备并播放音乐
					status = 0x12;
					break;
				case 4:	// 下一首
					if (status == 0x12 || status == 0x13) {
						mPlayer.stop();
					}
					current = (current+1)%musics.length;	// current前移
					prepareAndPlay(musics[current]);
					status = 0x12;
					break;
			}
			Toast.makeText(context, "music " + (current+1), Toast.LENGTH_SHORT).show();
			// 广播通知Activity更改图标、文本框
			Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
			sendIntent.putExtra("update", status);
			sendIntent.putExtra("current", current);
			// 发送广播，将被Activity组件中的BroadcastReceiver接收到
			sendBroadcast(sendIntent);
		}
}
	private void prepareAndPlay(String music) {
		try {

			AssetFileDescriptor afd = am.openFd(music);// 打开指定音乐文件
			mPlayer.reset();
			mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());// 使用MediaPlayer加载指定的声音文件。
			mPlayer.prepare();// 准备声音
			mPlayer.start();// 播放
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}