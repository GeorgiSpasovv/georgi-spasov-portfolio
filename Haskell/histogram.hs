--TEMPLATE FILE FOR COURSEWORK 1 for COMP2209
--Julian Rathke, Oct 2020

--EXERCISE A1 ONLY

--CONTAINS FUNCTION REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES


module Exercises (histogram) where


findIndex :: Int -> (Int -> Int)
findIndex n x = div x n

changeEl :: Int -> a -> [a] -> [a]
changeEl _ _ [] = []
changeEl n z (x:xs) | n == 0 = z:xs
                    | otherwise = x:changeEl (n-1) z xs


count :: Int -> [Int] -> [Int] ->[Int]
count _ [] xs1 = xs1
count n (x:xs) xs1 = count n xs (changeEl (findIndex n x) ((xs1 !! (findIndex n x) )+ 1) xs1)
                     




-- Exercise A1
histogram :: Int -> [Int] -> [Int]
histogram n xs | n <= 0    = error "bad input"
               | otherwise = count n xs (replicate ((findIndex n (maximum xs)) + 1) 0) 