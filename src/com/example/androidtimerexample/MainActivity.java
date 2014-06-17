package com.example.androidtimerexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private Button mStartButton;
		private Button mPauseButton;

		private TextView mTimerValue;

		private long mStartTime = 0L;

		private Handler mCustomHandler = new Handler();

		long mTimeInMilliseconds = 0L;
		long mTimeSwapBuff = 0L;
		long mUpdatedTime = 0L;
		protected Runnable mUpdateTimerThread = new Runnable() {
			
			@Override
			public void run() {
				mTimeInMilliseconds = SystemClock.uptimeMillis() - mStartTime;
				
				mUpdatedTime = mTimeSwapBuff + mTimeInMilliseconds;
				
				int secs = (int) (mUpdatedTime / 1000);
				int mins = secs / 60;
				secs = secs % 60;
				int milliseconds = (int) (mUpdatedTime % 1000);
				mTimerValue.setText(mins + ":"
					+ String.format("%02d", secs) + ":"
					+ String.format("%03d", milliseconds));
				mCustomHandler.postDelayed(this, 0);
				
			}
		};

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			mTimerValue = (TextView) rootView.findViewById(R.id.timerValue);
			
			mStartButton = (Button) rootView.findViewById(R.id.startButton);
			mPauseButton = (Button) rootView.findViewById(R.id.pauseButton);
			
			mStartButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mStartTime = SystemClock.uptimeMillis();
					mCustomHandler.postDelayed(mUpdateTimerThread, 0);
				}
			});
			
			mPauseButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mTimeSwapBuff += mTimeInMilliseconds;
					mCustomHandler.removeCallbacks(mUpdateTimerThread);
				}
			});

			return rootView;
		}

	}

}
