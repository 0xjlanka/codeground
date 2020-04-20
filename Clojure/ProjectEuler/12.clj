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
  (first (drop-while #(<= (count (getFactors %))
                          500)
                     (map #(apply + (range 1 (inc %)))
                          (iterate inc 1))))))
