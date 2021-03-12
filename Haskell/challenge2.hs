
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
