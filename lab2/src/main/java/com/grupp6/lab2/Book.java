package com.grupp6.lab2;

import java.lang.*;
import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Book")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CATEGORY", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PUBLISHED_YEAR")
    private String publishedYear;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPublishedYear() { return publishedYear; }
    public void setPublishedYear(String publishedYear) { this.publishedYear = publishedYear; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}

