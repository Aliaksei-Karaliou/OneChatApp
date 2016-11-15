package com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AsyncOperation<Param, Result> {

    protected abstract Result doInBackground(Param param);

    public final Result execute(final Param param) throws ExecutionException, InterruptedException {
        final AssyncOperationCallableImplementation callableImplementation = new AssyncOperationCallableImplementation(param);
        final Future<Result> future = Executors.newCachedThreadPool().submit(callableImplementation);
        return future.get();
    }

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
}
