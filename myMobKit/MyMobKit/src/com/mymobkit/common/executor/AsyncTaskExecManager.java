package com.mymobkit.common.executor;

import com.mymobkit.common.PlatformSupportManager;


public final class AsyncTaskExecManager extends PlatformSupportManager<AsyncTaskExecInterface> {

  public AsyncTaskExecManager() {
    super(AsyncTaskExecInterface.class, new DefaultAsyncTaskExecInterface());
    addImplementationClass(11, "com.mymobkit.common.executor.HoneycombAsyncTaskExecInterface");
  }
}
