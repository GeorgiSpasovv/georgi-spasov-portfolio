SELECT 
       (select sum(cases) from "Table 2") totalCases,
       (select sum(deaths) from "Table 2") totalDeaths
        FROM [Table 2];
