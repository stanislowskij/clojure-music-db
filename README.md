# music-db

This folder/repository contains our work for the CSCI 2601 final project.

Goal: Ask 3 questions about this dataset that can be answered by writing Clojure functions, using "at least 5 pre-defined Clojure functions that we haven't covered in class or at least one feature that we haven't covered in detail (macros, transducers, parallel execution)"

"Additionally, for at least 3 of the functions you are using you need to write a short "tutorial" (different from clojuredocs) and at least one exercise. You will also need to solve at least 3 exercises posted by other groups."

We are pulling data from Last.fm global trends using Kaggle.
Link to the database: https://www.kaggle.com/datasets/tiagoadrianunes/last-fm-global-trends

The three questions we want to answer using this database are as follows:

1. Given a country, what are the top 10 tracks/top 10 artists in that country?

2. Given an artist, what are their top 10 tracks globally?

3. (Using the top tags database) How popular are certain genres compared to others? For example, given all tags containing "rock" and all tags containing "pop", compare the sum of their ranks and see which one is lower (lower # for rank = more popular).

Authors: Jaydon Stanislowski and Mahathir Mostafa

TODO: Clarify that .zip must be unzipped, writing Clojure code to do this automatically is outside the scope of this project