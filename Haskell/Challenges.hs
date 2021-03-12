{-# LANGUAGE DeriveGeneric #-}
-- comp2209 Functional Programming Challenges
-- Created by Georgi Spasov gts1u19 
-- (c) University of Southampton 2020
-- Skeleton code to be updated with your solutions
-- The dummy functions here simply return an arbitrary value that is usually wrong 

-- DO NOT MODIFY THE FOLLOWING LINES OF CODE
module Challenges (WordSearchGrid,Placement,Posn,Orientation(..),solveWordSearch, createWordSearch,
    LamMacroExpr(..),LamExpr(..),prettyPrint, parseLamMacro,
    cpsTransform,innerRedn1,outerRedn1,compareInnerOuter) where

-- Import standard library and parsing definitions from Hutton 2016, Chapter 13
-- We import System.Random - make sure that your installation has it installed - use stack ghci and stack ghc
import Data.Char
--import Parsing
import Control.Monad
import Data.List
import GHC.Generics (Generic,Generic1)
import Control.DeepSeq
import System.IO
--import System.Random
import Data.Maybe
import Control.Applicative

instance NFData Orientation
instance NFData LamMacroExpr
instance NFData LamExpr

-- types for Part I
type WordSearchGrid = [[ Char ]]
type Placement = (Posn,Orientation)
type Posn = (Int,Int)
data Orientation = Forward | Back | Up | Down | UpForward | UpBack | DownForward | DownBack deriving (Eq,Ord,Show,Read,Generic)

-- types for Parts II and III
data LamMacroExpr = LamDef [ (String,LamExpr) ] LamExpr deriving (Eq,Show,Read,Generic)
data LamExpr = LamMacro String | LamApp LamExpr LamExpr  |
               LamAbs Int LamExpr  | LamVar Int deriving (Eq,Show,Read,Generic)

-- END OF CODE YOU MUST NOT MODIFY



-- ADD YOUR OWN CODE HERE

-- Challenge 1 --

removeItem :: String -> [String] -> [String]
removeItem _ []                 = []
removeItem x (y:ys) | x == y    = removeItem x ys
                    | otherwise = y : removeItem x ys

--findPotential 'H' ["HASKELL","STRING","STACK","MAIN","METHOD"]
findPotential1 :: Char ->[String] -> [String]
findPotential1 _ [] = []
findPotential1 c (x:xs) | c == head x             = x : findPotential1 c xs
                        | otherwise               = findPotential1 c xs

--findPotential 'H' ["HASKELL","STRING","STACK","MAIN","METHOD"]
findPotential2 :: Char ->[String] -> [String]
findPotential2 _ [] = []
findPotential2 c (x:xs) | c == last x  = reverse x : findPotential2 c xs
                        | otherwise     = findPotential2 c xs

--horizontalSearch "TRICK" [ ['T', 'R', 'I', 'C', 'K'], ['E', 'T', 'R','R', 'S'], ['O', 'P', 'I', 'J', 'B'], ['W', 'C', 'Z', 'C', 'P'], ['K', 'T', 'I', 'Q', 'K'] ]
horizontalSearch :: String -> WordSearchGrid -> Bool
horizontalSearch [] _ = True
horizontalSearch _ [] = True 
horizontalSearch (x:xs) (ys:gs) | length ys < length (x:xs) = False
                                | x == head ys              = horizontalSearch xs (tail ys :gs)
                                | otherwise                 = False  


--verticalSearch "TIME" [ ['T', 'A', 'Z', 'D'], ['I', 'Y', 'R','M'], ['M', 'P', 'X', 'J'], ['E', 'X', 'Z', 'D'] ]
verticalSearch :: String -> [Char] -> WordSearchGrid -> Bool
verticalSearch [] _ _ = True
verticalSearch _ [] _ = True
verticalSearch _ _ [] = True
verticalSearch (x:xs) zs (ys:gs) | length (ys:gs) < length (x:xs)     = False
                                 | x == ys !! (length ys - length zs) = verticalSearch xs zs gs
                                 | otherwise                          = False

--diagonalSearch "STACK" [ ['S', 'A', 'Z', 'D', 'F'], ['E', 'T', 'R','M', 'S'], ['O', 'P', 'A', 'J', 'B'], ['W', 'X', 'Z', 'C', 'P'], ['W', 'T', 'I', 'Q', 'K'] ]
diagonalSearch1 :: String -> [Char] -> Int -> WordSearchGrid -> Bool
diagonalSearch1 [] _ _ _ = True
diagonalSearch1 _ [] _ _ = True
diagonalSearch1 _ _ _ [] = True
diagonalSearch1 (x:xs) zs l (ys:gs) | length ys < length (x:xs)                          = False
                                    | length ys < length (ys:gs) && x == head ys         = diagonalSearch1 xs zs l gs
                                    | x == ys !! ((l-length (ys:gs)) + (l - length zs) ) = diagonalSearch1 xs zs l gs
                                    | otherwise                                          = False 

--diagonalSearch2 "TRICK" [ ['S', 'A', 'Z', 'D', 'T'], ['E', 'T', 'R','R', 'S'], ['O', 'P', 'I', 'J', 'B'], ['W', 'C', 'Z', 'C', 'P'], ['K', 'T', 'I', 'Q', 'K'] ]
diagonalSearch2 :: String -> [Char] -> WordSearchGrid -> Bool
diagonalSearch2 [] _ _ = True
diagonalSearch2 _ [] _ = True
diagonalSearch2 _ _ [] = True
diagonalSearch2 (x:xs) zs (ys:gs) | length (ys:gs)+1 - length zs < length (x:xs) = False
                                  | length ys < length (ys:gs) && x == head ys   = diagonalSearch2 xs zs gs
                                  | x == ys !! (length (ys:gs) - length zs)      = diagonalSearch2 xs zs gs
                                  | otherwise                                    = False

testWord :: [String] -> [String]-> WordSearchGrid ->(String, Maybe Orientation)
testWord [] [] _ = ("",Nothing)  
testWord _ _ [] = ("",Nothing)
testWord (x:xs) [] (ys:gs) | horizontalSearch x (ys:gs)                   = (x,Just Forward)
                           | verticalSearch x ys (ys:gs)                  = (x,Just Down)
                           | diagonalSearch1 x ys (length(ys:gs)) (ys:gs) = (x,Just DownForward)
                           | diagonalSearch2 x ys (ys:gs)                 = (x,Just DownBack)
                           |otherwise                                     = testWord xs [] (ys:gs)

testWord [] (z:zs) (ys:gs) | horizontalSearch z (ys:gs)                   = (reverse z,Just Back)
                           | verticalSearch z ys (ys:gs)                  = (reverse z,Just Up)
                           | diagonalSearch1 z ys (length(ys:gs)) (ys:gs) = (reverse z,Just UpBack)
                           | diagonalSearch2 z ys (ys:gs)                 = (reverse z,Just UpForward)
                           | otherwise                                    = testWord [] zs (ys:gs) 

testWord (x:xs) (z:zs) (ys:gs) | horizontalSearch x (ys:gs)                   = (x,Just Forward)
                               | verticalSearch x ys (ys:gs)                  = (x,Just Down)
                               | diagonalSearch1 x ys (length(ys:gs)) (ys:gs) = (x,Just DownForward)
                               | diagonalSearch2 x ys (ys:gs)                 = (x,Just DownBack)
                               | horizontalSearch z (ys:gs)                   = (reverse z,Just Back)
                               | verticalSearch z ys (ys:gs)                  = (reverse z,Just Up)
                               | diagonalSearch1 z ys (length(ys:gs)) (ys:gs) = (reverse z,Just UpBack)
                               | diagonalSearch2 z ys (ys:gs)                 = (reverse z,Just UpForward)
                               | otherwise                                    = testWord xs zs (ys:gs) 

testCon :: Char -> Int-> Int->Int-> WordSearchGrid -> Posn
testCon  _ _ _ _ [] = (0,0)
testCon c a b l ([]:gs) = testCon c (a-l) (b+1) l gs
testCon c a b l (ys:gs) | c == head ys = (a, b)
                        | otherwise = testCon c (a+1) b l (tail ys:gs)
                      



solveFirst :: [ String ] -> Int-> Int->Int-> WordSearchGrid -> [ (String, Maybe Placement) ]
solveFirst [] _ _ _ _ = []
solveFirst _ _ _ _ [] = []
solveFirst xs a b l ([]:gs) = solveFirst xs (a-l) (b+1) l gs
solveFirst xs a b l (ys:gs) | null (findPotential1 (head ys) xs) && null (findPotential2(head ys) xs) = solveFirst xs (a+1) b l (tail ys:gs)
                            | isNothing (snd (testWord (findPotential1 (head ys) xs) (findPotential2(head ys) xs) (ys:gs))) = solveFirst xs (a+1) b l (tail ys:gs)
                            | otherwise = (fst(testWord (findPotential1 (head ys) xs) (findPotential2(head ys) xs) (ys:gs)), Just((a,b), fromMaybe Forward (snd (testWord (findPotential1 (head ys) xs) (findPotential2(head ys) xs) (ys:gs))) )) : solveFirst (removeItem (fst(testWord (findPotential1 (head ys) xs) (findPotential2(head ys) xs) (ys:gs))) xs) (a+1) b l (tail ys:gs)


solveWordSearch :: [ String ] -> WordSearchGrid -> [ (String, Maybe Placement) ]
solveWordSearch [] _ = []
solveWordSearch _ [] = []
solveWordSearch xs gs = solveFirst xs 0 0 (length gs) gs

-- Two examples for you to try out, the first of which is in the instructions

exGrid1'1 = [ "HAGNIRTSH" , "SACAGETAK", "GCSTACKEL","MGHKMILKI","EKNLETGCN","TNIRTLETE","IRAAHCLSR","MAMROSAGD","GIZKDDNRG" ] 
exWords1'1 = [ "HASKELL","STRING","STACK","MAIN","METHOD"]

exGrid1'2 = ["ROBREUMBR","AURPEPSAN","UNLALMSEE","YGAUNPYYP","NLMNBGENA","NBLEALEOR","ALRYPBBLG","NREPBEBEP","YGAYAROMR"]
exWords1'2 = [ "BANANA", "ORANGE", "MELON", "RASPBERRY","APPLE","PLUM","GRAPE" ]


-- Challenge 2 --

--get all letters from a list of words into a letter list
getSymbols :: [String] -> [Char]
getSymbols [] = []
getSymbols ([]:xs) = getSymbols xs
getSymbols (x:xs) = head x : getSymbols (tail x:xs)

--using the density and the length of letters to find the size of the puzzle
findGridSize:: [Char] -> Double  -> Int
findGridSize  _ n | n < 0 && n > 1 = error "Invalid density"
findGridSize xs n = round (sqrt (fromIntegral (length xs) / n))

--create a starting puzzle with empty symbols
grid :: Int -> Int -> Char -> [[Char]]
grid x y = replicate y . replicate x

--filling all empty symbols with letters from the letter list
fillEmpty :: [[Char]] -> Int -> [Char] -> [[Char]]
fillEmpty [] _ _ = []
fillEmpty (g:gs) n cs = replace g (cs !! n) : fillEmpty gs n cs 


--replace an empty symbol with a letter from the letter list
replace :: [Char] -> Char -> [Char]
replace [] _ = []
replace (y:ys) c | y == 'O'  = c : replace ys c
                 | otherwise = y : replace ys c


createWordSearch :: [ String ] -> Double -> IO WordSearchGrid
createWordSearch _ _ = return []


--- Convenience functions supplied for testing purposes
createAndSolve :: [ String ] -> Double -> IO [ (String, Maybe Placement) ]
createAndSolve words maxDensity =   do g <- createWordSearch words maxDensity
                                       let soln = solveWordSearch words g
                                       printGrid g
                                       return soln

printGrid :: WordSearchGrid -> IO ()
printGrid [] = return ()
printGrid (w:ws) = do putStrLn w
                      printGrid ws



-- Challenge 3 --

--print variables
printVar :: LamExpr -> String 
printVar (LamVar x) = "x" ++ show x
printVar _          = error "wrong input"

--print lamAbs expressions
printAbs ::LamExpr -> String
printAbs (LamAbs x (LamVar n))     = "/x" ++ show x ++ " -> " ++ printVar (LamVar n)
printAbs (LamAbs x (LamApp e1 e2)) = "/x" ++ show x ++ " -> " ++ printApp (LamApp e1 e2)
printAbs (LamAbs x (LamMacro t)) = "/x" ++ show x ++ " -> " ++ t
printAbs _                         = error "wrong input"

--printApp expressions
printApp :: LamExpr -> String
--printApp (LamApp (LamAbs x e) (LamAbs y i)) = "("++printAbs (LamAbs x e) ++ ")" ++ " " ++ printAbs (LamAbs y i)
printApp (LamApp (LamAbs x e) (LamVar y))   = printAbs (LamAbs x e) ++ " " ++ printVar (LamVar y)
printApp (LamApp (LamAbs x e) (LamMacro t)) = printAbs (LamAbs x e) ++ " " ++  t
printApp (LamApp (LamMacro t) (LamAbs x e) )= t ++ " " ++ printAbs (LamAbs x e) 
printApp (LamApp (LamVar y) (LamAbs x e))   = printVar (LamVar y) ++ " " ++ printAbs (LamAbs x e)
printApp (LamApp (LamVar x) (LamVar y))     = printVar (LamVar x) ++ " " ++ printVar (LamVar y)
printApp (LamApp (LamVar x) (LamMacro s))   = printVar (LamVar x) ++ " " ++ s
printApp (LamApp (LamMacro s) (LamVar x))   = s ++ " " ++ printVar (LamVar x)
printApp (LamApp (LamMacro s) (LamMacro t)) = s ++ " " ++ t
printApp (LamApp (LamApp e1 e2) e)          = printApp(LamApp e1 e2) ++ " " ++ printAll e
printApp (LamApp e (LamApp e1 e2))          = printAll e ++ " " ++ printApp(LamApp e1 e2) 
printApp _                                  = error "Wrong input"




--printing lambda expressions
printAll :: LamExpr -> String
printAll (LamVar x) = printVar (LamVar x)
printAll (LamAbs x e) = printAbs (LamAbs x e)
printAll (LamApp e1 e2) = printApp (LamApp e1 e2)
printAll _ = error "Wrong Input"

--printing normal expressions
printMacro1 :: LamMacroExpr -> String
printMacro1 (LamDef [] e) = printAll e
printMacro1 (LamDef (x:xs) e) = "def " ++ fst x ++ " = " ++ printAll (snd x) ++ " in " ++ printMacro1 (LamDef xs e)

--printing only the macros
printMacro2 :: LamMacroExpr -> String
printMacro2(LamDef [] e) = []
printMacro2 (LamDef (x:xs) e)  = "def " ++ fst x ++ " = " ++ printAll (snd x) ++ " in " ++ printMacro2 (LamDef xs e)

--putting the macros in a tripple
collectMacro :: LamMacroExpr -> [(String, String, String)]
collectMacro (LamDef [] _) = []
collectMacro (LamDef (x:xs) e) = (fst x,  printAll (snd x), printAll e) : collectMacro (LamDef xs e)


--replacing a string with another string
rep :: String -> String ->String ->String
rep a b s@(x:xs) = if a `isPrefixOf` s
                     then b++rep a b (drop (length a) s)
                     else x:rep a b xs
rep _ _ [] = []

--replacing an expression with a defined macro name
checkMacroExpr :: [(String, String, String)]  -> String
checkMacroExpr []  = ""
checkMacroExpr ((x, y, z):xs)  | y `isInfixOf` z = rep y x z
                           | otherwise = checkMacroExpr xs

--printing everything
prettyPrint :: LamMacroExpr -> String
prettyPrint (LamDef xs e) | length(checkMacroExpr (collectMacro (LamDef xs e))) == 0 = printMacro1 (LamDef xs e)
                          | otherwise = printMacro2 (LamDef xs e) ++checkMacroExpr (collectMacro (LamDef xs e))

-- examples in the instructions
ex3'1 = LamDef [] (LamApp (LamAbs 1 (LamVar 1)) (LamAbs 1 (LamVar 1)))
ex3'2 = LamDef [] (LamAbs 1 (LamApp (LamVar 1) (LamAbs 1 (LamVar 1))))
ex3'3 = LamDef [ ("F", LamAbs 1 (LamVar 1) ) ] (LamAbs 2 (LamApp (LamVar 2) (LamMacro "F")))
ex3'4 = LamDef [ ("F", LamAbs 1 (LamVar 1) ) ] (LamAbs 2 (LamApp (LamAbs 1 (LamVar 1)) (LamVar 2))) 


-- Challenge 4 --

--creating the parser
newtype Parser a = P { parse :: String -> [(a,String)] }

--creating the default parser functions (did not know that there was a Parsing.hs file and created them myself)
bind :: Parser a -> (a -> Parser b) -> Parser b
bind p f = P $ \s -> concatMap (\(a, s') -> parse (f a) s') $ parse p s

unit :: a -> Parser a
unit a = P (\s -> [(a,s)])

instance Functor Parser where
  fmap f (P cs) = P (\s -> [(f a, b) | (a, b) <- cs s])

instance Applicative Parser where
  pure = return
  (P cs1) <*> (P cs2) = P (\s -> [(f a, s2) | (f, s1) <- cs1 s, (a, s2) <- cs2 s1])

instance Monad Parser where
  return = unit
  (>>=)  = bind

--getting a single item
itemm :: Parser Char
itemm = P (\inp -> case inp of
                     []     -> []
                     (x:xs) -> [(x,xs)])

(<||>) :: Parser a -> Parser a -> Parser a
p <||> q = P (\inp -> case parse p inp of
             [] -> parse q inp
             [(v, out)] -> [(v, out)])


many0 :: Parser a -> Parser [a]
many0 p = many1 p <||> unit []

--getting many items
many1 :: Parser a -> Parser [a]
many1 p = do v <- p
             vs <- many0 p
             return (v:vs)

--getting some items
some1 :: Parser a -> Parser [a]
some1 = many1 

--satisfy a predicate
sat :: (Char -> Bool) -> Parser Char
sat p = itemm `bind` \c ->
  if p c
  then unit c
  else (P (\cs -> []))

--check if a char exist 
char :: Char -> Parser Char
char x = sat (x==)

--Digit ::= “0” | “1” | “2” | “3” | “4” | “5” | “6” | “7” | “8” | “9”
digitt :: Parser Char
digitt = sat isDigit

--Digits ::= Digit | Digit Digits
nat :: Parser Int
nat = do ds <- some1 digitt
         return (read ds)

--UChar ::= "A" | "B" | ... | "Z"
letter :: Parser Char
letter = sat isUpper

--parsing the macro name for LamDef [ (String , LamExpr) ]
macroString :: Parser String
macroString = do ds <-some1 letter
                 return ds

--MacroName ::= UChar | UChar MacroName
macroName :: Parser LamExpr
macroName = do ds <- some1 letter
               return (LamMacro ds)

--Parsing Lambda Expr

--Var ::= “x” Digits
var1 :: Parser LamExpr
var1 =   do char 'x'
            LamVar <$> nat
--parsing lamApp expr LamApp LamExpr LamExpr
lamApp :: Parser LamExpr
lamApp = do {e1 <- expr2; rest e1}
        where rest e1 = (do char ' '
                            e2 <- expr2
                            rest(LamApp e1 e2))
                            <||> return e1

--parsing lamAbs expr LamAbs Int LamExpr
lamAbs :: Parser LamExpr
lamAbs = do char '\\'
            char 'x'
            n <- nat
            char ' '
            char '-'
            char '>'
            char ' '
            e <- expr1
            return (LamAbs n e)



--parsing brackets
brackets :: Parser LamExpr
brackets = do char '('
              e <-expr1
              char ')'
              return e

--Parsing and checking LamMacroExpr

--parsing MacroExpr ::= "def" MacroName "=" Expr "in"
macroExpr :: Parser (String, LamExpr)
macroExpr = do char 'd'
               char 'e'
               char 'f'
               char ' '
               m <- macroString
               char ' '
               char '='
               char ' '
               e1 <- lamApp
               char ' '
               char 'i'
               char 'n'
               char ' '
               return (m, e1)


--parsing the macroBody 
macroBody :: Parser [(String, LamExpr)]
macroBody = do xs <- some1 macroExpr
               return xs

--check if there are macros with the same name
checkMacro :: [(String, LamExpr)] ->Bool
checkMacro xs | nub ( map fst xs) ==map fst xs = True
              | otherwise = False

--parsing the macro expr while doing the checks
parseMacro :: Parser LamMacroExpr
parseMacro = do {m<-macroBody; check m}
                 where check m = ( if checkMacro m  
                                   then do e <-lamApp
                                           return (LamDef (m) e) else (P (\cs -> [])))

--check if there are any macros and parse
finalCheck :: Parser LamMacroExpr
finalCheck = parseMacro
             <||> do e <- lamApp
                     return (LamDef [] e)  

--putting everything together 
--Expr ::= Var | MacroName | Expr Expr | “\” Var “->” Expr | “(“ Expr “)”
expr1 :: Parser LamExpr
expr1 =
        lamAbs <||>
        brackets <||>
        lamApp <||>
        var1   
  
expr2 :: Parser LamExpr
expr2 = lamAbs <||>
        brackets <||>
        var1 <||>
        macroName 

--running the parser with everything
runParser2 :: Parser a -> String -> Maybe a
runParser2 m s =
  case parse m s of
    [(res, [])] -> Just res 
    [(_, rs)]   -> Nothing
    _           -> Nothing

--final function 
parseLamMacro :: String -> Maybe LamMacroExpr
parseLamMacro = runParser2 finalCheck



-- Challenge 5 
--getting a variable from the different expr
lambdaVar :: LamExpr -> Int
lambdaVar (LamVar n) = n
lambdaVar (LamAbs n _) = n
lambdaVar (LamMacro _) = 78
lambdaVar (LamApp e1 _) = lambdaVar e1

--transforming the expr into cps
lambdaTransform :: LamExpr -> LamExpr
lambdaTransform (LamVar n) = let k = 45 in LamAbs k (LamApp (LamVar k) (LamVar n))
lambdaTransform (LamAbs n e) = let k = 49 in LamAbs k (LamApp (LamVar k) (LamAbs n (lambdaTransform e)))
lambdaTransform (LamApp e1 e2) = let k = 78
                                     f = 81
                                     e = 94 
                                 in LamAbs k (LamApp (lambdaTransform e1) (LamAbs f (LamApp (lambdaTransform e2) (LamAbs e (LamApp (LamApp (LamVar f) (LamVar e)) (LamVar k))))))
lambdaTransform (LamMacro x) = LamMacro x

--transforming the macroexpr into cps
lamMacroTransform :: [(String, LamExpr)] -> [(String, LamExpr)]
lamMacroTransform [] = []
lamMacroTransform ((s, e):xs) = (s, lambdaTransform e) : lamMacroTransform xs

--putting everything together
cpsTransform :: LamMacroExpr -> LamMacroExpr
cpsTransform (LamDef m e) = LamDef (lamMacroTransform m) (lambdaTransform e)


-- Examples in the instructions
exId =  (LamAbs 1 (LamVar 1))
ex5'1 = (LamApp (LamVar 1) (LamVar 2))
ex5'2 = (LamDef [ ("F", exId) ] (LamVar 2) )
ex5'3 = (LamDef [ ("F", exId) ] (LamMacro "F") )
ex5'4 = (LamDef [ ("F", exId) ] (LamApp (LamMacro "F") (LamMacro "F")))


-- Challenge 6

innerRedn1 :: LamMacroExpr -> Maybe LamMacroExpr
innerRedn1 _ = Nothing

outerRedn1 :: LamMacroExpr -> Maybe LamMacroExpr
outerRedn1 _ = Nothing

compareInnerOuter :: LamMacroExpr -> Int -> (Maybe Int,Maybe Int,Maybe Int,Maybe Int)
compareInnerOuter _ _ = (Nothing,Nothing,Nothing,Nothing) 

-- Examples in the instructions

-- (\x1 -> x1 x2)
ex6'1 = LamDef [] (LamAbs 1 (LamApp (LamVar 1) (LamVar 2)))

--  def F = \x1 -> x1 in F  
ex6'2 = LamDef [ ("F",exId) ] (LamMacro "F")

--  (\x1 -> x1) (\x2 -> x2)   
ex6'3 = LamDef [] ( LamApp exId (LamAbs 2 (LamVar 2)))

--  (\x1 -> x1 x1)(\x1 -> x1 x1)  
wExp = (LamAbs 1 (LamApp (LamVar 1) (LamVar 1)))
ex6'4 = LamDef [] (LamApp wExp wExp)

--  def ID = \x1 -> x1 in def FST = (\x1 -> λx2 -> x1) in FST x3 (ID x4) 
ex6'5 = LamDef [ ("ID",exId) , ("FST",LamAbs 1 (LamAbs 2 (LamVar 1))) ] ( LamApp (LamApp (LamMacro "FST") (LamVar 3)) (LamApp (LamMacro "ID") (LamVar 4)))

--  def FST = (\x1 -> λx2 -> x1) in FST x3 ((\x1 ->x1) x4))   
ex6'6 = LamDef [ ("FST", LamAbs 1 (LamAbs 2 (LamVar 1)) ) ]  ( LamApp (LamApp (LamMacro "FST") (LamVar 3)) (LamApp (exId) (LamVar 4)))

-- def ID = \x1 -> x1 in def SND = (\x1 -> λx2 -> x2) in SND ((\x1 -> x1 x1) (\x1 -> x1 x1)) ID
ex6'7 = LamDef [ ("ID",exId) , ("SND",LamAbs 1 (LamAbs 2 (LamVar 2))) ]  (LamApp (LamApp (LamMacro "SND") (LamApp wExp wExp) ) (LamMacro "ID") ) 