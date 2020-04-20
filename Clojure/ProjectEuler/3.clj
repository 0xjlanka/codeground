(defn isPrime?
  [n]
  (if (.contains '(2 3 5 7) n) true
      (if (or (even? n) (<= n 1)) false
          (not-any? zero? (map #(rem n %)
                               (range 3
                                      (inc (int (Math/sqrt n)))
                                      2))))))

(defn getFactors
  [n]
  (loop [i n j 1 f #{}]
    (if (>= j i)
      (sort f)
      (if (zero? (rem n j))
        (recur (quot n j) (inc j) (conj f j (quot n j)))
        (recur i (inc j) (conj f))))))

(prn
 (time
  (apply max
         (filter isPrime?
                 (getFactors 600851475143)))))
