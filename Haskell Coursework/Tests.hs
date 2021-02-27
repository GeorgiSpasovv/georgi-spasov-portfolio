-- comp2209 Functional Programming Challenges
-- Created by Georgi Spasov gts1u19 
-- (c) University of Southampton 2020
import Challenges

assert :: Bool -> String -> String -> IO()
assert test passInfo failInfo = if test then putStrLn passInfo else putStrLn failInfo

--Test1
t1c1 = ["HAPPY","GOOD","BIRD","PEN"]
t1c2 = ["IGYRGYC","AAGOODR","LRYXDXE","MGPPRSD","VLPEIPR","WPANBIJ","VLHCCUL"]

t1c4 = [ "HASKELL","STRING","STACK","MAIN","METHOD"]
t1c5 = [ "HAGNIRTSH" , "SACAGETAK", "GCSTACKEL","MGHKMILKI","EKNLETGCN","TNIRTLETE","IRAAHCLSR","MAMROSAGD","GIZKDDNRG" ] 
t1c6 = [("HASKELL",Just((0,0),DownForward)),("STRING",Just((7,0),Back)),("STACK",Just((2,2),Forward)),("MAIN",Just((2,7),Up)),("METHOD",Just((4,3),Down))]

t1c7 = []
t1c8 = ["IGYRGYC","AAGOODR","LRYXDXE","MGPPRSD","VLPEIPR","WPANBIJ","VLHCCUL"]
t1c9 = []

--Test2


--Test3
t3c1 = (LamDef [] (LamApp (LamAbs 2 (LamVar 2)) (LamAbs 3 (LamVar 3))) )
t3c2 = "(\\x2 -> x2) \\x3 -> x3" 

t3c3 = (LamDef [ ("G", LamAbs 1 (LamApp (LamVar 1) (LamVar 2))) ] (LamApp (LamAbs 1 (LamVar 1)) (LamMacro "G")))
t3c4 = "def G = \\x1 -> x1 x2 in \\x1 -> x1 G"

t3c5 = ((LamDef [("F",LamAbs 1 (LamVar 2))] (LamAbs 1 (LamAbs 1 (LamVar 2)))))
t3c6 = "def F = \\x1 -> x2 in \\x1 -> F"

t3c7 = (LamDef [("F",LamAbs 1 (LamVar 1)),("G",LamVar 1)] (LamAbs 2 (LamApp (LamMacro "F") (LamMacro "G"))))

t3c8 = "def F = /x1 -> x1 in def G = x1 in /x2 -> F G"

--Test 4
t4c1 = ""

t4c2 = "x1 x2 x3"
t4c3 = Just (LamDef [] (LamApp (LamApp (LamVar 1) (LamVar 2)) (LamVar 3)))

t4c4 = "\\x1 -> F x3"
t4c5 = Just (LamDef [] (LamAbs 1 (LamApp (LamMacro "F") (LamVar 3))))

t4c6 = "def F = \\x1 -> x1 x2 in \\x2 -> x2 F"
t4c7 = Just (LamDef [("F",LamAbs 1 (LamApp (LamVar 1) (LamVar 2)))] (LamAbs 2 (LamApp (LamVar 2) (LamMacro "F"))))

t4c8 = "def G = \\x1 -> x1 in def G = x1 in \\x2 -> x2"


t4c9 = "def F = \\x1 -> x1 def G = x1 x2 in x1"




testSolveWordSearch :: [String] -> WordSearchGrid -> [ (String, Maybe Placement) ] -> Bool
testSolveWordSearch words grid answer = solveWordSearch words grid == answer


--testCreateWordSearch :: [String] -> Double -> Bool
--testCreateWordSearch words density answer = (testCreate createAndSolve words density)

testCreate :: [ (String, Maybe Placement) ] -> Bool
testCreate [] = True
testCreate (x:xs) | snd x == Nothing  = False
                  | otherwise = testCreate xs

testPrintMacro :: LamMacroExpr -> String -> Bool
testPrintMacro input output = prettyPrint input == output

testParseMacro :: String -> Maybe LamMacroExpr -> Bool
testParseMacro s expr1 = parseLamMacro s == expr1 


main :: IO ()
main = do
    putStrLn "====Challenge1===="
    putStrLn "====Test Start===="
    assert (testSolveWordSearch t1c4 t1c5 t1c6) "Normal test passed" "Normal test failed"
    assert (testSolveWordSearch t1c7 t1c8 t1c9) "Empty String passed" "Empty String failed"
    putStrLn "====Test Ends===="

    putStrLn "====Challenge2===="
    putStrLn "====Test Start===="
    putStrLn "====Test Ends===="

    putStrLn "====Challenge3===="
    putStrLn "====Test Start===="
    assert (testPrintMacro t3c1 t3c2) "Expr no Macro passed" "Expr no Macro failed" 
    assert (testPrintMacro t3c3 t3c4) "Expr with Macro replacement passed" "Expr with Macro replacement failed"
    assert (testPrintMacro t3c5 t3c6) "Expr with Macro passed" "Expr with Macro failed"
    assert (testPrintMacro t3c7 t3c8) "Expresion with several Macros passed" "Expression with Several Macros failed"
    putStrLn "====Test Ends===="

    putStrLn "====Challenge4===="
    putStrLn "====Test Start===="
    assert (testParseMacro t4c1 Nothing) "Check Empty string passed" "Check Empty string failed"
    assert (testParseMacro t4c2 t4c3) "Check No Macro Expr passed" "Check No Macro Expr failed"
    assert (testParseMacro t4c4 t4c5) "Check Macro Name in Expr passed" "Check Macro Name in Expr failed"
    assert (testParseMacro t4c6 t4c7) "Check Macro Expr passed" "Check Macro Expr failed"
    assert (testParseMacro t4c8 Nothing) "Check Repeated Macro passed" "Check Repeated Macro failed"
    assert (testParseMacro t4c9 Nothing) "Check Wrong grammar passed" "Check Wrong grammar failed "
    putStrLn "====Test Ends===="


