package com.pocoin.basemvp.domain;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by Administrator on 2016/10/11.
 */

public abstract class UseCase<Q extends UseCase.RequestValues> {

    private Scheduler executorThread;
    private Scheduler uiThread;

    protected Subscription subscription = Subscriptions.empty();

    public UseCase() {
        this(JobSchedule.getInstance().getScheduler(), AndroidSchedulers.mainThread());
    }

    public UseCase(Scheduler executorThread, Scheduler uiThread) {
        this.executorThread = executorThread;
        this.uiThread = uiThread;
    }

    public void execute(Subscriber UseCaseSubscriber , Q rv) {
        this.subscription = this.buildUseCaseObservable(rv)
                .subscribeOn(executorThread)
                .observeOn(uiThread)
                .subscribe(UseCaseSubscriber);
    }

    public void unSubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


    protected abstract Observable buildUseCaseObservable(Q rv);

    public interface RequestValues {
        boolean checkInput();
        int getErrorStringRes();
    }

    public interface RequestPageValue{
        void setPage(int page);
        void setLimit(int limit);
        int getPage();
        int getLimit();
    }

    public static class EmptyRequestValues implements RequestValues{

        public static final EmptyRequestValues INSTANCE = new EmptyRequestValues();

        private EmptyRequestValues(){}

        @Override
        public boolean checkInput() {
            return true;
        }
        @Override
        public int getErrorStringRes() {
            return 0;
        }
    }

}
