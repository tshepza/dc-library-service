# DigiCert  Library Service

This is the microservice application for the library. Written in java and H2 in memory db for testing purposes.
The application loads test data on start up.

List of available operations
* `Add a Book`
* `Upate Book`
* `Get book ByTitle`
* `Get book ByIsbn`
* `Delete`
* `Get All Books with filter and page nation` 

## Curl Commands Examples 
### Add/Create Book

`curl --request POST \
  --url http://localhost:8080/api/v1/books/ \
  --header 'Content-Type: application/json' \
  --data '{"isbn": "8989235","title":"Java 9","category": "FICTION","publisher": "Olerato","numberOfPages": 310,"author": "Alfred"}'
`
### Update Book

` 
curl --request PUT \
--url http://localhost:8080/api/v1/books/11 \
--header 'Content-Type: application/json' \
--data '{"isbn": "8989235","title":"Java11","category": "FICTION","publisher": "Olerato","numberOfPages": 310,"author": "Alfred"}'
`

### Get a Book by ISBN


`curl --request GET \
--url http://localhost:8080/api/v1/books/byIsbn/8989235 \
--header 'Content-Type: application/json' `

### Get a Book by Title

`
curl --request GET \
--url http://localhost:8080/api/v1/books/byTitle/Java%2011 \
--header 'Content-Type: application/json' 
`

### Delete Book


`curl --request DELETE \
--url http://localhost:8080/api/v1/books/11 
`
### Get All Books Filter by Author 

`curl --request GET \
--url 'http://localhost:8080/api/v1/books/?author=Mosa' \
--header 'Content-Type: application/json' 
`
### Get All Books Filter by Author and Offset/Limit

`curl --request GET \
--url 'http://localhost:8080/api/v1/books/?author=Mosa&limit=1&offset=0' \
--header 'Content-Type: application/json' `

### Get All Books Filter NO Filter Offset/Limit default

`
curl --request GET \
--url http://localhost:8080/api/v1/books/ \
--header 'Content-Type: application/json' 
`

