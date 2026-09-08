import java.util.*;
import java.util.stream.*;

class Song {
    String title;
    String artist;
    String genre;
    double rating; // 0.0 to 5.0

    Song(String title, String artist, String genre, double rating) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.rating = rating;
    }

    void display() {
        System.out.println(title + " | " + artist + " | " + genre + " | Rating: " + rating);
    }
}

public class MusicStreamLab {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Song> songs = new ArrayList<>();

        int n = 0;
        while (true) {
            System.out.print("Enter number of songs (1-20): ");
            n = Integer.parseInt(sc.nextLine().trim());
            if (n >= 1 && n <= 20) break;
            System.out.println("Please enter a number between 1 and 20.");
        }

        for (int i = 0; i < n; i++) {
            System.out.println("\nSong " + (i + 1));

            System.out.print("Title: ");
            String title = sc.nextLine().trim();
            while (title.isEmpty()) {
                System.out.print("Title cannot be empty. Enter again: ");
                title = sc.nextLine().trim();
            }

            System.out.print("Artist: ");
            String artist = sc.nextLine().trim();
            while (artist.isEmpty()) {
                System.out.print("Artist cannot be empty. Enter again: ");
                artist = sc.nextLine().trim();
            }

            System.out.print("Genre: ");
            String genre = sc.nextLine().trim();
            while (genre.isEmpty()) {
                System.out.print("Genre cannot be empty. Enter again: ");
                genre = sc.nextLine().trim();
            }

            double rating = -1;
            while (rating < 0 || rating > 5) {
                System.out.print("Rating (0.0 - 5.0): ");
                rating = Double.parseDouble(sc.nextLine().trim());
                if (rating < 0 || rating > 5)
                    System.out.println("Rating must be between 0.0 and 5.0.");
            }

            songs.add(new Song(title, artist, genre, rating));
        }

        // forEach() with lambda - display all records
        System.out.println("\n----- All Songs -----");
        songs.forEach(s -> s.display());

        // filter() - songs above a minimum rating
        System.out.print("\nEnter minimum rating to filter by: ");
        double minRating = Double.parseDouble(sc.nextLine().trim());

        System.out.println("\n----- Songs with rating >= " + minRating + " -----");
        songs.stream()
                .filter(s -> s.rating >= minRating)
                .forEach(s -> s.display());

        // sorted() - by rating, highest first
        List<Song> sortedSongs = songs.stream()
                .sorted((a, b) -> Double.compare(b.rating, a.rating))
                .collect(Collectors.toList());

        System.out.println("\n----- Songs Sorted by Rating (High to Low) -----");
        sortedSongs.forEach(s -> s.display());

        // distinct() - unique genres
        System.out.println("\n----- Distinct Genres -----");
        songs.stream()
                .map(s -> s.genre)
                .distinct()
                .forEach(g -> System.out.println(g));

        // limit() - top 3 rated songs (or fewer if less than 3 songs)
        int topCount = Math.min(3, songs.size());
        System.out.println("\n----- Top " + topCount + " Rated Song(s) -----");
        sortedSongs.stream()
                .limit(topCount)
                .forEach(s -> s.display());

        // Terminal operation - average rating
        double avgRating = songs.stream()
                .mapToDouble(s -> s.rating)
                .average()
                .orElse(0.0);
        System.out.println("\nAverage Rating: " + avgRating);

        // Added complexity - average rating grouped by genre
        System.out.println("\n----- Average Rating by Genre -----");
        Map<String, Double> genreAverages = songs.stream()
                .collect(Collectors.groupingBy(
                        s -> s.genre,
                        Collectors.averagingDouble(s -> s.rating)));

        genreAverages.forEach((genre, avg) ->
                System.out.println(genre + " -> " + avg));

        sc.close();
    }
}
