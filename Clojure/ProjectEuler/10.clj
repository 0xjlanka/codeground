(defn isPrime?
  [n]
  (cond
    (.contains '(2 3 5 7) n) true
    (or (even? n) (<= n 1)) false
    :else (not-any? zero? (map #(rem n %)
                               (range 3
                                      (inc (int (Math/sqrt n)))
                                      2)))))

(defn primeSeq
  []
  (filter isPrime?
          (cons 2
                (iterate (partial + 2)
                         1))))

(prn
 (time
  (reduce + (take-while #(< % 2000000) (primeSeq)))))
