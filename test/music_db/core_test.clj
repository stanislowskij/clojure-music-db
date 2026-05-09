(ns music-db.core-test
  (:require [clojure.test :refer :all]
            [music-db.core :refer :all]
            [music-db.data-handling :refer :all]))

(deftest country-top-tracks-test

  (is (= '({:rank "1" :track "Stateside + Zara Larsson" :artist "PinkPantheress"}
           {:rank "2" :track "Babydoll" :artist "Dominic Fike"})
         (country-top-tracks "Albania" 2)))

  (is (= '({:rank "1", :track "SWIM", :artist "BTS"}
           {:rank "2", :track "Hooligan", :artist "BTS"}
           {:rank "3", :track "Body to Body", :artist "BTS"}
           {:rank "4", :track "2.0", :artist "BTS"}
           {:rank "5", :track "FYA", :artist "BTS"})
           (country-top-tracks "Peru" 5))
  )

  (is (= () (country-top-tracks "Albansdfia" 2))) ;testing for incorrect country names.
  (is (= () (country-top-tracks "Albania" 0)))
)

(deftest artist-top-songs-test
  (is (= '({:rank "51", :track "Flashing Lights"}
           {:rank "201", :track "Heartless"}
           {:rank "212", :track "I Wonder"}
           {:rank "230", :track "All Falls Down"}
           {:rank "232", :track "FATHER (feat. Travis Scott)"}
           {:rank "253", :track "Bound 2"}
           {:rank "288", :track "Devil in a New Dress"}
           {:rank "294", :track "Father Stretch My Hands Pt. 1"}
           {:rank "325", :track "Ghost Town"}
           {:rank "328", :track "ALL THE LOVE (feat. Andre Troutman)"})
         (artist-top-songs "Kanye West" 10)))

(is (= '({:rank "66", :track "The Fate of Ophelia"}
           {:rank "272", :track "Style"}
           {:rank "339", :track "Opalite"}
           {:rank "533", :track "cardigan"})
         (artist-top-songs "Taylor Swift" 4)))

  (is (= [] (artist-top-songs "Unknown Artist" 5)))

  (is (= [] (artist-top-songs "Kanye West" 0)))
  
)


(deftest artist-top-countries-test

  (is (= '({:rank "1" :country "Albania"}
           {:rank "1" :country "Australia"})
         (artist-top-countries "Kanye West" 2)))

  (is (= '({:rank "1" :country "Barbados"}
           {:rank "1" :country "Afghanistan"}
           {:rank "1" :country "Angola"}
           {:rank "1" :country "Antigua and Barbuda"}
           {:rank "1" :country "Bahamas"})
         (artist-top-countries "Drake" 5)))

  (is (= [] (artist-top-countries "Unknown Artist" 3)))

  (is (= [] (artist-top-countries "Rihanna" 0)))

)
    

