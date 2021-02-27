--
-- File generated with SQLiteStudio v3.2.1 on Fri May 22 18:05:10 2020
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Table 1
CREATE TABLE "Table 1" (dateRep DATE, day INTEGER, month VARCHAR, year INTEGER, PRIMARY KEY (dateRep));

-- Table: Table 2
CREATE TABLE "Table 2" (dateRep DATE, cases INTEGER, deaths INTEGER, countriesAndTerritories VARCHAR, PRIMARY KEY (dateRep, countriesAndTerritories));

-- Table: Table 3 
CREATE TABLE "Table 3 " (countriesAndTerritories VARCHAR, geoId VARCHAR, countryterritoryCode VARCHAR, popData2018 INTEGER, continentExp VARCHAR, PRIMARY KEY (countriesAndTerritories));

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;

https://lp-cms-production.imgix.net/2019-06/113840805.jpg?fit=crop&q=40&sharp=10&vib=20&auto=format&ixlib=react-8.6.4

https://upload.wikimedia.org/wikipedia/commons/5/53/Maroon_Bells_%2811553%29a.jpg