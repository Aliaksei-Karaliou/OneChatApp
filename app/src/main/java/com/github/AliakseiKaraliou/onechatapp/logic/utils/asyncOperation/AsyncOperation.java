package com.github.AliakseiKaraliou.onechatapp.logic.utils.asyncOperation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AsyncOperation<Param, Result> {

    private Future<Result> future;
    private static ExecutorService executor = Executors.newCachedThreadPool();

    protected abstract Result doInBackground(Param param);

    public final Result execute(final Param param) {
        startLoading(param);
        return getResult();
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

    public final void startLoading(final Param param){
        final AssyncOperationCallableImplementation callableImplementation = new AssyncOperationCallableImplementation(param);
        this.future = executor.submit(callableImplementation);
    }

    public final Result getResult() {
        try {
            return future.get();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
