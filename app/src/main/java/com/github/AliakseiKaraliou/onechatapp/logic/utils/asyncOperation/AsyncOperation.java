package com.github.aliakseiKaraliou.onechatapp.logic.utils.asyncOperation;

import android.support.annotation.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AsyncOperation<Param, Result> {

    private Future<Result> future;
    private static ExecutorService executor = Executors.newCachedThreadPool();

    protected abstract Result doInBackground(Param param);

    private class AssyncOperationCallableImplementation implements Callable<Result> {

        private Param param;

        public AssyncOperationCallableImplementation(Param param) {
            this.param = param;
        }

        @Override
        public Result call() throws Exception {
            return doInBackground(param);
        }
    }

    public final AsyncOperation<Param, Result> startLoading(final Param param) {
        final AssyncOperationCallableImplementation callableImplementation = new AssyncOperationCallableImplementation(param);
        this.future = executor.submit(callableImplementation);
        return this;
    }

    @Nullable
    public final Result getResult() {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public final boolean isLoadingFinished(){
        return future.isDone();
    }

}
