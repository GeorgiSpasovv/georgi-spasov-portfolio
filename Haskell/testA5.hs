


foo [] = error "empty list"
foo (x:xs) = foo1 x xs 
 where 
   foo1  y [] = y
   foo1  y (x:xs) = foo1  (f y x) xs

                         