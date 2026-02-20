package com.grupp6.lab2;

import java.lang.*;
import java.util.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Iterable<Book> findByTitleContaining(String title);
    Iterable<Book> findByCategory(Category category);
}
