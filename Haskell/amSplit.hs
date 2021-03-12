--TEMPLATE FILE FOR COURSEWORK 1 for COMP2209
--Julian Rathke, Oct 2020

--EXERCISE A3 ONLY

--CONTAINS FUNCTION REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES


module Exercises (amSplit) where

-- Exercise A3
isSorted1 :: (Ord a) => [a] -> Bool
isSorted1 []       = True
isSorted1 [x]      = True
isSorted1 (x:y:xs) = x <= y && isSorted1 (y:xs)

isSorted2 :: (Ord a) => [a] -> Bool
isSorted2 []       = True
isSorted2 [x]      = True
isSorted2 (x:y:xs) = x >= y && isSorted2 (y:xs)

listCheck ::Ord a => [a] -> [a]
listCheck [] = []
listCheck [x] = x:listCheck []
listCheck (x:y:xs)  | x >= y    = x:listCheck (y:xs)
                    | otherwise = x:listCheck []







twoSplit :: Ord a => [a] -> [[a]]
twoSplit [] = []
twoSplit [x] = [x]: twoSplit []
twoSplit xs = take 2 xs : twoSplit (drop 2 xs)

arrange :: Ord a=> [[a]] -> [[a]]
arrange [] = []
arrange [xs] = [xs]
arrange [[], x:xs] = [[x], xs] 
arrange [[x,y], [z]] | x == y = [[x,y,z]]
                     | y == z = [[x,y,z]]
                     | isSorted1 ([x, y, z]) || isSorted2 ([x,y,z]) = [[x,y], [z]] 
                     | otherwise = [[x,y,z]]

--arrange [xs, [x]] | xs!!(length(xs)-1) >= x = [(xs ++ [x])]
  --                | otherwise   = [xs, [x]]

arrange [xs, [x]] | isSorted1 (xs++[x]) || isSorted2 (xs++[x]) = [xs, [x]]
                  | otherwise = [(xs ++ [x])]

--arrange (xs:(y:ys):xss) | xs!!(length(xs)-1) >= y = (xs ++ [y]) : arrange (ys:xss)
--                        | otherwise   = (xs) : arrange((y:ys):xss)

arrange (xs:(y:ys):xss) | length xs < 2  = (xs ++ [y]) : arrange (ys:xss)
                        | xs!!(length(xs)-1) == y = (xs ++ [y]) : arrange (ys:xss)
                        | isSorted1 (xs++[y]) || isSorted2 (xs++[y]) = (xs) : arrange((y:ys):xss)
                        | otherwise = (xs ++ [y]) : arrange (ys:xss)

amSplit :: Ord a => [a] -> [[a]]
amSplit [] = []
--amSplit xs = listCheck xs : amSplit (drop (length(listCheck xs)) xs)
amSplit xs = arrange (twoSplit xs)