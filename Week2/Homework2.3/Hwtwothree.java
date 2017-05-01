package com.mongodb.m101j.crud;

import static com.mongodb.client.model.Filters.eq;

import java.net.UnknownHostException;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

public class Hwtwothree {
	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("students");
		MongoCollection<Document> collection = database.getCollection("grades");

		Bson filter = eq("type", "homework");
		Bson sort = Sorts.ascending("student_id", "score");

		MongoCursor<Document> cursor = collection.find(filter).sort(sort).iterator();
		int curStudentId = -1;
		/* System.out.println("count of list"+all.size()); */
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				int studentId = (Integer) doc.get("student_id");
				if (studentId != curStudentId) {
					collection.deleteMany(doc);
					curStudentId = studentId;
				}
			}
		} finally {
			// Close cursor
			cursor.close();
		}
		// Close mongoClient
		client.close();


	}
}