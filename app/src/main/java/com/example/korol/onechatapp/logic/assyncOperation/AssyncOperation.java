package com.example.korol.onechatapp.logic.assyncOperation;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public abstract class AssyncOperation<Param, Result> {

    private Param param;

    protected abstract Result doInBackground(Param param);

    public final Result execute(Param param) throws Exception {
        this.param = param;
        final AssyncOperationCallableImplementation callableImplementation = new AssyncOperationCallableImplementation(param, new AssyncOperationInterface<Param, Result>() {
            @Override
            public Result doIt(Param parameter) {
                return doInBackground(parameter);
            }
        });
        final FutureTask<Result> futureTask = new FutureTask(callableImplementation);
        return futureTask.get();
    }

    private class AssyncOperationCallableImplementation implements Callable<Result> {

        public AssyncOperationCallableImplementation(Param param, AssyncOperationInterface<Param, Result> operationInterface) {
            this.param = param;
            this.operationInterface = operationInterface;
        }

        AssyncOperationInterface<Param, Result> operationInterface;
        Param param;

        @Override
        public Result call() throws Exception {
            return operationInterface.doIt(param);
        }
    }

    private interface AssyncOperationInterface<Param, Result> {
        Result doIt(Param parameter);
    }
}
