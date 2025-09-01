import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecretSharing {
    
    // Point class to store (x, y) coordinates
    static class Point {
        int x;
        BigInteger y;
        
        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    
    // Convert a number from given base to decimal using BigInteger
    public static BigInteger baseToDecimal(String value, int base) {
        BigInteger decimalValue = BigInteger.ZERO;
        BigInteger baseBig = BigInteger.valueOf(base);
        
        value = value.toLowerCase();
        
        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(i);
            int digitValue;
            
            if (digit >= '0' && digit <= '9') {
                digitValue = digit - '0';
            } else {
                // For bases > 10, a=10, b=11, ..., z=35
                digitValue = digit - 'a' + 10;
            }
            
            decimalValue = decimalValue.multiply(baseBig).add(BigInteger.valueOf(digitValue));
        }
        
        return decimalValue;
    }
    
    // Perform Lagrange interpolation to find polynomial value at x
    public static BigInteger lagrangeInterpolation(List<Point> points, int x) {
        int n = points.size();
        double result = 0.0;
        
        for (int i = 0; i < n; i++) {
            Point pi = points.get(i);
            int xi = pi.x;
            BigInteger yi = pi.y;
            
            // Calculate Lagrange basis polynomial L_i(x)
            double numerator = 1.0;
            double denominator = 1.0;
            
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    Point pj = points.get(j);
                    int xj = pj.x;
                    numerator *= (x - xj);
                    denominator *= (xi - xj);
                }
            }
            
            double li = numerator / denominator;
            result += yi.doubleValue() * li;
        }
        
        return BigInteger.valueOf(Math.round(result));
    }
    
    // Solve Shamir's Secret Sharing puzzle
    public static BigInteger solveShamirSecretSharing(TestCase testCase) {
        int n = testCase.n; // total number of shares
        int k = testCase.k; // minimum shares needed (degree + 1)
        
        System.out.println("Total shares: " + n + ", Minimum needed: " + k);
        System.out.println("Polynomial degree: " + (k-1));
        
        // Parse all points
        List<Point> points = new ArrayList<>();
        for (Share share : testCase.shares) {
            BigInteger y = baseToDecimal(share.value, share.base);
            points.add(new Point(share.x, y));
            
            System.out.println("Point " + share.x + ": base " + share.base + 
                             ", value '" + share.value + "' -> decimal " + y);
        }
        
        System.out.println("\nFound " + points.size() + " points");
        
        // Use first k points for interpolation (we only need k points)
        List<Point> selectedPoints = points.subList(0, k);
        System.out.println("Using first " + k + " points for interpolation:");
        for (Point p : selectedPoints) {
            System.out.println("  " + p);
        }
        
        // Find the secret (polynomial value at x=0)
        BigInteger secret = lagrangeInterpolation(selectedPoints, 0);
        
        System.out.println("\nSecret (polynomial at x=0): " + secret);
        return secret;
    }
    
    // Helper classes for test data
    static class Share {
        int x;
        int base;
        String value;
        
        Share(int x, int base, String value) {
            this.x = x;
            this.base = base;
            this.value = value;
        }
    }
    
    static class TestCase {
        int n, k;
        List<Share> shares;
        
        TestCase(int n, int k) {
            this.n = n;
            this.k = k;
            this.shares = new ArrayList<>();
        }
        
        void addShare(int x, int base, String value) {
            shares.add(new Share(x, base, value));
        }
    }
    
    public static void main(String[] args) {
        // Test Case 1
        System.out.println("=== TEST CASE 1 ===");
        TestCase test1 = new TestCase(4, 3);
        test1.addShare(1, 10, "4");
        test1.addShare(2, 2, "111");
        test1.addShare(3, 10, "12");
        test1.addShare(6, 4, "213");
        
        BigInteger secret1 = solveShamirSecretSharing(test1);
        
        System.out.println("\n" + "=".repeat(50));
        
        // Test Case 2
        System.out.println("=== TEST CASE 2 ===");
        TestCase test2 = new TestCase(10, 7);
        test2.addShare(1, 6, "13444211440455345511");
        test2.addShare(2, 15, "aed7015a346d635");
        test2.addShare(3, 15, "6aeeb69631c227c");
        test2.addShare(4, 16, "e1b5e05623d881f");
        test2.addShare(5, 8, "316034514573652620673");
        test2.addShare(6, 3, "2122212201122002221120200210011020220200");
        test2.addShare(7, 3, "20120221122211000100210021102001201112121");
        test2.addShare(8, 6, "20220554335330240002224253");
        test2.addShare(9, 12, "45153788322a1255483");
        test2.addShare(10, 7, "1101613130313526312514143");
        
        BigInteger secret2 = solveShamirSecretSharing(test2);
        
        System.out.println("\n=== RESULTS ===");
        System.out.println("Test Case 1 Secret: " + secret1);
        System.out.println("Test Case 2 Secret: " + secret2);
        
        // Manual verification for Test Case 1
        System.out.println("\n=== MANUAL VERIFICATION (Test Case 1) ===");
        System.out.println("Converting values to decimal:");
        System.out.println("Point 1: base 10, '4' -> 4");
        System.out.println("Point 2: base 2, '111' -> 7");
        System.out.println("Point 3: base 10, '12' -> 12");
        System.out.println("\nUsing points (1,4), (2,7), (3,12) to find polynomial f(x) = ax² + bx + c");
        System.out.println("At x=0, we want f(0) = c (the constant term)");
        
        // Manual calculation using Lagrange interpolation
        double manualCalc = 4.0 * (0-2)*(0-3)/((1-2)*(1-3)) + 
                           7.0 * (0-1)*(0-3)/((2-1)*(2-3)) + 
                           12.0 * (0-1)*(0-2)/((3-1)*(3-2));
        System.out.println("Manual calculation: f(0) = " + manualCalc);
    }
}