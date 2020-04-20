(defn isPrime?
  [n]
  (if (.contains '(2 3 5 7) n) true
      (if (or (even? n) (<= n 1)) false
          (not-any? zero? (map #(rem n %)
                               (range 3
                                      (inc (int (Math/sqrt n)))
                                      2))))))

(prn
 (time
  (nth (filter isPrime?
               (iterate inc 1))
       10000)))
