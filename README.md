Mongo DB -Scaling out and more doesnt consume more memory

Mongo DB starts a mongod process and standalone mongo shell(has js shell language embedded into it) program which communicates to mongo db using tcp ip

We are building web app using two framework
	*)spark framework is a micro web framework inspired by sinatra --> has embedded jetty server
		*)spark.get(new Route(/) method name) is marked for that route
	*)freearker for web desiginging template(MVC framework)
		*)
	*)Mongo java driver similar to hibernate driver for connecting with mongo db
	
Location of Mongo DB - C:\Program Files\MongoDB\Server\3.4\bin

Steps after installing mongo db
	C:\Users\Praka$h>md \data

	C:\Users\Praka$h>md \data\db

	C:\Users\Praka$h>mongod    --> this command is used to start the server
	
	*)ERROR in case of dll missing download vc++ to overcome this error
	
	*) start mongod in one cmd prompt --> now ur server is started 
	*) start mongo in other cmd prompt -> mow ur cmd is connected to server via local host using local port 2701
	
	*)Sample test commands for inserting and retreiving 
		MongoDB Enterprise > db.names.insert({'name':'prakash'});
		WriteResult({ "nInserted" : 1 })
		MongoDB Enterprise > db.names.find()
		{ "_id" : ObjectId("587b9686ab3165ef91792e3c"), "name" : "prakash" }
		MongoDB Enterprise >
	*)Mongo d needs be running for doing crud operations , once u hit ctrl+c then our crud will not work from the window where mongo was started
	
	JSON- a refresher
	-----
	*) data types supported by JSON are object ,arrays,string, number 
	
	BSON - Binary Json
	----
	*)Mongo db stores the data in BSON
	
	*)Mongo java driver acts as buffer for converting BSON to JSON and vice versa
	
	Mongo DB commands
	-----------------
	use dbname --> creates or go into that db 
	show dbs --> used for showing all the databases created
	show collections --> is used to show all the collections present
	db.somename.insertOne({json_values}) --> used for inserting values into db
	db.somename.find() --> used for selecting all the values from db .pretty() is used for structured format
	db.somename.find({'specify json condition'}) for example --> db.employees.find({'name':'Prakash'}).pretty() retreives only name with prakash
	
	you can also assign it to var as it is a Javascript
	var a = db.somename.find()
	
	supported methods are 'hasNext' and 'next'
	
	_id is used as primary ke if omitted then mongo db is gonna auto generate it ,it needs to be uniques as it is as primary key

	
	Homework 
	---------
	1st week show database used to show all the database 
	use database name to navigate and 
-------------------------------------------------------------------------------------------------------------------------------
Mongo DB - week 2
CRUD
------
Different ways for 
*)Creating documentss - > db.collectionname.insertone({json_values}) ,if _id is used as unique identifier ,if not specified it is auto generated else it takes user defined value as id
*)all documents inserted should contain _id,all _id in single collection are unique
*)json_values is the document 
*)Dropping a collection in a database --> db.collectionname.drop()
*)Insert many @ single shot
	*)db.insertmany(
		[	{json1},
			{json2},...
			{json N}
		]
	);
	*)insert many in case of duplicate _id present in json value and ordered is true it stops pshing the remaining values.
	db.insertmany(
		[	{json1},
			{json1},...
			{json N}
		]
	);  , it stops after inserting second json1 and does'nt insert json N
	*)insert many in case of duplicate _id present in json value and ordered is false it bypasses & pushes the remaining values.
	db.insertmany(
		[	{json1},
			{json1},...
			{json N}
		],
		{
			"ordered":false
			----------------
			----------------
		}
	); 
	
	*) Update series cmds are used to create docuemnts & called as upserts
	
*)The _id field
	*)mongo db auto generates _id of type objectId if _id is not specfied . objectId type is specfied in BSON spec
		Object id - 12 bytes - Date(unix epoch time)+mac addr+process id+counter
