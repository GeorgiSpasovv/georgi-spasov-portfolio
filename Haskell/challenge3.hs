import Data.List
import Data.Maybe

data LamMacroExpr = LamDef [ (String , LamExpr) ] LamExpr deriving (Eq,Show,Read)

data LamExpr = LamMacro String | LamApp LamExpr LamExpr | LamAbs Int LamExpr | LamVar Int deriving (Eq, Show, Read)

--print variables
printVar :: LamExpr -> String 
printVar (LamVar x) = "x" ++ show x
printVar _          = error "wrong input"

--print lamAbs expressions
printAbs ::LamExpr -> String
printAbs (LamAbs x (LamVar n))     = "/x" ++ show x ++ " -> " ++ printVar (LamVar n)
printAbs (LamAbs x (LamApp e1 e2)) = "/x" ++ show x ++ " -> " ++ printApp (LamApp e1 e2)
printAbs _                         = error "wrong input"

--printApp expressions
printApp :: LamExpr -> String
printApp (LamApp (LamAbs x e) (LamAbs y i)) = "("++printAbs (LamAbs x e) ++ ")" ++ " " ++ printAbs (LamAbs y i)
printApp (LamApp (LamAbs x e) (LamVar y))   = printAbs (LamAbs x e) ++ " " ++ printVar (LamVar y)
printApp (LamApp (LamVar y) (LamAbs x e))   = printVar (LamVar y) ++ " " ++ printAbs (LamAbs x e)
printApp (LamApp (LamVar x) (LamVar y))     = printVar (LamVar x) ++ " " ++ printVar (LamVar y)
printApp (LamApp (LamVar x) (LamMacro s))   = printVar (LamVar x) ++ " " ++ s
printApp (LamApp (LamMacro s) (LamVar x))   = s ++ " " ++ printVar (LamVar x)
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



