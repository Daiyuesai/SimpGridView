// BookManager.aidl
package com.xiaozhanxiang.simplegridview.aidl;

// Declare any non-default types here with import statements
import com.xiaozhanxiang.simplegridview.aidl.Book;
import com.xiaozhanxiang.simplegridview.aidl.IOnNewBookArrivedListener;
interface BookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void addBookOut(out Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unRegisterListener(IOnNewBookArrivedListener listener);
}
