factorial :: Int -> Int
factorial n = product [1..n]

doubleFac :: Int -> Int
doubleFac k | k `mod` 2 == 0 = product [x| x <- [1..k], x `mod` 2 == 0]
            | otherwise      = product [x| x <- [1..k], x `mod` 2 /= 0]

append :: Int -> [Double] -> [Double]
append n xs | n==2       = (fromIntegral(factorial (n-1))/ fromIntegral(doubleFac (2*(n-1)+1) )): xs
            | otherwise  = append (n-1) ((fromIntegral(factorial (n-1))/ fromIntegral(doubleFac (2*(n-1)+1) )): xs)

approxPi :: Int -> Double
approxPi n = sum(append n []);