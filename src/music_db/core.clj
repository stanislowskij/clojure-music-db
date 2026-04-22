;; Jaydon Stanislowski and Mahathir Mostafa
;; CSCI 2601 Final Project

(ns music-db.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.data.csv :as csv]))

;; File locations
;; This is the original database (extremely large). Don't use it.
;; #_(def geo-top-tracks-csv "./resources/csv/geo_top_tracks.csv")

;; This is the reduced version of the one above.
(def geo-top-tracks-csv "./resources/csv/geo_top_tracks_TRIMMED.csv")

(def global-top-tracks-csv "./resources/csv/global_top_tracks.csv")

(def global-top-artists-csv "./resources/csv/global_top_artists.csv")

(def global-top-tags-csv "./resources/csv/global_top_tags.csv")

;; Reading our CSV files into variables, should contain the data as
;; Clojure sequences
(defn get-from-csv
  "Takes a path to a .csv file and outputs a lazy sequence of the data.
   The first row will contain the legend/keys of the .csv file."
  [path]
  (with-open [reader (io/reader path)]
       (let [data (csv/read-csv reader)]
         (reduce conj [] data))))

;; We are only using these two functions once in the project.
;; This is to trim the geo-top-tracks.csv dataset so that each country
;; only has its top 50 songs. It is impractical for our purposes to have
;; each country with thousands of songs listed, as reading it into the REPL
;; is extremely slow.
(defn write-to-csv
  "Takes a list of lists (data) and outputs a new .csv file to the 
   specified path. Helper function here for our purposes."
  [path, data]
  (with-open [writer (io/writer path)]
    (csv/write-csv writer, data)))

(defn- prune-data
  "Helper function to trim geo_top_tracks.csv to only include
   50 songs per country, to make the dataset easier to work with."
  []
  ;; Load the dataset in once
  (def regional-top-tracks (get-from-csv geo-top-tracks-csv))
  (->> regional-top-tracks
    ;; Take the second column of each entry (rank), reduce to
    ;; entries whose rank value <= 50
    (filter #(or (= "rank" (second %)) 
                  (>= 50 (Integer/parseInt (second %)))))
    ;; Write the new data
    (write-to-csv "./resources/csv/geo_top_tracks_TRIMMED.csv")))

;; Do not uncomment this one unless you want REPL to be really really slow.
;; Use the TRIMMED database instead. This is just our original copy of it.
;; Regional top tracks:
;;     ["country", "rank", "track", "artist", "listeners", "fetched_at",
;;      "track_url", "artist_mbid", "artist_url", "playcount"]
;;(def regional-top-tracks (get-from-csv geo-top-tracks-csv))

;; Regional top tracks:
;;    ["country", "rank", "track", "artist", "listeners", "fetched_at",
;;     "track_url", "artist_mbid", "artist_url", "playcount"]
(def regional-top-tracks (get-from-csv geo-top-tracks-csv))

;; Global top tracks:
;;    ["rank", "track", "artist", "playcount", "fetched_at", "track_mbid", 
;;     "track_url", "artist_mbid", "artist_url", "listeners"]
(def global-top-tracks (get-from-csv global-top-tracks-csv))

;; Global top artists:
;;    ["rank", "artist", "listeners", "playcount", "fetched_at", 
;;     "artist_mbid", "artist_url"]
(def global-top-artists (get-from-csv global-top-artists-csv))

;; Global top tags:
;;    ["rank", "tag", "tag_url", "reach", "taggings", "fetched_at"]
(def global-top-tags (get-from-csv global-top-tags-csv))

;; Questions to answer about the databases:

;; 1. Given a country, what are the top n tracks in that country?
;; Write a function that takes the name of a country in the world
;; and uses the global_top_artists database to return the top n tracks
;; sorted by rank.
(defn country-top-tracks
  "Given a country, returns the top n tracks (up to 50) from that country,
   removing unnecessary data from each entry.
   Each sublist in the resulting collection is formatted as (\"rank\", \"track\", \"artist\")."
  [country, n]
  (->> regional-top-tracks
    ;; Filter by the first column ("country") of each row. Make sure it matches
    ;; the country that the user gave.
    (filter #(= country (first %)))
    ;; Sort by the second column ("rank").
    (sort-by #(Integer/parseInt (second %)))
    ;; Take the first n entries, or the top n tracks that the user wanted.
    (take n)
    ;; Trim the rest of the columns off to exclude unnecessary metadata in the results.
    ;; This removes "country", "listeners", "fetched_at", "artist_mbid", etc.
    (map #(rest (take 4 %)))))

;; 2. Given an artist, how many songs do they have in the top n songs globally?
;; i.e., how popular is their music globally?
(defn artist-top-songs
  "Given an artist name, retrieves all of their hit songs from the list of top n
   tracks globally, removing unnecessary data from each entry.
   Each sublist in the resulting collection is formatted as (\"rank\", \"track\")."
  [artist, n]
  (try 
    (->> global-top-tracks
    ;; Filter rank <= n, also remove first row which has the labels
    (filter #(and (not= "rank" (first %)) (>= n (Integer/parseInt (first %)))))
    ;; Filter on the "artist" (3rd) column
    (filter #(= artist (nth % 2)))
    ;; Sort by rank
    (sort-by #(Integer/parseInt (first %)))
    ;; Trim unnecessary data, i.e. take first two columns
    (map #(take 2 %)))
    (catch ClassCastException _ 
      (println "Invalid data. Arguments must be a string followed by a number."))))

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (println "Hello, World!"))