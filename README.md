# counter-api
counter-api

This Counter-api application is developed using spring framework. It facilitates two REST services (GET & POST).

POST: searchtext

Description: It returns the count of the each input JSON string array searchtext 
Mapping: http://localhost:8080/counter-api/search/ 
Body: "String in JSON format" 
Authorisation: BASIC AUTHENTICATION 

GET: top/{value}

Description: It returns the Top strings in the descending order based on the input parameter passed through the URL. The returned result will be in text/csv
Mapping: http://localhost:8080/counter-api/top/{value}
Authorisation: BASIC AUTHENTICATION

This Spring framework application is created using maven. The dependencies required for this application were added in the pom.xml file. And the input is present in the src/main/resources folder

To run this application:

1. mvn clean install

2. Java -jar returncount.war

3. Sample curl commands

3a. curl http://localhost:8080/counter-api/search -H "Authorization: Basic b3B0dXM6Y2FuZGlkYXRlcw==" -d "{\"searchText\":[\"Duis\", \"Sed\", \"Donec\", \"Augue\", \"Pellentesque\", \"123\"]}" -H "Content-Type: application/json" -X POST

3b. curl http://host/counter-api/top/20 -H "Authorization: Basic b3B0dXM6Y2FuZGlkYXRlcw==" -H "Accept: text/csv"
