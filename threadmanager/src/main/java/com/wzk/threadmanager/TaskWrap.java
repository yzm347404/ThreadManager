package com.wzk.threadmanager;

import java.util.concurrent.Callable;

/**
 * Created by 政魁 on 2019/2/28 19:23
 * E-Mail Address：wangzhengkui@yingzi.com
 */
class TaskWrap<V>{
    private Task task;
    private TaskCallable<V> taskCallable;
    private V result;
    ThreadPoolManager.OnResultListener<V> resultListener;
    public TaskWrap(Task<V> task) {
        this.task = task;
        taskCallable = new TaskCallable();
    }

    public void run() {
        result = (V) taskCallable.call();
        //执行完成，则将结果返回
        if (resultListener != null) {
            resultListener.onResult(result);
        }
    }

    public void setOnResultListener(ThreadPoolManager.OnResultListener<V> listener) {
        resultListener = listener;
    }

    class TaskCallable<T> implements Callable<T> {

        @Override
        public T call() {
            task.setState(TaskState.STATE_RUNING);
            T result = (T) task.runTask();
            task.setState(TaskState.STATE_DONE);
            return result;
        }
    }

    public V get() {
        return result;
    }

}
