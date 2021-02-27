SELECT 
       continentExp AS Continent,
       substr(dateRep, 7,4) || '-' || substr(dateRep, 4, 2)|| '-' || substr(dateRep, 1, 2) as date,
       sum(cases) as numberOfCases,
       sum(deaths) as numberOfDeaths
       
       FROM [Table 2]
       LEFT JOIN
       "Table 3" ON "Table 3".countriesAndTerritories = "Table 2".countriesAndTerritories
       GROUP BY date, continent
       

  
