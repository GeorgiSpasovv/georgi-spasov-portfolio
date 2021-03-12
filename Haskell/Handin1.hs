{-# LANGUAGE DeriveGeneric #-}
--SKELETON FILE FOR COURSEWORK 1 for COMP2209, 2020
--CONTAINS ALL FUNCTIONS REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES
--Julian Rathke, Oct 2020

module Exercises (histogram,approxPi,amSplit) where

-- The following two imports are needed for testing, do not delete
import GHC.Generics (Generic,Generic1)
import Control.DeepSeq


-- Exercise A1
histogram :: Int -> [Int] -> [Int]
histogram n xs | n <= 0    = error "bad input"
               | otherwise = count n xs (replicate ((findIndex n (maximum xs)) + 1) 0) 

findIndex :: Int -> (Int -> Int)
findIndex n x = div x n

changeEl :: Int -> a -> [a] -> [a]
changeEl _ _ [] = []
changeEl n z (x:xs) | n == 0    = z:xs
                    | otherwise = x:changeEl (n-1) z xs


count :: Int -> [Int] -> [Int] ->[Int]
count _ [] xs1 = xs1
count n (x:xs) xs1 = count n xs (changeEl (findIndex n x) ((xs1 !! (findIndex n x) )+ 1) xs1)


-- Exercise A2
approxPi :: Int -> Double
approxPi n | n<=0      = error "bad Input"
           | otherwise = 2*sum(append n [])

append :: Int -> [Double] -> [Double]
append n xs | n==1       = (facDiv n ): xs
            | otherwise  = append (n-1) (facDiv n: xs)

factorial :: Rational -> Rational
factorial n = product [1..n]

doubleFac :: Rational -> Rational
doubleFac 0 = 1
doubleFac 1 = 1
doubleFac n = n * doubleFac(n-2)

facDiv:: Int -> Double
facDiv n = fromRational(factorial (fromIntegral(n-1)))/ fromRational(doubleFac (fromIntegral(2*(n-1)+1)))


-- Exercise A3
amSplit :: Ord a => [a] -> [[a]]
amSplit [] = []
amSplit xs = arrange (twoSplit xs)

isSorted1 :: (Ord a) => [a] -> Bool
isSorted1 []       = True
isSorted1 [x]      = True
isSorted1 (x:y:xs) = x <= y && isSorted1 (y:xs)

isSorted2 :: (Ord a) => [a] -> Bool
isSorted2 []       = True
isSorted2 [x]      = True
isSorted2 (x:y:xs) = x >= y && isSorted2 (y:xs)

twoSplit :: Ord a => [a] -> [[a]]
twoSplit [] = []
twoSplit [x] = [x]: twoSplit []
twoSplit xs = take 2 xs : twoSplit (drop 2 xs)

arrange :: Ord a=> [[a]] -> [[a]]
arrange [] = []
arrange [xs] = [xs]
arrange [[], x:xs] = [[x], xs] 
arrange [[x,y], [z]]    | x == y = [[x,y,z]]
                        | y == z = [[x,y,z]]
                        | isSorted1 ([x, y, z]) || isSorted2 ([x,y,z]) = [[x,y], [z]] 
                        | otherwise = [[x,y,z]]

arrange [xs, [x]]       | isSorted1 (xs++[x]) || isSorted2 (xs++[x]) = [xs, [x]]
                        | otherwise = [(xs ++ [x])]


arrange (xs:(y:ys):xss) | length xs < 2  = (xs ++ [y]) : arrange (ys:xss)
                        | xs!!(length(xs)-1) == y = (xs ++ [y]) : arrange (ys:xss)
                        | isSorted1 (xs++[y]) || isSorted2 (xs++[y]) = (xs) : arrange((y:ys):xss)
                        | otherwise = (xs ++ [y]) : arrange (ys:xss)