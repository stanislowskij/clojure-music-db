(ns music-db.core-test
  (:require [clojure.test :refer :all]
            [music-db.core :refer :all]))

(deftest country-top-tracks-test
  (testing "Tests for country-top-tracks function"
    (is (= (list (list "1" "SWIM" "BTS")
                 (list "2" "Body to Body" "BTS")
                 (list "3" "Hooligan" "BTS"))))
           (country-top-tracks "Mexico" 3)
    (is (= (list (list "1" "Babydoll" "Dominic Fike")
                 (list "2" "Dracula" "Tame Impala")
                 (list "3" "Streets" "Doja Cat") 
                 (list "4" "Pink + White" "Frank Ocean"))
           (country-top-tracks "Afghanistan" 4)))))
   
