import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReliefDistributionManager {

    // --- 1. The Data Structure: A class to represent a relief item ---
    public static class ReliefItem {
        String name;
        double weight; // wi
        double value;  // vi
        double ratio;  // The calculated value-to-weight ratio

        public ReliefItem(String name, double weight, double value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
            if (weight > 0) {
                this.ratio = value / weight;
            } else {
                this.ratio = 0; // Avoid division by zero
            }
        }

        @Override
        public String toString() {
            return String.format(
                "Item[Name: %s, Weight: %.1fkg, Value: %.1f, Ratio: %.2f]",
                name, weight, value, ratio
            );
        }
    }

    // --- 2. The Fractional Knapsack Algorithm Implementation ---

    /**
     * Implements the Fractional Knapsack algorithm to maximize utility.
     * @param items The list of available relief items.
     * @param boatCapacity The maximum weight (W) the boat can carry.
     */
    public static void loadBoat(List<ReliefItem> items, double boatCapacity) {
        // Step 1: Sort items in descending order of their value-to-weight ratio.
        // This is the core of the greedy strategy.
        Collections.sort(items, new Comparator<ReliefItem>() {
            @Override
            public int compare(ReliefItem item1, ReliefItem item2) {
                // We want descending order, so we compare item2's ratio to item1's.
                return Double.compare(item2.ratio, item1.ratio);
            }
        });

        System.out.println("\nItems prioritized by value-to-weight ratio:");
        items.forEach(System.out::println);
        System.out.println("-------------------------------------------------------");

        double currentWeight = 0;
        double totalValue = 0;

        System.out.println("\nLoading the boat with capacity " + boatCapacity + "kg...\n");

        // Step 2: Iterate through the sorted items and add them to the boat.
        for (ReliefItem item : items) {
            if (currentWeight + item.weight <= boatCapacity) {
                // Case 1: The entire item can be taken.
                currentWeight += item.weight;
                totalValue += item.value;
                System.out.printf(">> Took all of '%s' (Weight: %.1fkg, Value: %.1f)\n", 
                                  item.name, item.weight, item.value);
            } else {
                // Case 2: Only a fraction of the item can be taken.
                double remainingCapacity = boatCapacity - currentWeight;
                double fractionToTake = remainingCapacity / item.weight;
                double valueFromFraction = item.value * fractionToTake;
                
                totalValue += valueFromFraction;
                currentWeight += remainingCapacity; // Boat is now full
                
                System.out.printf(">> Took %.1fkg of '%s' (%.1f%% of item, Value: %.1f)\n", 
                                  remainingCapacity, item.name, fractionToTake * 100, valueFromFraction);
                
                // The boat is full, so we can stop.
                break;
            }
        }

        System.out.println("-------------------------------------------------------");
        System.out.printf("Boat loading complete.\n");
        System.out.printf("Total Weight of Supplies: %.1fkg\n", currentWeight);
        System.out.printf("Total Utility Value: %.1f\n", totalValue);
    }


    // --- 3. The Main Method: Demonstration ---
    public static void main(String[] args) {
        // Define the available relief supplies
        List<ReliefItem> availableSupplies = new ArrayList<>();
        availableSupplies.add(new ReliefItem("Medical Kits", 10, 60)); // Must be taken whole, but logic handles it
        availableSupplies.add(new ReliefItem("Drinking Water", 30, 120)); // Divisible
        availableSupplies.add(new ReliefItem("Food Packets", 20, 100)); // Divisible
        availableSupplies.add(new ReliefItem("Blankets", 15, 45));
        
        // Define the boat's maximum weight capacity (W)
        double boatCapacity = 50.0; 

        System.out.println("Emergency Relief Operation Started.");
        System.out.println("Available Supplies:");
        availableSupplies.forEach(System.out::println);
        
        // Run the algorithm
        loadBoat(availableSupplies, boatCapacity);
    }
}
