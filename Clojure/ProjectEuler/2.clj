(defn fib
  [a b]
  (lazy-seq (cons a (fib b (+ a b)))))

(println (time (reduce +
                 (filter even?
                         (take-while #(< % 4000000)
                                     (fib 1 2))))))
