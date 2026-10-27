package dmit2015.service;

import dmit2015.entity.Movie;
import dmit2015.repository.MovieRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovieService {

    @Inject
    private SecurityContext securityContext;

    @Inject
    private MovieRepository movieRepository;

    private boolean hasContributorRole() {
        return securityContext.isCallerInRole("Sales");
    }

    private boolean hasAdministratorRole() {
        return securityContext.isCallerInRole("Administration");
    }


    public void addMovie(Movie movie) {
        String username = securityContext.getCallerPrincipal().getName();
        if (username.equalsIgnoreCase("anonymous")) {
            throw new RuntimeException("Access denied. Anonymouse are allowed to perform this task.");
        }

        if (!hasContributorRole()) {
            throw new RuntimeException("Access denied. Your role does not have permission to perform this task.");
        }
        movie.setUsername(username);

        LocalDateTime now = LocalDateTime.now();

        movie.setCreateTime(now);
        movie.setUpdateTime(now);

        movieRepository.insert(movie);
    }

    public void updateMovie(Movie movie) {
        String username = securityContext.getCallerPrincipal().getName();
        if (username.equalsIgnoreCase("anonymous")) {
            throw new RuntimeException("Access denied. Anonymouse are allowed to perform this task.");
        }

        if (!hasContributorRole()) {
            throw new RuntimeException("Access denied. Your role does not have permission to perform this task.");
        }

        movie.setUpdateTime(LocalDateTime.now());

        movieRepository.update(movie);
    }

    public void deleteMovie(Movie movie) {
        String username = securityContext.getCallerPrincipal().getName();
        if (username.equalsIgnoreCase("anonymous")) {
            throw new RuntimeException("Access denied. Anonymouse are allowed to perform this task.");
        }

        if (!hasAdministratorRole()) {
            throw new RuntimeException("Access denied. Your role does not have permission to perform this task.");
        }
        movieRepository.delete(movie);
    }

    public Optional<Movie> findMovieById(Long movieId) {
        return movieRepository.findOptionalById(movieId);
    }

    public List<Movie> getAllMovies() {
        String username = securityContext.getCallerPrincipal().getName();
        if (username.equalsIgnoreCase("anonymous")) {
            throw new RuntimeException("Access denied. Anonymouse are allowed to perform this task.");
        }

        if (!(hasContributorRole() || hasAdministratorRole())) {
            throw new RuntimeException("Access denied. Your role does not have permission to perform this task.");
        }

        if (hasContributorRole()) {
            return movieRepository.findAllByUsername(username);
        }

        return movieRepository.findAll();
    }

    public long countMovies() {
        return movieRepository.count();
    }
}
