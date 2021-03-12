import Data.Char
import Control.Monad
import Control.Applicative
import Data.List

data LamMacroExpr = LamDef [ (String , LamExpr) ] LamExpr deriving (Eq,Show,Read)
data LamExpr = LamMacro String | LamApp LamExpr LamExpr | LamAbs Int LamExpr | LamVar Int deriving (Eq, Show, Read)

lambdaVar :: LamExpr -> Int
lambdaVar (LamVar n) = n
lambdaVar (LamAbs n _) = n
lambdaVar (LamMacro _) = 78
lambdaVar (LamApp e1 _) = lambdaVar e1
 
lambdaTransform :: LamExpr -> LamExpr
lambdaTransform (LamVar n) = let k = 45 in LamAbs k (LamApp (LamVar k) (LamVar n))
lambdaTransform (LamAbs n e) = let k = 49 in LamAbs k (LamApp (LamVar k) (LamAbs n (lambdaTransform e)))
lambdaTransform (LamApp e1 e2) = let k = 78
                                     f = 81
                                     e = 94 
                                 in LamAbs k (LamApp (lambdaTransform e1) (LamAbs f (LamApp (lambdaTransform e2) (LamAbs e (LamApp (LamApp (LamVar f) (LamVar e)) (LamVar k))))))
lambdaTransform (LamMacro x) = LamMacro x

lamMacroTransform :: [(String, LamExpr)] -> [(String, LamExpr)]
lamMacroTransform [] = []
lamMacroTransform ((s, e):xs) = (s, lambdaTransform e) : lamMacroTransform xs

cpsTransform :: LamMacroExpr -> LamMacroExpr
cpsTransform (LamDef m e) = LamDef (lamMacroTransform m) (lambdaTransform e)