package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class Main {
    public static void main(String[] args) {
        String inp;
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Retrieve all books\n" +
                "2. Retrieve a specific book by id\n" +
                "3. Retrieve books by title \n" +
                "4. Retrieve books by category\n" +
                "5. Create a new book\n" +
                "6. Delete a book by id\n" +
                "7. Exit\n" +
                "Select an option: ___\n");
        while (true){
            boolean catTrue = true;
            inp = sc.nextLine();
            switch (inp){
                case "1":
                    getAllBooks();
                    break;
                case "2":
                    System.out.println("Enter the id of the book you want to look up:");
                    inp = sc.nextLine();
                    getBookById(inp);
                    break;
                case "3":
                    System.out.println("Enter a title to search for:");
                    getBookByTitle(sc.nextLine());
                    break;
                case "4":
                        System.out.println("Choose a category:\n" +
                                "1. IT\n" +
                                "2. Math\n" +
                                "3. Physics");
                        inp = sc.nextLine();
                        switch (inp) {
                            case "1":
                                getBookByCategoty("IT");
                                break;
                            case "2":
                                getBookByCategoty("MATH");
                                break;
                            case "3":
                                getBookByCategoty("PHYSICS");
                                break;
                            default:
                                System.out.println("Please choose a number from 1-3;");
                                break;
                        }
                    break;
                case "5":
                    String cat="";
                    System.out.println("Title: ");
                    String title = sc.nextLine();
                    if(title.isBlank()){
                     System.out.println("Title cannot be empty");
                     break;
                    }
                    System.out.println("Description: ");
                    String desc = sc.nextLine();
                    if (desc.length() > 500){
                        System.out.println("Description cannot be longer than 500 characters");
                        break;
                    }
                    System.out.println("Published Year: ");
                    String year = sc.nextLine();
                    System.out.println("Author: ");
                    String auth = sc.nextLine();
                    if(auth.isBlank()){
                        System.out.println("Author cannot be empty");
                        break;
                    }
                    boolean pubCatTrue = true;
                    while(pubCatTrue){
                        System.out.println("Choose a category:\n" +
                                "1. IT\n" +
                                "2. Math\n" +
                                "3. Physics");
                        inp = sc.nextLine();
                        switch (inp){
                            case "1":
                                cat="IT";
                                pubCatTrue = false;
                                break;
                            case "2":
                                cat="MATH";
                                pubCatTrue = false;
                                break;
                            case "3":
                                cat="PHYSICS";
                                pubCatTrue = false;
                                break;
                            default:
                                System.out.println("Please choose a number from 1-3;");
                                break;
                        }
                    }
                    addBook(new Book(title,desc,year,auth,cat));
                    break;
                case "6":
                    System.out.println("Enter the id of the book you want to delete:");
                    deleteBookById(sc.nextLine());
                    break;
                case "7":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Error in input. Please enter a number between 1-7");
                    break;
            }
            System.out.println("=========================");
            System.out.println("1. Retrieve all books\n" +
                    "2. Retrieve a specific book by id\n" +
                    "3. Retrieve books by title \n" +
                    "4. Retrieve books by category\n" +
                    "5. Create a new book\n" +
                    "6. Delete a book by id\n" +
                    "7. Exit\n" +
                    "Select an option: ___\n");
        }
    }
    public static void addBook(Book newBook) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonInString = objectMapper.writeValueAsString(newBook);
            System.out.println(jsonInString);
            String postUrl = "http://localhost:8080/books";
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(postUrl);
            httpPost.setHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(jsonInString,"UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void getAllBooks() {
        System.out.println("====== All Books ========");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = new URL("http://localhost:8080/books");
            List<Record> records = objectMapper.readValue(url,
                    new TypeReference<List<Record>>() {});
                for (Record record : records) {
                    System.out.println("ID: " + record.getId() +
                            ", Title: " + record.getTitle()+
                            ", Description: " + record.getDescription()+
                            ", Published Year: " + record.getPubYear()+
                            ", Author: " + record.getAuthor()+
                            ", Category: " + record.getCategory());
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void getBookById(String id) {
        System.out.println("====== Book by id ========");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = new URL("http://localhost:8080/books/id/"+id);
            JsonNode jsonNode = objectMapper.readTree(url);
            System.out.println(jsonNode);
            String sid = jsonNode.get("id").asText();
            String title = jsonNode.get("title").asText();
            String desc = jsonNode.get("description").asText();
            String auth = jsonNode.get("publishedYear").asText();
            String cat = jsonNode.get("category").asText();

            System.out.println("Id: " + sid);
            System.out.println("Title: " + title);
            System.out.println("Description: " + desc);
            System.out.println("Author: " + auth);
            System.out.println("Category: " + cat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteBookById(String id){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String deleteUrl = "http://localhost:8080/books/id/"+id;
            HttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpDelete = new HttpDelete(deleteUrl);
            httpDelete.setHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpDelete);
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void getBookByCategoty(String cat) {
        System.out.println("====== Books by Category ========");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = new URL("http://localhost:8080/books/category/"+cat);
            List<Record> records = objectMapper.readValue(url,
                    new TypeReference<List<Record>>() {});
            for (Record record : records) {
                System.out.println("ID: " + record.getId() +
                        ", Title: " + record.getTitle()+
                        ", Description: " + record.getDescription()+
                        ", Published Year: " + record.getPubYear()+
                        ", Author: " + record.getAuthor()+
                        ", Category: " + record.getCategory());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void getBookByTitle(String title) {
        System.out.println("====== Books by Title ========");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            URL url = new URL("http://localhost:8080/books/title/" + encodedTitle);
            List<Record> records = objectMapper.readValue(url,
                    new TypeReference<List<Record>>() {});
            for (Record record : records) {
                System.out.println("ID: " + record.getId() +
                        ", Title: " + record.getTitle()+
                        ", Description: " + record.getDescription()+
                        ", Published Year: " + record.getPubYear()+
                        ", Author: " + record.getAuthor()+
                        ", Category: " + record.getCategory());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Book {
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("publishedYear")
    private String pubYear;
    @JsonProperty("author")
    private String author;
    @JsonProperty("category")
    private String category;
        public Book() {}
        public Book(String title, String description,String pubYear,String author,String category) {
            setTitle(title);
            setDescription(description);
            setPubYear(pubYear);
            setAuthor(author);
            setCategory(category);
        }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPubYear() {
        return pubYear;
    }
    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

}

class Record{
    @JsonProperty("id")
    private int Id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("publishedYear")
    private String pubYear;
    @JsonProperty("author")
    private String author;
    @JsonProperty("category")
    private String category;
    public Record(){}
    public Record(String title, String description,String pubYear,String author,String category){
        setTitle(title);
        setDescription(description);
        setPubYear(pubYear);
        setAuthor(author);
        setCategory(category);
    }
    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPubYear() {
        return pubYear;
    }
    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}