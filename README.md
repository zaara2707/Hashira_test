# Shamir's Secret Sharing Solver in Java

This Java program provides a robust implementation for solving Shamir's Secret Sharing puzzles. It can reconstruct a secret from a given set of shares, even when the shares are represented in different number bases and involve very large numbers.

---

##  Core Concepts

Shamir's Secret Sharing is a cryptographic algorithm that allows a secret to be divided into multiple parts, called **shares**. The original secret can only be reconstructed when a sufficient number of shares (**threshold**, `k`) are combined.

The scheme is based on polynomial interpolation. A polynomial of degree `k-1` is created where the secret is the constant term (the value at $x=0$). Shares are then generated as points `(x, y)` on this polynomial. Given any `k` points, the original polynomial can be uniquely determined, and thus the secret can be found.

This implementation uses **Lagrange Interpolation** to reconstruct the polynomial and find its value at $x=0$.



---

##  Features

* **Solves (n, k) Schemes**: Correctly handles any threshold `k` from a total of `n` shares.
* **Arbitrary-Precision Arithmetic**: Uses `java.math.BigInteger` to accurately handle extremely large numbers, preventing precision errors.
* **Multi-Base Support**: Can parse share values given in any number base from 2 to 36.
* **Self-Contained**: No external libraries are required.

---

##  How It Works

The program follows a straightforward process to recover the secret:

1.  **Parse Shares**: The program takes a set of shares, where each share consists of an x-coordinate, a number base, and a value string.
2.  **Convert to Points**: It converts the value of each share from its given base into a standard decimal `BigInteger`. This creates a list of `(x, y)` points.
3.  **Select Threshold Points**: The program selects the first `k` points from the list. Any `k` points would be sufficient to solve the puzzle.
4.  **Lagrange Interpolation**: It applies the Lagrange interpolation formula to these `k` points to find the polynomial's y-intercept (the value at $x=0$).
5.  **Reveal Secret**: The calculated y-intercept is the original secret.

---

##  Usage

The code is ready to run and includes two pre-configured test cases in the `main` method.

1.  **Compile the code:**
    ```sh
    javac ShamirSecretSharing.java
    ```

2.  **Run the executable:**
    ```sh
    java ShamirSecretSharing
    ```

The program will print the step-by-step process for solving both test cases and display the final reconstructed secrets.

### Expected Output
