SELECT  countriesAndTerritories AS Countries,


( ( SELECT sum(deaths)
)
*100.0/( SELECT sum(cases)

FROM [Table 3]
)
) AS deathsPresentage
  FROM [Table 2]
  GROUP BY Countries
  ORDER BY deathsPresentage DESC
  LIMIT 10  
;
