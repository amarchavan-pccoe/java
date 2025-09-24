import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StreamFlixSorter {

    // --- 1. The Data Structure: A class to represent a Movie ---
    public static class Movie {
        private String title;
        private double imdbRating;
        private int releaseYear;
        private long watchTimeMinutes;

        public Movie(String title, double imdbRating, int releaseYear, long watchTimeMinutes) {
            this.title = title;
            this.imdbRating = imdbRating;
            this.releaseYear = releaseYear;
            this.watchTimeMinutes = watchTimeMinutes;
        }
        
        // Getters to access movie properties
        public double getImdbRating() { return imdbRating; }
        public int getReleaseYear() { return releaseYear; }
        public long getWatchTimeMinutes() { return watchTimeMinutes; }

        @Override
        public String toString() {
            return String.format(
                "Movie[Title: \"%s\", Rating: %.1f, Year: %d, Watched: %d mins]",
                title, imdbRating, releaseYear, watchTimeMinutes
            );
        }
    }

    // --- 2. The Quicksort Algorithm Implementation ---

    /**
     * The main Quicksort function that sorts the list of movies.
     * It uses a Comparator to allow sorting on different parameters.
     * @param movies The list of movies to sort.
     * @param comparator The logic to compare two movies.
     */
    public static void quickSort(List<Movie> movies, Comparator<Movie> comparator) {
        if (movies == null || movies.size() <= 1) {
            return; // Already sorted
        }
        quickSortRecursive(movies, 0, movies.size() - 1, comparator);
    }

    /**
     * The recursive helper method for Quicksort.
     */
    private static void quickSortRecursive(List<Movie> movies, int low, int high, Comparator<Movie> comparator) {
        if (low < high) {
            // pi is the partitioning index, arr[pi] is now at the right place
            int pi = partition(movies, low, high, comparator);

            // Recursively sort elements before partition and after partition
            quickSortRecursive(movies, low, pi - 1, comparator);
            quickSortRecursive(movies, pi + 1, high, comparator);
        }
    }

    /**
     * This function takes the last element as pivot, places the pivot element 
     * at its correct position in the sorted list, and places all smaller 
     * elements to the left of the pivot and all greater elements to the right.
     */
    private static int partition(List<Movie> movies, int low, int high, Comparator<Movie> comparator) {
        Movie pivot = movies.get(high);
        int i = (low - 1); // Index of smaller element

        for (int j = low; j < high; j++) {
            // If the current element is smaller than or equal to the pivot
            // We use the comparator to decide the order
            if (comparator.compare(movies.get(j), pivot) <= 0) {
                i++;
                // Swap movies.get(i) and movies.get(j)
                Collections.swap(movies, i, j);
            }
        }

        // Swap the pivot element (movies.get(high)) with the element at i+1
        Collections.swap(movies, i + 1, high);
        return i + 1;
    }


    // --- 3. The Main Method: Demonstration ---
    public static void main(String[] args) {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Sentinel", 8.8, 2024, 1500000));
        movies.add(new Movie("Galactic Echoes", 9.2, 2021, 2200000));
        movies.add(new Movie("Midnight Runner", 7.5, 2023, 800000));
        movies.add(new Movie("Chrono Heist", 8.5, 2020, 1800000));
        movies.add(new Movie("Forgotten Realms", 9.5, 2022, 3000000));

        System.out.println("Original Unsorted List of Movies:");
        movies.forEach(System.out::println);
        System.out.println("=======================================================================");

        // --- Use Case 1: Sort by IMDB Rating (Descending) ---
        System.out.println("\n--- Sorting by IMDB Rating (Highest First) ---");
        // We use a lambda expression for the comparator: m2 vs m1 for descending order.
        quickSort(movies, (m1, m2) -> Double.compare(m2.getImdbRating(), m1.getImdbRating()));
        movies.forEach(System.out::println);
        System.out.println("=======================================================================");

        // --- Use Case 2: Sort by Release Year (Newest First) ---
        System.out.println("\n--- Sorting by Release Year (Newest First) ---");
        quickSort(movies, (m1, m2) -> Integer.compare(m2.getReleaseYear(), m1.getReleaseYear()));
        movies.forEach(System.out::println);
        System.out.println("=======================================================================");

        // --- Use Case 3: Sort by Watch Time Popularity (Most Watched First) ---
        System.out.println("\n--- Sorting by Watch Time (Most Popular First) ---");
        quickSort(movies, (m1, m2) -> Long.compare(m2.getWatchTimeMinutes(), m1.getWatchTimeMinutes()));
        movies.forEach(System.out::println);
        System.out.println("=======================================================================");
    }
}
