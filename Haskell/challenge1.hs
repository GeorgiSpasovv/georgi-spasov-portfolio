import Data.List
import Data.Maybe

type WordSearchGrid = [ [ Char ] ] 
type Placement = (Posn,Orientation)
type Posn = (Int,Int) 
data Orientation = Forward | Back | Up | Down | UpForward | UpBack | DownForward | DownBack deriving (Eq,Ord,Show,Read)

--removing a certain string from a list
removeItem :: String -> [String] -> [String]
removeItem _ []                 = []
removeItem x (y:ys) | x == y    = removeItem x ys
                    | otherwise = y : removeItem x ys

--find the words from the list which start with a certain letter in order to begin searching for the whole word in the puzzle
findPotential1 :: Char ->[String] -> [String]
findPotential1 _ [] = []
findPotential1 c (x:xs) | c == head x             = x : findPotential1 c xs
                        | otherwise               = findPotential1 c xs

--find the words that end with a certain letter in order to begin searching for the whole word in the puzzle
findPotential2 :: Char ->[String] -> [String]
findPotential2 _ [] = []
findPotential2 c (x:xs) | c == last x  = reverse x : findPotential2 c xs
                        | otherwise     = findPotential2 c xs

--search for a word horizontally
horizontalSearch :: String -> WordSearchGrid -> Bool
horizontalSearch [] _ = True
horizontalSearch _ [] = True 
horizontalSearch (x:xs) (ys:gs) | length ys < length (x:xs) = False
                                | x == head ys              = horizontalSearch xs (tail ys :gs)
                                | otherwise                 = False  


--search for a word vertically
verticalSearch :: String -> [Char] -> WordSearchGrid -> Bool
verticalSearch [] _ _ = True
verticalSearch _ [] _ = True
verticalSearch _ _ [] = True
verticalSearch (x:xs) zs (ys:gs) | length (ys:gs) < length (x:xs)     = False
                                 | x == ys !! (length ys - length zs) = verticalSearch xs zs gs
                                 | otherwise                          = False

--search for a word forward down
diagonalSearch1 :: String -> [Char] -> Int -> WordSearchGrid -> Bool
diagonalSearch1 [] _ _ _ = True
diagonalSearch1 _ [] _ _ = True
diagonalSearch1 _ _ _ [] = True
diagonalSearch1 (x:xs) zs l (ys:gs) | length ys < length (x:xs)                          = False
                                    | length ys <= length (ys:gs) && x == head ys         = diagonalSearch1 xs zs l gs
                                    | length ys > length (ys:gs)                         = False
                                    | x == ys !! ((l-length (ys:gs)) + (l - length zs) ) = diagonalSearch1 xs zs l gs
                                    | otherwise                                          = False 

----search for a word back down
diagonalSearch2 :: String -> [Char] -> WordSearchGrid -> Bool
diagonalSearch2 [] _ _ = True
diagonalSearch2 _ [] _ = True
diagonalSearch2 _ _ [] = True
diagonalSearch2 (x:xs) zs (ys:gs) | length (ys:gs)+1 - length zs < length (x:xs) = False
                                  | length ys < length (ys:gs) && x == head ys   = diagonalSearch2 xs zs gs
                                  | x == ys !! (length (ys:gs) - length zs)      = diagonalSearch2 xs zs gs
                                  | otherwise                                    = False

--test the words to find if they are in the puzzle and their direction
testWord :: [String] -> [String]-> WordSearchGrid ->(String, Maybe Orientation)
testWord [] [] _ = ("",Nothing)  
testWord _ _ [] = ("",Nothing)
--searching for forward and down words
testWord (x:xs) [] (ys:gs) | horizontalSearch x (ys:gs)                   = (x,Just Forward)
                           | verticalSearch x ys (ys:gs)                  = (x,Just Down)
                           | diagonalSearch1 x ys (length(ys:gs)) (ys:gs) = (x,Just DownForward)
                           | diagonalSearch2 x ys (ys:gs)                 = (x,Just DownBack)
                           |otherwise                                     = testWord xs [] (ys:gs)
--searching for back and up words
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


--putting exerything together and finding the full placement of the words
solveFirst :: [ String ] -> Int-> Int->Int-> WordSearchGrid -> [ (String, Maybe Placement) ]
solveFirst [] _ _ _ _ = []
solveFirst _ _ _ _ [] = []
solveFirst _ a b l _ | a == l-1 && b == l-1 = []
solveFirst xs a b l ([]:gs) = solveFirst xs (a-l) (b+1) l gs
solveFirst xs a b l (ys:gs) | null (findPotential1 (head ys) xs) && null (findPotential2(head ys) xs) = solveFirst xs (a+1) b l (tail ys:gs)
                            | isNothing (snd (testWord (findPotential1 (head ys) xs) (findPotential2(head ys) xs) (ys:gs))) = solveFirst xs (a+1) b l (tail ys:gs)
                            | otherwise = (fst(testWord (findPotential1 (head ys) xs) (findPotential2(head ys) xs) (ys:gs)), Just((a,b), fromMaybe Forward (snd (testWord (findPotential1 (head ys) xs) (findPotential2(head ys) xs) (ys:gs))) )) : solveFirst (removeItem (fst(testWord (findPotential1 (head ys) xs) (findPotential2(head ys) xs) (ys:gs))) xs) (a+1) b l (tail ys:gs)


--running solveFirst function
solveWordSearch :: [ String ] -> WordSearchGrid -> [ (String, Maybe Placement) ]
solveWordSearch [] _ = []
solveWordSearch _ [] = []
solveWordSearch xs gs = solveFirst xs 0 0 (length gs) gs