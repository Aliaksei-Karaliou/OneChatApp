package com.example.korol.onechatapp.logic.utils.asyncOperation;

import org.json.JSONException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AsyncOperation<Param, Result> {

    protected abstract Result doInBackground(Param param);

    public final Result execute(final Param param) throws ExecutionException, InterruptedException {
        final AssyncOperationCallableImplementation callableImplementation = new AssyncOperationCallableImplementation(param, new AssyncOperationInterface<Param, Result>() {
            @Override
            public Result doIt(Param parameter) throws JSONException {
                return doInBackground(param);
            }
        });
        final Future<Result> future = Executors.newCachedThreadPool().submit(callableImplementation);
        return future.get();
    }

    private class AssyncOperationCallableImplementation implements Callable<Result> {

        private Param param;

        public AssyncOperationCallableImplementation(Param param, final AssyncOperationInterface<Param, Result> operationInterface) {
            this.operationInterface = operationInterface;
            this.param = param;
        }

        AssyncOperationInterface<Param, Result> operationInterface;

        @Override
        public Result call() throws Exception {
            return operationInterface.doIt(param);
        }
    }

    private interface AssyncOperationInterface<P, Result> {
        Result doIt(P parameter) throws JSONException;
    }
}
