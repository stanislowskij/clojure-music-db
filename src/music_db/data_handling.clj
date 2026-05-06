(ns music-db.data-handling
    (:require [clojure.java.io :as io]
              [clojure.string :as str]
              [clojure.data.csv :as csv])
    (:import (java.io File FileNotFoundException)
             ;; For .zip extraction
             (java.util.zip ZipInputStream)))

;; Reading our CSV files into variables, should contain the data as
;; Clojure sequences
(defn get-from-csv
  "Takes a path to a .csv file and outputs a lazy sequence of the data.
   The first row will contain the legend/keys of the .csv file."
  [path]
  (try
    (with-open [reader (io/reader path)]
      (let [data (csv/read-csv reader)]
        (reduce conj [] data)))
    (catch FileNotFoundException _
      (.println *err* (str "ERROR: Could not find a file at " path 
        ". Please ensure that csv.zip has been unzipped and "
        "that the directory structure is correct.")))))

(defn file-exists?
  "Returns true if a file (or files) exists at the specified path, otherwise
   false. Mainly used as a helper function in core/-main."
  [& paths]
  (every? true? (map #(.exists (io/file %)) paths)))

;; Credit to mikeananev on GitHub for this sample code, because
;; Java loves to overcomplicate I/O and Clojure's interop with it
;; doesn't help much.
;; https://gist.github.com/mikeananev/b2026b712ecb73012e680805c56af45f
(defn unzip
  "Uncompress a zip archive from input to output."
  [input output]
  (with-open [stream (-> input io/input-stream ZipInputStream.)]
    (loop [entry (.getNextEntry stream)]
      (if entry
        (let [save-path (str output File/separatorChar (.getName entry))
              out-file (File. save-path)]
          (if (.isDirectory entry)
            (if-not (.exists out-file)
              (.mkdirs out-file))
            (let [parent-dir (File. (.substring save-path 0 (.lastIndexOf save-path (int File/separatorChar))))]
              (if-not (.exists parent-dir) (.mkdirs parent-dir))
              (io/copy stream out-file)))
          (recur (.getNextEntry stream)))))))

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

(defn trim-geo-data
  "Helper function to trim geo_top_tracks.csv and geo_top_artists.csv
   to only include 50 songs/artists per country, to make the dataset 
   easier to work with. This function should ONLY be called on these
   two datasets."
  [csv]
  ;; "./resources/csv/{name}_TRIMMED.csv"
  (def new-name (clojure.string/replace csv #".{4}$" "_TRIMMED.csv"))
  ;; Load the dataset in once
  (->> (get-from-csv csv)
    ;; Take the second column of each entry (rank), reduce to
    ;; entries whose rank value <= 50
    (filter #(or (= "rank" (second %)) 
                 (>= 50 (Integer/parseInt (second %)))))
    ;; Write the new data
    (write-to-csv new-name)))