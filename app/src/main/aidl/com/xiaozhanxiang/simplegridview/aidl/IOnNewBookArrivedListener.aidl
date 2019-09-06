// IOnNewBookArrivedListener.aidl
package com.xiaozhanxiang.simplegridview.aidl;

// Declare any non-default types here with import statements
import com.xiaozhanxiang.simplegridview.aidl.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook );
}
