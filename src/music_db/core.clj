;; Jaydon Stanislowski and Mahathir Mostafa
;; CSCI 2601 Final Project

(ns music-db.core
  (:require [music-db.data-handling :as data]
            [clojure.java.io :as io]))

;; File locations
(def csv-zip "./resources/csv.zip")
(def geo-top-tracks-csv "./resources/csv/geo_top_tracks_TRIMMED.csv")
(def geo-top-artists-csv "./resources/csv/geo_top_artists_TRIMMED.csv")
(def global-top-tracks-csv "./resources/csv/global_top_tracks.csv")

;; Data sets:
;; Each data set is a list, with rows containing maps that represent 
;; each entry (columns labeled).

;; This processing works under the assumption that each .csv file contains 
;; the legend (map keys) on the first line and all later lines are data 
;; entries respective to this legend.
;; -----------------------------------------------------------------------
;; Regional top tracks:
;;    [:country, :rank, :track, :artist, :listeners, :fetched_at,
;;     :track_url, :artist_mbid, :artist_url]
(def regional-top-tracks 
  (data/map-from-csv geo-top-tracks-csv))

;; Regional top artists:
;;    [:country, :rank, :artist, :artist_mbid, :artist_url, 
;;     :listeners, :fetched_at]
(def regional-top-artists 
  (data/map-from-csv geo-top-artists-csv))

;; Global top tracks:
;;    [:rank, :track, :artist, :playcount, :fetched_at, :track_mbid, 
;;     :track_url, :artist_mbid, :artist_url, :listeners]
(def global-top-tracks 
  (data/map-from-csv global-top-tracks-csv))
;; -----------------------------------------------------------------------

;; Answering questions:

;; 1. Given a country, what are the top n tracks in that country?
;; Write a function that takes the name of a country and uses the 
;; regional-top-tracks database to return the top n tracks in that
;; country, sorted by rank.
(defn country-top-tracks
  "Given a country, returns the top n tracks (up to 50) from that country,
   along with the artist. Output contains {:rank, :track, :artist}."
  [country, n]
  (->> regional-top-tracks
    ;; Filter by the entries whose :country matches user input
    (filter #(= country (:country %)))
    ;; Sort by the :rank column (possibly unnecessary)
    (sort-by #(Integer/parseInt (:rank %)))
    ;; Take the top n tracks
    (take n)
    ;; Trim unnecessary data, this leaves the user 
    ;; with {:rank, :track, :artist}
    (map #(select-keys % [:rank :track :artist]))))

;; 2. Given an artist, how many songs do they have in the top n 
;; songs globally?
;; Write a function that takes an artist and uses the global-top-tracks
;; database to return the tracks by that artist in the top n global tracks.
(defn artist-top-songs
  "Given an artist's name, retrieves all of their tracks from the top n
   tracks globally. Output contains {:rank, :track}."
  [artist, n]
  (->> global-top-tracks
    ;; Filter to entries whose rank value <= n
    (filter #(>= n (Integer/parseInt (:rank %))))
    ;; Filter to entries whose :artist matches user input
    (filter #(= artist (:artist %)))
    ;; Sort by the :rank column (possibly unnecessary)
    (sort-by #(Integer/parseInt (:rank %)))
    ;; Trim unnecessary data, this leaves the user
    ;; with {:rank, :track}
    (map #(select-keys % [:rank :track]))))

;; 3. Given an artist, what are the first n countries they are most 
;; popular in?
;; Write a function that takes an artist and uses the geo-top-artists
;; database to return the countries where they appear in the top 50
;; artists, then sort by their rank in each country.
(defn artist-top-countries
  "Given an artist's name, retrieves the first n countries in which
   they appear in the top 50 artists, sorted by rank. Output contains
   {:rank, :country, :artist}."
  [artist, n]
  (->> regional-top-artists
    ;; Filter to entries whose :artist matches user input
    (filter #(= artist (:artist %)))
    ;; Sort by the :rank column (needed here!)
    (sort-by #(Integer/parseInt (:rank %)))
    ;; Trim to first n results
    (take n)
    ;; Trim unnecessary data, this leaves the user
    ;; with {:rank, :country, :artist}.
    (map #(select-keys % [:rank :country :artist]))))

;; Entry point for lein run

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
