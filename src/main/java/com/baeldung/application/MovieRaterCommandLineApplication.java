package com.baeldung.application;

import java.util.Scanner;
import java.util.UUID;

import com.baeldung.application.service.impl.MovieSearchServiceImpl;
import com.baeldung.application.service.impl.MovieServiceImpl;
import com.baeldung.domain.Movie;
import com.baeldung.domain.service.MovieSearchService;
import com.baeldung.domain.service.MovieService;
import com.baeldung.repository.impl.InMemoryMovieRepository;

/**
 * Sample command line interface for movie rating system
 */
public class MovieRaterCommandLineApplication {
    private MovieService movieService;
    private MovieSearchService movieSearchService;

    private MovieRaterCommandLineApplication(MovieService movieService, MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
        this.movieService = movieService;
    }

    public static void main(String[] args) {

        final InMemoryMovieRepository inMemoryMovieRepository = new InMemoryMovieRepository();
        final MovieServiceImpl movieService = new MovieServiceImpl(inMemoryMovieRepository);
        final MovieSearchServiceImpl movieSearchService = new MovieSearchServiceImpl(inMemoryMovieRepository);

        final MovieRaterCommandLineApplication commandLineMovieRaterApplication = new MovieRaterCommandLineApplication(movieService, movieSearchService);
        commandLineMovieRaterApplication.start();
    }

    private void start() {
        boolean doNotQuit = true;
        try (Scanner scanner = new Scanner(System.in)) {
            while (doNotQuit) {
                System.out.println("Enter movie name: ");
                String movieName = scanner.next();

                System.out.println("Enter your rating (no of stars): ");
                int noOfStars = scanner.nextInt();

                System.out.println("Enter your name: ");
                String reviewer = scanner.next();

                Movie movie = movieSearchService.findByName(movieName);
                UUID uuid = null;
                if (movie == null) {
                    uuid = movieService.saveMovie(movieName);
                } else {
                    uuid = movie.getId();
                }
                movieService.addRating(uuid, reviewer, noOfStars);

                System.out.println("Thank you for rating movie: " + movieName);
                System.out.println("Average rating for movie: " + movieName + " is " + movieService.getAverageRating(uuid) + " stars");

                System.out.println("Do you wish to quit (yes/no) ?");
                String answer = scanner.next();
                doNotQuit = answer.equals("yes") || answer.equals("y");
            }
        }
        System.out.println("Goodbye!");
    }
}
