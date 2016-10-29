package com.example.korol.onechatapp.logic.utils.asyncOperation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AsyncOperation<Param, Result> {

    protected abstract Result doInBackground(Param param);

    public final Result execute(Param param) throws ExecutionException, InterruptedException {
        final AssyncOperationCallableImplementation callableImplementation = new AssyncOperationCallableImplementation(param, new AssyncOperationInterface<Param, Result>() {
            @Override
            public Result doIt(Param parameter) {
                return doInBackground(parameter);
            }
        });
        final Future<Result> future = Executors.newCachedThreadPool().submit(callableImplementation);
        return future.get();
    }

    private class AssyncOperationCallableImplementation implements Callable<Result> {

        public AssyncOperationCallableImplementation(final Param param, final AssyncOperationInterface<Param, Result> operationInterface) {
            this.param = param;
            this.operationInterface = operationInterface;
        }

        AssyncOperationInterface<Param, Result> operationInterface;
        final Param param;

        @Override
        public Result call() throws Exception {
            return operationInterface.doIt(param);
        }
    }

    private interface AssyncOperationInterface<P, Result> {
        Result doIt(P parameter);
    }
}
