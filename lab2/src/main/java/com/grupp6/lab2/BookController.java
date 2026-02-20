package com.grupp6.lab2;

import java.lang.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {
    @Autowired
    private BookRepository bookRep;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(){
        try {
            List<Book> bookList = new ArrayList<>();
            bookRep.findAll().forEach(bookList::add);
            if (bookList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bookList,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/books/id/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        Optional<Book> bookObj = bookRep.findById(id);
        if (bookObj.isPresent()){
            return new ResponseEntity<>(bookObj.get(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/books/title/{title}")
    public ResponseEntity<List<Book>> getBookByName(@PathVariable String title){
        try {
            List<Book> bookList = new ArrayList<>();
            bookRep.findByTitleContaining(title).forEach(bookList::add);
            if (bookList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bookList,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/books/category/{category}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Category category){
        try {
            List<Book> bookList = new ArrayList<>();
            bookRep.findByCategory(category).forEach(bookList::add);
            if (bookList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bookList,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/books/id/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id){
        try {
            bookRep.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book newBook){
        try {
            if(newBook.getAuthor().isBlank()){
                throw new Exception("null auth");
            }
            if(newBook.getTitle().isBlank()){
                throw new Exception("null title");
            }
            if(newBook.getDescription().length()>500){
                throw new Exception("desc too long");
            }
            Book bookObj = bookRep.save(newBook);
            return new ResponseEntity<>(bookObj, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
