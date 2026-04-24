#_(ns exercises.exercise-solutions)

;; 1. Write a function (multiply-all) that takes a list or a vector 
;; of numbers and returns their product. If the user enters any 
;; elements in the list that can't be multiplied together (like 
;; strings or hashmaps), the function excludes them from the product, 
;; and warns the user by printing any custom exception message to the 
;; terminal.
(defn multiply-all
    "Returns the product of elements in coll, ignoring any
     non-numeric elements (with a warning message if there
     are any)."
    [coll]
    (try
        (reduce * coll)
        (catch ClassCastException _
            (println "multiply-all: An element in the" 
                     "collection was not a number, ignoring it.")
            (->> coll
                (filter #(number? %))
                (reduce *)))))

;; 2. Write a function (`parse-all`) that takes a list or a vector of 
;; strings and converts each string into a number. If the string can't 
;; be parsed into an integer (i.e. throws a NumberFormatException when
;; you try), the function removes it from the output list, no exception 
;; message needed.
(defn parse-all
    "Returns a list of integers parsed from a collection of strings, 
     removing any elements that can't be parsed."
    [coll]
    (->> coll
        (filter #(try
            (Integer/parseInt %) true
            (catch NumberFormatException _ false)))
        (map #(Integer/parseInt %))))