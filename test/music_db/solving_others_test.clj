(ns music-db.solving-others-test
  (:require [clojure.test :refer :all]
            [exercises.solving-others :refer :all]))

;; group-by exercise
(deftest group-by-test
  (let [map1 {"oranges" 27, "lemons" 11, "apples" 42, "peaches" 11}
        map2 {"parrots" 14, "turtles" 14, "dogs" 14, "hamsters" 84}]
    (is (= {11 [["lemons" 11] ["peaches" 11]], 27 [["oranges" 27]], 42 [["apples" 42]]} (group-kv-pairs map1))) 
    (is (= {14 [["parrots" 14] ["turtles" 14] ["dogs" 14]], 84 [["hamsters" 84]]} (group-kv-pairs map2)))))

;; partition-by exercise
(deftest partition-by-test 
  (is (= '((0 0) (3) (1 2)) (partition-on [0 0 3 1 2] 3))) 
  (is (= '((0 0 3 2 1)) (partition-on [0 0 3 2 1] 4))))
