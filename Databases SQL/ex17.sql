SELECT countriesAndTerritories AS Countries,

( ( 
SELECT sum(cases)
)
*100.0/( SELECT popData2018

FROM [Table 3]
)
) AS casesPresentage,


( ( 
SELECT sum(deaths)
)
*100.0/( SELECT popData2018

FROM [Table 3]
)
) AS deathsPresentage


  FROM [Table 2]
    GROUP BY Countries
;
