--TEMPLATE FILE FOR COURSEWORK 1 for COMP2209
--Julian Rathke, Oct 2020

--EXERCISE A2 ONLY

--CONTAINS FUNCTION REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES


module Exercises (approxPi) where

-- Exercise A2
facDiv:: Int -> Double
facDiv n = fromRational(factorial (fromIntegral(n-1)))/ fromRational(doubleFac (fromIntegral(2*(n-1)+1)))

append :: Int -> [Double] -> [Double]
append n xs | n==1       = (facDiv n ): xs
            | otherwise  = append (n-1) (facDiv n: xs)

approxPi :: Int -> Double
approxPi n | n<=0      = error "bad Input"
           | otherwise = 2*sum(append n [])

factorial :: Rational -> Rational
factorial n = product [1..n]

doubleFac :: Rational -> Rational
doubleFac 0 = 1
doubleFac 1 = 1
doubleFac n = n * doubleFac(n-2)


