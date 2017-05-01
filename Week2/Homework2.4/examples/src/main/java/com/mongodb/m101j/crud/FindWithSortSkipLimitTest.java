/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.m101j.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.m101j.util.Helpers;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class FindWithSortSkipLimitTest {
	public static void main(String[] args) {
		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("course");
		MongoCollection<Document> collection = database.getCollection("findWithSortTest");

		collection.drop();

		// insert 100 documents with two random integers
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				collection.insertOne(new Document().append("i", i).append("j", j));
			}
		}

		Bson projection = Projections.fields(Projections.include("i", "j"), Projections.excludeId());
		Bson sort = Sorts.descending("j", "i");

		List<Document> all = collection.find().projection(projection).sort(sort).skip(20).limit(50).into(new ArrayList<Document>());

		for (Document cur : all) {
			Helpers.printJson(cur);
		}
	}
}
