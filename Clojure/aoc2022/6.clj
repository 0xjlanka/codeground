
(defn findstr
  "Finds the first occurance of unique n letters in the arg"
  [x n]
  (loop [s x
         pos 0]
    (if (empty? s) :none
        (if (= n (count (set (take n s)))) (+ pos n)
            (recur (rest s) (inc pos))))))
(time
 (let [a (clojure.string/split (slurp "6.txt") #"\n")]
   (prn (map #(findstr % 4) a))
   (prn (map #(findstr % 14) a))))
