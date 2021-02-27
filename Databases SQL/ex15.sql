SELECT
       substr(dateRep, 7,4) || '-' || 
       substr(dateRep, 4, 2)|| '-' ||
       substr(dateRep, 1, 2) as date,
       cases
        FROM [Table 2]
        WHERE countriesAndTerritories = "United_Kingdom"
        ORDER BY 
        date ASC;
        
        
