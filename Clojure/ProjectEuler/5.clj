(defn gcd
  [a b]
  (loop [i a j b]
    (if (zero? (rem i j)) j
        (recur j (rem i j)))))
(defn lcm
  [a b]
  (/ (* a b)
     (gcd a b)))

(prn
 (time
  (reduce lcm
          (range 1 20))))
