package com.xiaozhanxiang.simplegridview.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xiaozhanxiang.simplegridview.aidl.Book;
import com.xiaozhanxiang.simplegridview.aidl.BookManager;
import com.xiaozhanxiang.simplegridview.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author: dai
 * date:2019/6/22
 */
public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";

    //CopyOnWriteArrayList 支持并发读写
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mRemoteCallbackList = new RemoteCallbackList<>();

    private AtomicBoolean misDestory = new AtomicBoolean(false);

    private BookManager.Stub mStub = new BookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            // 这个方法是在Binder线程池中执行的，需要考虑线程安全问题

            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            // 这个方法是在Binder线程池中执行的，需要考虑线程安全问题
            mBookList.add(book);
        }

        @Override
        public void addBookOut(Book book) throws RemoteException {

        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mRemoteCallbackList.register(listener);
            int i = mRemoteCallbackList.beginBroadcast();
            Log.e(TAG, "registerListener:  listener size : " + i );
            mRemoteCallbackList.finishBroadcast();
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mRemoteCallbackList.unregister(listener);
            int i = mRemoteCallbackList.beginBroadcast();
            Log.e(TAG, "registerListener:  listener size : " + i );
            mRemoteCallbackList.finishBroadcast();
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    private void onNerBookArrived(Book book)  {
        mBookList.add(book);
        int N = mRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mRemoteCallbackList.getBroadcastItem(i);
            if (listener != null){
                try {
                    listener.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        mRemoteCallbackList.finishBroadcast();
        Log.d(TAG, "onNerBookArrived , notify listeners:  " );
    }





    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("android",1));
        mBookList.add(new Book("哈哈哈哈",2));
        new Thread(new ServiceWorker()).start();
    }


    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while (!misDestory.get()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int  bookId = mBookList.size() + 1;
                Book newBook = new Book("new book#" + bookId,bookId);
                onNerBookArrived(newBook);
            }
        }
    }


    @Override
    public void onDestroy() {
        misDestory.set(true);
        super.onDestroy();


    }
}
