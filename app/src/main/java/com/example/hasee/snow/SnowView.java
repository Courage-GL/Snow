package com.example.hasee.snow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;


public class SnowView extends View {
	int MAX_SNOW_COUNT = 30;//这个是当前屏幕的雪花最多的数量  最好不要超过20，因为效果不太好
	Bitmap bitmap_snows = null;//雪花图片
	private final Paint mPaint = new Paint();
	private static final Random random = new Random();//这个类可以随机生成一个数以内的数(包括负数)
	private Snow[] snows = new Snow[MAX_SNOW_COUNT];//雪花的数组
	int view_height = 0;//当前父容器的高
	int view_width = 0;//当前父容器的宽
	int MAX_SPEED = 15;//雪花的最大下落速度

	public SnowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SnowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void LoadSnowImage() {
		Resources r = this.getContext().getResources();
		//得到图片
		bitmap_snows = ((BitmapDrawable) r.getDrawable(R.mipmap.ic_launcher))
				.getBitmap();
	}
	public void SetView(WindowManager window) {//将父容器的宽高传过来
		LoadSnowImage();
		DisplayMetrics dm = new DisplayMetrics();
		window.getDefaultDisplay().getMetrics(dm);
		view_height = dm.heightPixels - 100;
		view_width = dm.widthPixels - 50;
		addRandomSnow();
		startSnow();
	}

	public void addRandomSnow() {//添加雪花
		for(int i =0; i< MAX_SNOW_COUNT;i++){
			//生成雪花对象  x是在父容器的宽以内取一个随机数，y为0，随机的速度
			snows[i] = new Snow(random.nextInt(view_width), random.nextInt(view_height),random.nextInt(MAX_SPEED));
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//循环遍历
		for (int i = 0; i < MAX_SNOW_COUNT; i ++) {
			//那个雪花的宽超出了屏幕或者y超出了屏幕 就将把宽高重新设定
			if (snows[i].coordinate.x >= view_width || snows[i].coordinate.y >= view_height) {
				snows[i].coordinate.y = 0;
				snows[i].coordinate.x = random.nextInt(view_width);
				snows[i].speed = Math.abs(random.nextInt(MAX_SPEED));
				if (snows[i].speed<1){
					snows[i].speed = 1;
				}
			}
			// 设置雪花的下落位置的原理为：当前的y+下落速度就是下次雪花的位置
			snows[i].coordinate.y += snows[i].speed;

			//雪花飘动的效果
			// 随机产生一个数字，让雪花有水平移动的效果   喜欢的可以打开
//			int tmp = MAX_SPEED/2 - random.nextInt(MAX_SPEED);
			//将速度和随机生成的数对比   如果随机生成的数大就会取自己的速度加上当前的x，如果随机生成的数小就为x+=tmp
			//主要为了让雪花可以所有飘动  所以才会使用Random    因为它可能会生成负数   随意x可能会往左移动，也能往右移动
//			snows[i].coordinate.x += snows[i].speed < tmp ? snows[i].speed : tmp;
			canvas.drawBitmap(bitmap_snows, ((float) snows[i].coordinate.x),
					((float) snows[i].coordinate.y), mPaint);
		}


	}
	public void startSnow(){
		mRefreshSnowHandler.mySleep(600);
	}

	public void stop(){
		mRefreshSnowHandler.stop();
		mRefreshSnowHandler.removeCallbacksAndMessages(null);

		bitmap_snows.recycle();
		bitmap_snows = null;
	}

	/*
   * 负责做界面更新工作 ，实现下雪
   */
	private RefreshSnowHandler mRefreshSnowHandler = new RefreshSnowHandler();

	class RefreshSnowHandler extends Handler {
		//接收消息刷新UI再次调用sleep方法并且延迟100毫秒
		@Override
		public void handleMessage(Message msg) {
			//snow.addRandomSnow();
			if (msg.what == 0){
				invalidate();
				mySleep(80);//此处的最好不要超过150毫秒
			}else  if (msg.what == 1){
				removeMessages(1);
			}
		}
		/**
		 *  不断发送消息
		 *  @param delayMillis
		 */
		public void mySleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
		/**
		 * 停止发送
		 */
		public  void stop(){
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(1), 0);
		}
	}


}
