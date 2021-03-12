{-# LANGUAGE DeriveGeneric #-}
--TEMPLATE FILE FOR COURSEWORK 1 for COMP2209
--Julian Rathke, Oct 2019

--EXERCISE A9 ONLY

--CONTAINS FUNCTION REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES


module Exercises (isPossiblePower,Instruction(..),Stack,SMProg) where

import GHC.Generics (Generic,Generic1)
import Control.DeepSeq
import Data.Maybe
import Control.Applicative

data Instruction = Add | Sub | Mul | Div | Dup | Pop deriving (Eq,Ord,Show,Generic)
type Stack = [Maybe Int]
type SMProg = [Instruction] 

instance NFData (Instruction)

sub :: [Maybe Int] -> [Maybe Int]
sub [] = []
sub (x:xs) | isNothing x       = Nothing : tail xs
           | null xs            = error "Wrong input!"
           | isNothing(head xs) = Nothing : tail xs
           | otherwise          = sub1 (fromMaybe 0 x) (fromMaybe 0 (head xs)) : tail xs

sub1 :: Int -> Int -> Maybe Int
sub1 x y = Just (x - y) 

divv :: [Maybe Int] -> [Maybe Int]
divv [] = []
divv (x:xs) | isNothing x        = Nothing : tail xs
            | null xs            = error "Wrong input!"
            | isNothing(head xs) = Nothing : tail xs
            | head xs == Just 0  = Nothing : tail xs 
            | otherwise          = divv1 (fromMaybe 0 x) (fromMaybe 0 (head xs)) : tail xs

divv1 :: Int -> Int -> Maybe Int
divv1 _ 0 = Nothing
divv1 x y = Just (div x y) 


evalInst :: Stack -> SMProg -> Stack
evalInst [] _ = error "Empty Stack"
evalInst xs [] = xs 
evalInst [_] (Add:_) = error "Single element"
evalInst [_] (Sub:_) = error "Single element"
evalInst [_] (Mul:_) = error "Single element"
evalInst [_] (Div:_) = error "Single element"
evalInst (x:y:xs) (Add:ys) = evalInst (summ:xs) ys
                             where summ = liftA2 (+) x  y
evalInst xs (Sub:ys) = evalInst (sub xs) ys
evalInst (x:y:xs) (Mul:ys) = evalInst (mull:xs) ys
                             where mull = liftA2 (*) x  y
evalInst xs (Div:ys) = evalInst (divv xs) ys
evalInst (x:xs) (Dup:ys)   = evalInst (x:x:xs) ys
evalInst xs (Pop:ys)   = evalInst (tail xs) ys


tryVar :: Int -> [SMProg]
tryVar z = mapM (const [Add, Mul, Sub, Div, Pop]) [1..z-1]

findRed :: [SMProg] -> Int -> Stack -> [SMProg] -> [SMProg]
findRed [] _ _ y = y
findRed (x:xs) z s y | z > fromMaybe 0 (head (evalInst s x))  = findRed xs z s y
                 | z == fromMaybe 0 (head (evalInst s x)) = findRed xs z s (x:y)
                 | otherwise                              = findRed xs (fromMaybe 0 (head (evalInst s x))) s [x]



-- Exercise A8

findMaxReducers :: Stack -> [SMProg]
findMaxReducers [] = []
findMaxReducers s = findRed (tryVar (length s)) 0 s []

-- Exercise A9
isPossiblePower :: Int -> Int -> Bool
isPossiblePower 0 0 = False
isPossiblePower _ x | x< 0     = False
isPossiblePower k l | k<l       = False
                    | otherwise = findSec (tryVar2 l) (3^k) [Just 3] 

findSec :: [SMProg] ->  Int -> Stack ->Bool
findSec [] _ _ = True
findSec (x:xs) n s | n == fromMaybe 0 (head (evalInst s x)) = True 
                | otherwise = findSec xs n s

tryVar2 :: Int -> [SMProg]
tryVar2 0 = []
tryVar2 n = filter((== Mul) . last)(filter((== Dup) . head) (sameMulDup (n) (n)))



sameMulDup :: Int -> Int -> [[Instruction]]
sameMulDup 0 d = [replicate d Dup]
sameMulDup d 0 = [replicate d Mul]
sameMulDup m d = do
    x <- [Dup, Mul]
    let (m', d') = case x of
           Dup -> (m  , d-1)
           Mul -> (m-1, d  )
    xs <- sameMulDup m' d'
    return (x:xs)