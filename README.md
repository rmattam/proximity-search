# proximity-search

A scala app to perform proximity boolean search on an inverted positional index from a file containing a new line separated list of documents.
The first token in each line of the input file is interpreted as the user specified document id and is not searchable.

### Instructions

The project uses:
```
sbt.version = 1.2.8
scalaVersion := "2.12.8"
```

1. Clone this repository and run "sbt test" to view status of 11 tests created.

2. Run the sbt run Main command with the command line argument mentioning the file.txt that needs to be indexed.

```
sbt 'runMain search.Main documents/index.txt'
```

3. On seeing the message that the documents are indexed correctly, type your query after the prompt. 
If you want the query to be direction sensitive, add the "-d" flag after your query, for egs: term1 /k term2 -d.

```
Documents have been Indexed Correctly!
Enter your search query after the prompt.
Add the -d flag after your query string to enable directional search. egs: term1 /k term2 -d
Type exit/quit to stop
>> drug /4 schizophrenia
ArrayBuffer((Doc1,2,4), (Doc2,3,2), (Doc3,2,6))
>> drug /4 schizophrenia -d
ArrayBuffer((Doc1,2,4), (Doc3,2,6))
```

The output is an ArrayBuffer collection of Triples. The first element in the Triple is the Document ID that match the query executed.
The second element in the Triple is the positional index of the first term in the query and the last element in the Triple is the positional index of the second term in the query.

### General assumptions
1. The code assumes that the input documents to be added to the inverted index and the search query are easily tokenizable and normalized correctly.
The code uses a simple white space regex to extract all tokens from input documents and query string.
2. term1 /k term2 is the expected pattern for the query string. /k should be an integer. 
3. A -d flag can be specified at the end of the query string to enable directional proximity search. By default direction is ignored.

### Expected results

On indexing the file: documents/index.txt, The answer to the following queries describe the expected results by the algorithm used.

1. What does the code return for the file above and the query: schizophrenia /2 drug?

```
ArrayBuffer((Doc1,4,2), (Doc2,2,3))
```

2. What does the code return for the query: schizophrenia /4 drug?
```
ArrayBuffer((Doc1,4,2), (Doc2,2,3), (Doc3,6,2))
```

3. What does the code return for the directional proximity search query: schizophrenia /2 drug?

```
ArrayBuffer((Doc2,2,3))
```

### Testing

Run sbt test to run the test cases created.

```
sbt test
```

Have a look at the ProximitySpec class in the src/test/scala/ProximitySpec.scala file to see expected results for queries executed when the file documents/index.txt is used as the input set of documents.