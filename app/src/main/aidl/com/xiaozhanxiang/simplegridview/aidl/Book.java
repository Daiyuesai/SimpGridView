package com.xiaozhanxiang.simplegridview.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: dai
 * date:2019/6/22
 */
public class Book implements Parcelable {
    private String bookName;
    private int bookId;


    public Book(String bookName, int bookId) {
        this.bookName = bookName;
        this.bookId = bookId;
    }

    public Book() {
    }

    protected Book(Parcel in) {
        bookName = in.readString();
        bookId = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookName);
        dest.writeInt(bookId);
    }

    /**
     * https://www.cnblogs.com/xgjblog/p/10980939.html
     * 这个方法是 使用 out 定向tag是需要用到
     * 参数是一个Parcel,用它来存储与传输数据
     * @param dest
     */
    public void readFromParcel(Parcel dest) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        bookName = dest.readString();
        bookId = dest.readInt();
    }


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", bookId=" + bookId +
                '}';
    }
}
