(ns music-db.core-test
  (:require [clojure.test :refer :all]
            [music-db.core :refer :all]))

(deftest country-top-tracks-test
  (testing "Tests for country-top-tracks function"
    (is (= '('("1" "SWIM" "BTS") '("2" "Body to Body" "BTS") '("3" "Hooligan" "BTS")) (country-top-tracks Mexico 3))))
    (is (= '("2" "Body to Body" "BTS")) (country-top-tracks Venezuela 1))))
    (is (= '("1" "SWIM" "BTS")) (country-top-tracks Mexico 5))))
   
