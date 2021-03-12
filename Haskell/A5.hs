--TEMPLATE FILE FOR COURSEWORK 1 for COMP2209
--Julian Rathke, Oct 2019

--EXERCISE A5 ONLY

--CONTAINS FUNCTION REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES


module Exercises (findBonding) where

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