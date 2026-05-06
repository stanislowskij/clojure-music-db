# Clojure music-db

This repository contains work for the CSCI 2601 final project done by Jaydon Stanislowski and Mahathir Mostafa.

We are using Clojure to explore global trends in listener data from Last.fm and answer questions about artist/track popularity. The database we are using is from [Kaggle](https://www.kaggle.com/datasets/tiagoadrianunes/last-fm-global-trends) and is current as of 5/5/2026, but future work on this project might include automation of regularly downloading and processing the data to keep our work up-to-date.

The repository also contains work for the exercises and solutions component of this project. The exercises we wrote on `(try)` and `(catch)`, in addition to the tutorial and solutions, can be found in the `/src/exercises` folder ([here](/src/exercises/)).

# Prerequisites and running the project

Requires Clojure version 1.11.0 or later and Leiningen. 

Before running any code, the [zip file](/resources/csv.zip) located in the `/resources` folder needs to be unzipped into a directory called `resources/csv/`. The resulting directory should contain three files:

- geo_top_tracks.csv
- geo_top_artists.csv
- global_top_tracks.csv

Without these .csv files, the project will not run. 

Simply calling `lein run` will extract the zip file in `/resources` (if this hasn't been done already) with the appropriate directory structure and delete the zip file for convenience. It will also run a few examples of the functions written to answer the project questions. You can also use `lein repl` to try out these functions yourself with your own arguments. 

# Project criteria

Goal: Ask 3 questions about this dataset that can be answered by writing Clojure functions, using at least 5 pre-defined Clojure functions that weren't covered in class or at least one feature that we haven't covered in detail (macros, transducers, parallel execution)

Additionally, for at least 3 of the functions you are using you need to write a short "tutorial" (different from clojuredocs) and at least one exercise. You will also need to solve at least 3 exercises posted by other groups.

The three questions we want to answer using this database are as follows:

1. Given a country, what are the top n tracks users are listening to in that country (according to Last.fm play count)?

2. Given an artist, what are their top n songs out of all global top songs (according to Last.fm ranking)?

3. Given an artist, what are the top n countries that listen to their music (according to Last.fm ranking?)

# Approach

While the data from Kaggle originally came with five data sets in the form of .csv files, we chose three that were relevant to our problem and removed the other two in order to save space. Additionally, we had to pre-process the data to reduce the file size, since `geo_top_artists.csv` and `geo_top_tracks.csv` contain tens of thousands of entries *per country*, resulting in extremely large data sizes that were difficult and slow to work with. 

In order to resolve this, we wrote the `trim-geo-data` function to run once in the REPL, along with `write-to-csv` to keep the data consistent with our other files and save it as a .csv file. In a further attempt to deal with the file size issue, we kept the dataset as a .zip file in our repository and wrote code to extract the archive before loading any data.

To answer the questions about our data set, we first needed a way to extract data directly from the .csv files and convert it into a data format that is idiomatic and easy to work with in Clojure. To that end, we wrote the functions `read-from-csv` and `map-from-csv` in the `data_handling.clj` file. These functions use the `clojure.data.csv` library to first extract a list of lists from the .csv files, where each sublist represents a data entry/row in the .csv file. Then, `map-from-csv` converts each sublist into a map, using the first row of the .csv file as the keys (legend) for the maps.

By formatting each data entry as a map, we can easily extract values from the entries by referencing their key in Clojure, and keeping the data in an ordered list makes it easier to perform operations like `sort-by`, `map`, `filter`, and more. Handling the data in this way is ideal for our problem, since we are essentially just using Clojure to perform database queries, and these functions are optimal for achieving that in a clean and readable way using threading macros (`->>`).

We also used a few Clojure functions/special forms that weren't covered in class. Namely: 
- `try` and `catch` for error handling to ensure that data exists and is properly structured before trying to run anything.
- `select-keys` as part of our data querying to only return necessary map keys to the user, since some of our data entries contain columns that aren't particularly useful for our purposes, like MBIDs (MusicBrainz identifiers) and track URLs.
- `doseq` and `printf` for running examples and printing results in a neatly-formatted way for each entry returned
- Various Java interop methods for reading/writing data between files

# Findings

At the time of writing this, the data we are using doesn't update between runs of the project, even though user listening data changes constantly, and this is reflected by the Kaggle dataset updating weekly. However, there were some interesting trends in the snapshot that we did observe through testing our code. Some examples of these notable observations include:

- Tracks by K-pop group BTS dominate the charts in most Latin American countries, including Venezuela, Mexico, Argentina, Chile, Paraguay, etc., with the top tracks charts looking almost identical for each.

- Kanye West is the top artist in a fairly large number of countries, including Australia, Denmark, Iceland, Israel, Russia, Sweden, United States, Canada, United Kingdom, and more.

- TODO: Write more