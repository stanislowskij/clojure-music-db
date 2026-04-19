;; Jaydon Stanislowski and Mahathir Mostafa
;; CSCI 2601 Final Project

(ns music-db.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.data.csv :as csv]))

;; File locations
(def geo-top-tracks-csv "./resources/csv/geo_top_tracks.csv")
(def global-top-artists-csv "./resources/csv/global_top_artists.csv")
(def global-top-tags-csv "./resources/csv/global_top_tags.csv")

;; Reading our CSV files into variables, should contain the data as
;; Clojure sequences
(defn get-from-csv
  "Takes a path to a .csv file and outputs a lazy sequence of the data.
   The first row will contain the legend/keys of the .csv file."
  [str]
  (with-open [reader (io/reader str)]
       (let [data (csv/read-csv reader)]
         (reduce conj [] data))))

;; REPL is really really really really slow if these are uncommented.

;; Regional top tracks:
;;     ["country", "rank", "track", "artist", "listeners", "fetched_at",
;;      "track_url", "artist_mbid", "artist_url", "playcount"]
(def regional-top-tracks (get-from-csv geo-top-tracks-csv))

;; Global top artists:
;;     ["rank", "artist", "listeners", "playcount", "fetched_at", 
;;      "artist_mbid", "artist_url"]
;;(def global-top-artists (get-from-csv global-top-artists-csv))

;; Global top tags:
;;     ["rank", "tag", "tag_url", "reach", "taggings", "fetched_at"]
;;(def global-top-tags (get-from-csv global-top-tags-csv))

;; Questions to answer about the databases:

;; 1. Given a country, what are the top 10 tracks in that country?
;; Write a function that takes the name of a country in the world
;; and uses the global_top_artists database to return the top 10 tracks
;; sorted by rank.
(defn country-top-10-tracks
  "Given a country, returns the top 10 tracks from that country,
   removing unnecessary data from each entry.
   Each vector in the resulting collection is formatted as [\"rank\", \"track\", \"artist\"]"
  [country]
  ;; TODO: PLEASE REFACTOR THIS THIS IS SO UGLY
  (map #(rest (take 4 %)) (take 10 (sort-by #(Integer/parseInt (first (rest %))) (filter #(= country (first %)) regional-top-tracks)))))

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (println "Hello, World!"))
