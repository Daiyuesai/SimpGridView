package com.xiaozhanxiang.simplegridview.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.aidl.Book;
import com.xiaozhanxiang.simplegridview.aidl.BookManager;
import com.xiaozhanxiang.simplegridview.aidl.IOnNewBookArrivedListener;
import com.xiaozhanxiang.simplegridview.bean.TestSerializable;
import com.xiaozhanxiang.simplegridview.callback.PermissionResultListener;
import com.xiaozhanxiang.simplegridview.service.BookManagerService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: dai
 * date:2019/6/20
 * 用于演示跨进程通信得类
 */
public class RemoteProcessActivity extends BaseActivity {
    private static final String TAG = "RemoteProcessActivity";
    @BindView(R.id.tv_serializable)
    TextView tvSerializable;
    @BindView(R.id.tv_in_serializable)
    TextView tvInSerializable;

    private BookManager mBookManager;



    public static void getInstance(Context context) {
        Intent intent = new Intent(context, RemoteProcessActivity.class);

        context.startActivity(intent);
    }

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_process);
        ButterKnife.bind(this);
        attachServices();
    }


    @OnClick({R.id.tv_serializable, R.id.tv_in_serializable, R.id.tv_get_book_list, R.id.tv_add_book})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_serializable:
                requestPermissionsd(permissions, 100, new PermissionResultListener() {
                    @Override
                    public void permissionResult(HashMap<String, Boolean> permission, boolean isAllAgree) {
                        serializable();
                    }
                });
                break;
            case R.id.tv_in_serializable:
                inSerializable();
                break;
            case R.id.tv_get_book_list:
                getBookList();
                break;
            case R.id.tv_add_book:
                addBook();
                break;
        }
    }

    private void addBook() {
        if (mBookManager != null && mBookManager.asBinder().isBinderAlive()) {
            Book book = new Book("安卓探索艺术",3);
            try {
                mBookManager.addBook(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void getBookList() {
        if (mBookManager != null) {
            try {
                //这个方法是耗时操作，这里是为了简单直接放在了主线程
                List<Book> bookList = mBookManager.getBookList();

                Log.e(TAG, "getBookList: " + bookList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 绑定远程服务
     */
    private void attachServices() {
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBookManager = BookManager.Stub.asInterface(service);
            try {
                service.linkToDeath(mDeathRecipient, 0);
                //耗时操作
                mBookManager.registerListener(mBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //mBookManager = null;
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            //监听Binder 断开
            if (mBookManager != null) {
                mBookManager.asBinder().unlinkToDeath(this,0);
                attachServices(); //重连
            }
        }
    };

    private IOnNewBookArrivedListener mBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            //这个方法是在Binder线程池中执行的，需要考虑线程安全问题 和 不能更新UI

            Log.e(TAG, "onNewBookArrived: " + newBook );
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBookManager != null && mBookManager.asBinder().isBinderAlive()) {
            try {
                //耗时操作
                mBookManager.unRegisterListener(mBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mConnection);
    }

    private void inSerializable() {
        File file = new File(getExternalCacheDir(), "test.txt");

        ObjectInputStream out = null;
        try {
            out = new ObjectInputStream(new FileInputStream(file));

            TestSerializable testSerializable = (TestSerializable) out.readObject();
            tvInSerializable.setText(testSerializable.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void serializable() {
        new Thread() {
            @Override
            public void run() {
                File file = new File(getExternalCacheDir(), "test.txt");

                Log.i(TAG, "run: " + file.getAbsolutePath());
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(new FileOutputStream(file));
                    TestSerializable testSerializable = new TestSerializable("ff", 1, "eode");
                    out.writeObject(testSerializable);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }


}
