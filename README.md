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



# Findings