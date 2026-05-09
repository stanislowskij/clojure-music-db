(ns exercises.solving-others)

;; 1.
;; "Given a map, use group-by to group the key value 
;; pairs by their values and return a map of the groups in 
;; ascending order."
(defn group-kv-pairs
  "Groups key-value pairs in a map by their values, returning 
   a map of the groups in ascending order."
  [m]
  (->> m 
       (group-by val)
       ;; Maps are not inherently ordered collections
       (into (sorted-map))))
;; Tests in test/music_db/solving_others_test.clj

;; 2.
;; "Write a function that takes a vector and a number, and 
;; partitions till you reach a number less than or equal 
;; to that number"
(defn partition-on
  [coll x]
  "Takes a list or vector and a second argument, x, and 
   returns the collection partitioned on occurrences of 
   x."
  (partition-by #(= % x) coll))
;; Tests in test/music_db/solving_others_test.clj