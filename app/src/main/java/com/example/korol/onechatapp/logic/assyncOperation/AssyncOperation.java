package com.example.korol.onechatapp.logic.assyncOperation;

import java.util.concurrent.*;

public abstract class AssyncOperation<Param, Result> {

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