*)Redaing Documents
	*)db.collectionname.find({json condition}).pretty()
	*)db.collectionname.find({json nested condition}).pretty() --> quotes for nested json condition, order of json matters
		*)json nested condition specified above r implicitly AND'ed
		
		
	*)Based on scalar value fields 
			--> eg) db.collectionname.find({title:"toy story"}).pretty()
			--> eg) more than one contion  db.collectionname.find({title:"toy story",year:1999}).pretty()
			--> eg) nested document contion  db.collectionname.find({"title.year":1999}).pretty() , since it is nested year being inside title (parent) we need to write t as "title.year", else syntax error is reported
			TO PUT IT IN SIMPLE - nested structure should be covered within quotations "parent.son"
					sample json for the above is 
						{
						"title":
							{"year": 1999}
						}
			---> eg) Based on entire array - db.moviesScratch.find({"writers" : ["Ethan Coen","Joel Coen"]}).count()
			---->array value is for exact match and for single match u need to enclose in quotes (i.e. at least it contains joel coen) db.moviesScratch.find({"writers" : "Ethan Coen"}).count()
			-----> it needs to be in same order as well
			-----> eg) db.moviesScratch.find({"writers.0" : "John Lasseter"}).count() which fethches record where John Lasseter occurs first in the writers list 
			
			
	*)Cursors - if there are more records , to list values u can type "it" (iterate ) and display next set of records 
		--> to find the nuber of batches left u can use
			var samp = db.collectionName.find()
			samp.objsLeftInBatch() ---> will result no of iterations required to fetch the entire data
		
	*)Projections - when u want only some columns to be displayed in the result u can go for it 
		*)here 0 represents false(excluded) ,1 represents true(included)
		  eg)title : 0 then title is said to be excluded
		eg)_id is displayed all the times unless u exclude it as _id : 0
			-->db.moviesScratch.find({"actors.0":"Tom Hanks"},{"title":1,_id:0}).pretty();
				this will fetch records with actor zero tom hanks and dispaly only title without _id as _id is marked as 0
				
*)Comparison operators
	--> eg ) with one field operator db.moviesScratch.find({"runtime":{ $eq : 103 }}).count();
	--> db.moviesScratch.find({"runtime":{ $in : [103,104] }},{runtime:1,_id:0}).count(); -here $in uses array to specify values

	--> eg ) with more than one field operator db.moviesScratch.find({"runtime":{ $eq : 103 },"title" : "Toy Story 3"}).count();
*)Element operator
	*)$exists - to check if an element exist or not
	---> db.collectionName.find({"tomato.meter":{$exists:true}})
		above checks if tomato.meter field exist or not
	*)$type - to check if this datatype is present or not
	--->finding all ids with type string 
	--->db.collectionName.find({"_id":{$type : "String"}})
*)Logical operators 
	--> eg) $and is used for matching conditions on same field
	"[" array is used for mentioning condition
		db.movieDetails.find({ $or : [ { "tomato.meter": { $gt: 99 } },
                               { "metacritic": { $gt: 95 } } ] })


		db.movieDetails.find({ $and : [ { "metacritic": { $ne: 100 } },
										{ "metacritic" { $exists: true } } ] })
	--->Regex operator 
		db.movieDetails.find({ "awards.text": { $regex: /^Won.*/ } },
                     { title: 1, "awards": 1, _id: 0}).pretty()mon
		--above code checks the awards.text containing won in their text and return those alone
	---->Array Operators
	     .)db.movieDetails.find({ genres: { $all: ["Comedy", "Crime", "Drama"] } }).pretty()
		 check if docuemnt contains all generes depiceted above
		 .)db.movieDetails.find({ countries: { $size: 1 } }).pretty()
		   matching the document countries with array size 1
		  .)db.movieDetails.find({ boxOffice: {$elemMatch: { country: "UK", revenue: { $gt: 15 } } } })
		     for implicity AND'ing' { country: "UK", revenue: { $gt: 15 }} similar to  (country && revenue > 15)
			 
*)Updating Documents - sometimes creates docuemnts as well
	*)updateOne()->
		--->
	*)$position modifies for pushing at a specific position
*)Intro to Java driver 
	*) first point to connect to mongodb from java drvier is ia Mongoclient
		--> for getting database , client.getDatabase("dbname") where client is the MongoClient object
		--> getting colection , db.getCollection("dbname",bson.class)
		
*)HW - 2.2 
 solution: db.grades.find({'score':{$gte:65}} , { student_id: 1,score:1,_id:0}).sort({score:1})

