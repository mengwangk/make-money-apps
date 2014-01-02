package com.mymobkit.common.executor;

import android.annotation.TargetApi;
import android.os.AsyncTask;

/**
 * On Honeycomb and later, {@link AsyncTask} returns to serial execution by default which is undesirable.
 * This calls Honeycomb-only APIs to request parallel execution.
 */
@TargetApi(11)
public final class HoneycombAsyncTaskExecInterface implements AsyncTaskExecInterface {

  @Override
  public <T> void execute(AsyncTask<T,?,?> task, T... args) {
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
  }

}
