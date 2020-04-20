(println
 (time
  (apply max
         (for [x (range 100 1000) y (range 100 1000)
               :when(= (* x y) (Integer. (clojure.string/reverse (str (* x y)))))]
           (* x y)))))
