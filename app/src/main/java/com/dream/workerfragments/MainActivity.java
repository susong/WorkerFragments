package com.dream.workerfragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Author:      SuSong
 * Email:       751971697@qq.com | susong0618@163.com
 * GitHub:      https://github.com/susong0618
 * Date:        16/4/1 下午3:58
 * Description: MainActivity displays the screen's UI and starts a TaskFragment which will
 * execute an asynchronous task and will retain itself when configuration
 * changes occur.
 */
public class MainActivity extends AppCompatActivity implements TaskFragment.TaskCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true; // Set this to false to disable logs.

    private static final String KEY_CURRENT_PROGRESS = "current_progress";
    private static final String KEY_PERCENT_PROGRESS = "percent_progress";
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private TaskFragment mTaskFragment;
    private ProgressBar mProgressBar;
    private TextView mPercent;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) XLog.i(TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Initialize views.
        mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        mPercent = (TextView) findViewById(R.id.percent_progress);
        mButton = (Button) findViewById(R.id.task_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTaskFragment.isRunning()) {
                    mTaskFragment.cancel();
                } else {
                    mTaskFragment.start();
                }
            }
        });

        // Restore saved state.
        if (savedInstanceState != null) {
            mProgressBar.setProgress(savedInstanceState.getInt(KEY_CURRENT_PROGRESS));
            mPercent.setText(savedInstanceState.getString(KEY_PERCENT_PROGRESS));
        }

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is being retained
        // over a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        if (mTaskFragment.isRunning()) {
            mButton.setText(getString(R.string.cancel));
        } else {
            mButton.setText(getString(R.string.start));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (DEBUG) XLog.i(TAG, "onSaveInstanceState(Bundle)");
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_PROGRESS, mProgressBar.getProgress());
        outState.putString(KEY_PERCENT_PROGRESS, mPercent.getText().toString());
    }

    /*********************************/
    /***** TASK CALLBACK METHODS *****/
    /*********************************/

    @Override
    public void onPreExecute() {
        if (DEBUG) XLog.i(TAG, "onPreExecute()");
        mButton.setText(getString(R.string.cancel));
        Toast.makeText(this, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(int percent) {
        if (DEBUG) XLog.i(TAG, "onProgressUpdate(" + percent + "%)");
        mProgressBar.setProgress(percent * mProgressBar.getMax() / 100);
        mPercent.setText(percent + "%");
    }

    @Override
    public void onCancelled() {
        if (DEBUG) XLog.i(TAG, "onCancelled()");
        mButton.setText(getString(R.string.start));
        mProgressBar.setProgress(0);
        mPercent.setText(getString(R.string.zero_percent));
        Toast.makeText(this, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute() {
        if (DEBUG) XLog.i(TAG, "onPostExecute()");
        mButton.setText(getString(R.string.start));
        mProgressBar.setProgress(mProgressBar.getMax());
        mPercent.setText(getString(R.string.one_hundred_percent));
        Toast.makeText(this, R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    }

    /************************/
    /***** OPTIONS MENU *****/
    /************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_trigger_config_change:
                // Simulate a configuration change. Only available on
                // Honeycomb and above.
                recreate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /************************/
    /***** LOGS & STUFF *****/
    /************************/

    @Override
    protected void onStart() {
        if (DEBUG) XLog.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (DEBUG) XLog.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (DEBUG) XLog.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (DEBUG) XLog.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (DEBUG) XLog.i(TAG, "onDestroy()");
        super.onDestroy();
    }

}
