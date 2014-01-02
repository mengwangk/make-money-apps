package com.mymobkit.common.executor;

import android.os.AsyncTask;

/**
 * Before Honeycomb, {@link AsyncTask} uses parallel execution by default, which is desired. Good thing
 * too since there is no API to request otherwise.
 */
public final class DefaultAsyncTaskExecInterface implements AsyncTaskExecInterface {

  @Override
  public <T> void execute(AsyncTask<T,?,?> task, T... args) {
    task.execute(args);
  }

}
