// IBookManager.aidl
package com.humanheima.ibinderdemo.aidl;
import  com.humanheima.ibinderdemo.aidl.Book;
// Declare any non-default types here with import statements

interface IBookManager {
 List<Book> getBookList();
          void addBook(in Book book);
}
