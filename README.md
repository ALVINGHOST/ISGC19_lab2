# ISGC19_lab2
open Lab2 and run "Lab2Application.java"
this will start the backend so that the database and api are accessable

then open Lab2 CLI and start Main.java
now you can access the api using the CLI

to access the api using postman the link is :http://localhost:8080/ \n
o	Get all books
	GET /books
o	Get books by title
	GET /books/title/{title}
o	Filter books by category
	GET /books/category/{category}
The 3 categories are IT,MATH,PHYSICS. they are case sensitive.
o	Delete a book
	DELETE /books/id/{id}
o	Create a book
	POST /books: Create a new book
	Request Body: JSON with book details (except id)
Example JSON :
{
    "title": "Calculus Made Easy",
    "description": "Simplified approach to calculus",
    "publishedYear": "1910",
    "author": "Silvanus P. Thompson",
    "category": "MATH"
}
