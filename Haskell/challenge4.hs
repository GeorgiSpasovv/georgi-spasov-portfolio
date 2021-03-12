{-# LANGUAGE LambdaCase #-}
{-# OPTIONS_GHC -fno-warn-unused-do-bind #-}
import Data.Char
import Control.Monad
import Control.Applicative
import Data.List

data LamMacroExpr = LamDef [ (String , LamExpr) ] LamExpr deriving (Eq,Show,Read)
data LamExpr = LamMacro String | LamApp LamExpr LamExpr | LamAbs Int LamExpr | LamVar Int deriving (Eq, Show, Read)

--MacroExpr ::= "def" MacroName "=" Expr "in" MacroExpr | Expr
--Expr ::= Var | MacroName | Expr Expr | “\” Var “->” Expr | “(“ Expr “)”
--MacroName ::= UChar | UChar MacroName
--UChar ::= "A" | "B" | ... | "Z"
--Var ::= “x” Digits
--Digits ::= Digit | Digit Digits
--Digit ::= “0” | “1” | “2” | “3” | “4” | “5” | “6” | “7” | “8” | “9”

newtype Parser a = P { parse :: String -> [(a,String)] }
--newType Parser a = P ( String -> [(a, String)])

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



item :: Parser Char
item = P $ \case
  [] -> []
  (c : cs) -> [(c, cs)]


(<||>) :: Parser a -> Parser a -> Parser a
p <||> q = P (\inp -> case parse p inp of
             [] -> parse q inp
             [(v, out)] -> [(v, out)])






many0 :: Parser a -> Parser [a]
many0 p = many1 p <||> unit []

many1 :: Parser a -> Parser [a]
many1 p = do v <- p
             vs <- many0 p
             return (v:vs)

some1 :: Parser a -> Parser [a]
some1 = many1 

sat :: (Char -> Bool) -> Parser Char
sat p = item `bind` \c ->
  if p c
  then unit c
  else (P (\cs -> []))

char :: Char -> Parser Char
char x = sat (x==)

--Digit ::= “0” | “1” | “2” | “3” | “4” | “5” | “6” | “7” | “8” | “9”
digit :: Parser Char
digit = sat isDigit

--Digits ::= Digit | Digit Digits
nat :: Parser Int
nat = do xs <- some1 digit
         return (read xs)

--UChar ::= "A" | "B" | ... | "Z"
letter :: Parser Char
letter = sat isUpper

macroString :: Parser String
macroString = do ds <-some1 letter
                 return ds

--MacroName ::= UChar | UChar MacroName
macroName :: Parser LamExpr
macroName = do ds <- some1 letter
               return (LamMacro ds)

--Var ::= “x” Digits
var1 :: Parser LamExpr
var1 =   do char 'x'
            LamVar <$> nat

chainl1 :: Parser LamExpr
chainl1 = do {e1 <- expr2; rest e1}
        where rest e1 = (do char ' '
                            e2 <- expr2
                            rest(LamApp e1 e2))
                            <||> return e1


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


brackets :: Parser LamExpr
brackets = do char '('
              e <-expr1
              char ')'
              return e


--MacroExpr ::= "def" MacroName "=" Expr "in"
macroExpr :: Parser (String, LamExpr)
macroExpr = do char 'd'
               char 'e'
               char 'f'
               char ' '
               m <- macroString
               char ' '
               char '='
               char ' '
               e1 <- chainl1
               char ' '
               char 'i'
               char 'n'
               char ' '
               return (m, e1)



macroBody :: Parser [(String, LamExpr)]
macroBody = do xs <- some1 macroExpr
               return xs

checkMacro :: [(String, LamExpr)] ->Bool
checkMacro xs | nub ( map fst xs) ==map fst xs = True
              | otherwise = False


parseMacro :: Parser LamMacroExpr
parseMacro = do {m<-macroBody; check m}
                 where check m = ( if checkMacro m  
                                   then do e <-chainl1
                                           return (LamDef (m) e) else (P (\cs -> [])))

finalCheck :: Parser LamMacroExpr
finalCheck = parseMacro
             <||> do e <- chainl1
                     return (LamDef [] e)  

--Expr ::= Var | MacroName | Expr Expr | “\” Var “->” Expr | “(“ Expr “)”
expr1 :: Parser LamExpr
expr1 =
        lamAbs <||>
        brackets <||>
        --lamApp <||>
        chainl1 <||>
        var1   
        --macroName 
  
expr2 :: Parser LamExpr
expr2 = lamAbs <||>
        brackets <||>
        var1 <||>
        macroName 


            
runParser :: Parser a -> String -> a
runParser m s =
  case parse m s of
    [(res, [])] -> res
    [(_, rs)]   -> error "Parser did not consume entire stream."
    _           -> error "Parser error."

runParser2 :: Parser a -> String -> Maybe a
runParser2 m s =
  case parse m s of
    [(res, [])] -> Just res 
    [(_, rs)]   -> Nothing
    _           -> Nothing


 
parse1 :: Parser a -> String -> [(a,String)]
parse1 (P p) = p

parseLamExpr :: String -> LamExpr
parseLamExpr xs = fst (head (parse expr1 xs))

parseLamMacro :: String -> Maybe LamMacroExpr
parseLamMacro = runParser2 finalCheck
