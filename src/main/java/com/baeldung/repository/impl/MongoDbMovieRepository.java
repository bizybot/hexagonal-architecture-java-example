package com.baeldung.repository.impl;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.baeldung.domain.Movie;
import com.baeldung.domain.repository.MovieRepository;
import com.baeldung.domain.repository.MovieSearchRepository;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class MongoDbMovieRepository implements MovieRepository, MovieSearchRepository {
    private MongodExecutable mongodExecutable = null;
    @SuppressWarnings("unused")
    private MongodProcess mongod;
    private DB db = null;
    private Gson gson = new Gson();

    @SuppressWarnings("deprecation")
    public MongoDbMovieRepository() throws UnknownHostException, IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        String bindIp = "localhost";
        int port = 12345;
        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
            .net(new Net(bindIp, port, Network.localhostIsIPv6()))
            .build();

        mongodExecutable = starter.prepare(mongodConfig);
        mongod = mongodExecutable.start();
        @SuppressWarnings("resource")
        MongoClient mongo = new MongoClient(bindIp, port);
        db = mongo.getDB("moviesDB");
        db.createCollection("moviesCol", new BasicDBObject());
    }

    @Override
    public Set<Movie> searchMoviesByName(String movieName) {
        Set<Movie> searchResult = new HashSet<>();
        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put("name", new BasicDBObject("$regex", movieName).append("$options", "i"));

        DBCollection moviesCol = db.getCollection("moviesCol");
        try (DBCursor cursor = moviesCol.find(regexQuery)) {
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                searchResult.add(gson.fromJson(dbObject.toString(), Movie.class));
            }
        }
        return searchResult;
    }

    @Override
    public void save(Movie movie) {
        DBCollection moviesCol = db.getCollection("moviesCol");

        BasicDBObject bdo = BasicDBObject.parse(gson.toJson(movie));
        bdo.put("_id", movie.getId());

        moviesCol.save(bdo);
    }

    @Override
    public Movie findById(UUID movieId) {
        DBCollection moviesCol = db.getCollection("moviesCol");
        DBObject dbObject = moviesCol.findOne(new BasicDBObject("_id", movieId));

        if (dbObject == null)
            return null;
        return gson.fromJson(dbObject.toString(), Movie.class);
    }

    public void shutDown() {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }

    @Override
    public Movie findMovieByName(String movieName) {
        DBCollection moviesCol = db.getCollection("moviesCol");
        DBObject dbObject = moviesCol.findOne(new BasicDBObject("name", movieName));

        if (dbObject == null)
            return null;
        return gson.fromJson(dbObject.toString(), Movie.class);
    }

    @Override
    public void delete(UUID movieId) {
        DBCollection moviesCol = db.getCollection("moviesCol");
        moviesCol.remove(new BasicDBObject("_id", movieId));
    }

}
