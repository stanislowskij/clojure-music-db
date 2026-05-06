;; Jaydon Stanislowski and Mahathir Mostafa
;; CSCI 2601 Final Project

(ns music-db.core
  (:require [music-db.data-handling :as data]
            [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]))

;; File locations
(def csv-zip "./resources/csv.zip")
(def geo-top-tracks-csv "./resources/csv/geo_top_tracks_TRIMMED.csv")
(def geo-top-artists-csv "./resources/csv/geo_top_artists_TRIMMED.csv")
(def global-top-tracks-csv "./resources/csv/global_top_tracks.csv")

;; Data sets:
;; -----------------------------------------------------------------------
;; Regional top tracks:
;;    ["country", "rank", "track", "artist", "listeners", "fetched_at",
;;     "track_url", "artist_mbid", "artist_url", "playcount"]
(def regional-top-tracks (data/get-from-csv geo-top-tracks-csv))

;; Regional top artists:
;;    ["country", "rank", "artist", "artist_mbid", "artist_url", 
;;     "listeners", "fetched_at"]
(def regional-top-artists (data/get-from-csv geo-top-artists-csv))

;; Global top tracks:
;;    ["rank", "track", "artist", "playcount", "fetched_at", "track_mbid", 
;;     "track_url", "artist_mbid", "artist_url", "listeners"]
(def global-top-tracks (data/get-from-csv global-top-tracks-csv))
;; -----------------------------------------------------------------------

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
;;  (try 
    (->> global-top-tracks
      ;; Remove the row containing "rank"
      (filter #(not= "rank" (first %)))
      ;; Filter to only rows whose rank value <= n
      (filter #(>= n (Integer/parseInt (first %))))
      ;; Filter on the "artist" (3rd) column
      (filter #(= artist (nth % 2)))
      ;; No need to sort by rank since this is the default
      ;; sorting for this dataset.
      ;; Trim unnecessary data, i.e. take first two columns
      (map #(take 2 %))))
;;    (catch Exception _ 
;;      (str "Invalid data. Arguments must be a string followed by a number."))))

;; 3. Given an artist, what countries are they the most popular in?

(defn -main
  "Unzips the dataset located in /resources and performs relevant data
   processing. Also provides some examples of the project work.
   This only needs to run once using lein run."
  [& args]
  ;; Verify file structure is missing and .zip is present
  (if (and (data/file-exists? csv-zip)
           (not (data/file-exists? geo-top-tracks-csv 
                  geo-top-artists-csv global-top-tracks-csv)))
    ;; Extract the data
    (do
      (println "Unzipping csv.zip...")
      (data/unzip csv-zip "./resources/csv/")
      ;; TODO: Probably better to keep the .zip for now for testing purposes.
      ;; (println "Removing csv.zip...")
      ;; (.delete (io/file "./resources/csv.zip"))
      (println "All done.\nRunning examples:"))
    ;; Print and do nothing
    (println "csv.zip is already unzipped.\nRunning examples:"))
  ;; Examples
)