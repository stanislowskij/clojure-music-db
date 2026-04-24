# Try and Catch Tutorial

---

## Table of Contents

- [Overview](#overview)
    - [Why?](#why?)
- [Examples](#examples)
    - [Example 1](#example-1)
    - [Example 2](#example-2)
    - [Example 3](#example-3)
- [Exercises](#exercises)
    - [Exercise 1](#exercise-1)
    - [Exercise 2](#exercise-2)
- [Resources](#resources)

## Overview

The two Clojure features we are choosing for our group's exercises are `try` and `catch`. These are often used together in the context of error handling, and may be familiar to Java users. We will explain these using some sample code.

`try` is a special form that is followed by any number of statements and a call to the `catch` special form, which takes a Java exception type, a name for the exception, and any number of further statements.

Given pseudocode like this:

```clojure
(try 
    (statement A)
    (statement B)
    (statement C)
    (catch Exception e 
        (statement D) 
        (statement E)
        (statement F)))
```

The REPL will first attempt to run all of the code contained in the `try` block in sequence like usual (statements A, B, C, ...). If any code in the `try` block is impossible to execute because of an exception or an error, like dividing by zero, or a failed type conversion, etc., it will jump to the `catch` block and execute the code there instead (statements D, E, F, ...).

The error that we catch must be an instance of a Java Exception object. The documentation on Java exceptions is available [here](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html). There are multiple subclasses of Java Exceptions for more specific types of issues (like IOException, ClassCastException, etc.) and users can even create their own. Java exceptions must have a message associated with them, which we can get in Clojure using `(.getMessage exception-name)`.

### Why?

The `try` and `catch` statements exist because we sometimes want to ensure that our program can smoothly handle runtime errors that can't be accounted for at compile time (like users making a typo in an input), and we need to decide what to do if those errors happen (like telling the user to enter a different value instead), rather than letting the program just panic and crash, the default behavior for unhandled exceptions. It would be a huge problem if, for example, the entire server of our web app crashed every single time a user tried to put a dollar sign in their username.

---

## Examples

### Example 1.  

When we call this program using `lein run`, instead of crashing because `evil-variable` wasn't defined, the REPL will instead just print "Something went wrong:" followed by our exception message, and continue as normal with evaluating the rest of the statements.

Input:
```clojure
(def evil-variable) ; bad definition, giving it no value
(try 
    (+ 5 evil-variable)
    (catch Exception e 
    (println (str "Something went wrong: " (.getMessage e)))))
(println (+ 2 2))
```

Output: 
```
Something went wrong: class clojure.lang.Var$Unbound cannot be cast to class java.lang.Number (clojure.lang.Var$Unbound is in unnamed module of loader 'app'; java.lang.Number is in module java.base of loader 'bootstrap')
4
```

### Example 2.
This is a poorly optimized recursive function from a previous lab assignment that doesn't utilize tail recursion, making it take up too much memory on larger inputs and crash. If we wrap calls to this function in a `try`-`catch` block, we can bypass this (though in practice, we should probably just rewrite this using higher-order functions).

Input:
```clojure
(defn sum-elements
  "Takes a vector or a list of numbers and returns the sum of its elements."
  [coll]
  (if (empty? coll) 0 ; base case
      ; recursive case:
      (+ (first coll) (sum-elements (rest coll)))))
(try
    (println (sum-elements (range 10)))
    (println (sum-elements (range 500)))
    (println (sum-elements (range 999999)))
    (catch StackOverflowError err
        (println "Your collection is too scary. :(")))
```

Output: 
```
45
124750
Your collection is too scary. :(
```

### Example 3. 
We can also use `try` and `catch` to handle multiple different kinds of exceptions at a time, and we don't have to just print an error message in the `catch` block. If we really want to, we can use it to execute different code instead (for whatever weird reason you could possibly desire).

Input:
```clojure
(defn weird-function
    "Takes two numbers, n and m, and attempts to divide n/m.
     If m = 0, returns the result of dividing n by 999 instead.
     If either input isn't a number, returns nil instead."
     ;; Please never do this in actual code.
    [n m]
    (try
        (/ n m)
        (catch ArithmeticException e
            (/ n 999)) ; m was 0, so division was impossible
        (catch ClassCastException e
            (println "Dude, read the docstring :(")
            nil)))
(println (weird-function "hello" 2))
(println (weird-function 999 0)) ; prints 1, because 0 gets replaced with 999
```

Output: 
```
Dude, read the docstring :(
nil
1
```

---

## Exercises

### Exercise 1.

Write a function `(multiply-all)` that takes a list or a vector of numbers and returns their product. If the user enters any elements in the list that can't be multiplied together (like strings or hashmaps), the function excludes them from the product, and warns the user by printing a single custom exception message to the terminal.

<details>
    <summary>💡 Hint</summary>
    If the try block fails, the catch block should do some filtering on the original collection along with the exception message. After the collection is filtered, you can take the product of the elements as normal.
</details>

#### Test Case

Input:

```clojure
(multiply-all '(5 3 "Hello" 2))
```

Output:

```
multiply-all: An element in the collection was not a number, ignoring it.
30
```

#### Test Case

Input:

```clojure
(multiply-all [9 :a 2 4 "string" 0 -3])
```

Output:

```
multiply-all: An element in the collection was not a number, ignoring it.
0
```

### Exercise 2.

Write a function (`parse-all`) that takes a list or a vector of strings and converts each string into a number. If the string can't be parsed into an integer (NumberFormatException), the function removes it from the output list, no exception message needed.

You don't need to handle cases where the inputs aren't strings at all.

<details>
    <summary>💡 Hint</summary>
    If you're doing this with higher order functions, you can start by filtering the list based on whether or not Integer/parseInt throws a NumberFormatException. Use a try-catch block inside a filter, and make it return true if (Integer/parseInt %) succeeds without any errors. Make the catch block return false.
</details>

#### Test Case

Input:

```clojure
(parse-all ["2" "5" "bad" "bad" "0"])
```

Output:

```
(2 5 0)
```

#### Test Case

Input:

```clojure
(parse-all '("1" "2" "3" "4" "5"))
```

Output:

```
(1 2 3 4 5)
```

---

## Resources

- [Notes on exception handling in Clojure, by Ivan Grishaev](https://grishaev.me/en/clj-book-exceptions/)
- [JavaDocs on the Exception object](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)
- [ClojureDocs page on (try) with examples](https://clojuredocs.org/clojure.core/try)
- [ClojureDocs page on (catch) with examples](https://clojuredocs.org/clojure.core/catch)

Authored by Jaydon Stanislowski and Mahathir Mostafa for the CSCI 2601 final project at University of Minnesota Morris.