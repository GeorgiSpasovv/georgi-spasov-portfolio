--TEMPLATE FILE FOR COURSEWORK 1 for COMP2209
--Julian Rathke, Oct 2019

--EXERCISE A4 ONLY

--CONTAINS FUNCTION REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES


module Exercises (neighbours) where

import Data.List

type Point a = (a, a)
type Metric a = Point a -> Point a -> Double
type Tuple a = (Double, Point a)




create :: Ord a => Metric a -> Point a -> [Point a] -> [Tuple a]
create d p = sort . map f
    where f x = (d p x, x)
    
takeP:: Tuple a -> Point a
takeP (_,p) = p   

pList::  [Tuple a] ->[Point a]-> [Point a]
pList (x:xs) ys | null xs        = reverse(takeP x : ys)
                | otherwise      = pList xs (takeP x : ys) 

-- Exercise A4
neighbours :: Ord a => Int -> Metric a -> Point a -> [Point a] -> [Point a]--
neighbours _ _ _ [] = []
neighbours k d p xs   | k < 0 = error "Bad input"
                      | otherwise = take k (pList (create d p xs) [])
