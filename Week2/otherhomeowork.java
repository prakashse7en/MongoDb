Homework 2.5
db.movieDetails.find({"year":2013,"rated":"PG-13","awards.wins":0}).pretty()
Homework 2.6
db.movieDetails.find({"countries.1":"Sweden"}).count()
Challenge pblm 1
db.movieDetails.find({"awards.oscars.award":"bestPicture"})
Challenge pblm 2
*)used for finding the number of docuents to updated
db.movieDetails.find({
					 "imdb.votes": {$lt:10000} , 
					 "year":{$gte:2010,$lte:2013},
					 "tomato.consensus" : { $type: 10 }
					 }).count()
*)Orginal solution
db.movieDetails.updateMany({ year: {$gte: 2010, $lte: 2013},
                             "imdb.votes": {$lt: 10000},
                             "tomato.consensus": null },
                           { $unset: { "tomato.consensus": "" } });					 
----> $unset deletes tomato.consensus element 						 