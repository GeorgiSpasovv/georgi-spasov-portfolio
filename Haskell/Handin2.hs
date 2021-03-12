{-# LANGUAGE DeriveGeneric #-}
--SKELETON FILE FOR HANDIN 2 OF COURSEWORK 1 for COMP2209, 2020
--CONTAINS ALL FUNCTIONS REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES
--Julian Rathke, Oct 2020

module Exercises (neighbours,findBonding,insertFromCurrentNode,VTree(..),Direction(..),Trail(..),Zipper(..)) where

-- The following two  imports are needed for testing, do not delete
import GHC.Generics (Generic,Generic1)
import Control.DeepSeq

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

-- Exercise A5
findBonding :: Eq a => (a -> a -> Bool) -> [a] -> Maybe[(a,a)]
findBonding _ [] = Just[]
findBonding _ [x] = Just[]
findBonding f (x:y:xs) | odd(length(x:y:xs)) = Nothing
                       | f x y               = consOnMaybe x y (findBonding f xs)
                       | otherwise           = Nothing

consOnMaybe :: a -> a -> Maybe[(a,a)] -> Maybe[(a,a)]
consOnMaybe _ _ Nothing   = Nothing
consOnMaybe x y (Just xs) = Just ((x,y): (y,x) : xs)

-- Exercise A6

data VTree a = Leaf | Node (VTree a) a Int (VTree a) deriving (Eq,Show,Generic,Generic1)
data Direction a = L a Int (VTree a) | R a Int (VTree a) deriving (Eq,Show,Generic,Generic1)
type Trail a = [Direction a]
type Zipper a = (VTree a, Trail a)

instance NFData a => NFData (VTree a)
instance NFData a => NFData (Direction a)

insertFromCurrentNode :: Ord a => a -> Zipper a -> Zipper a
insertFromCurrentNode v z = (Leaf,[])
