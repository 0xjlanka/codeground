(defn get-sorted [st]
  (let [a (clojure.string/split st #"\n")
        b (sort (map #(Integer/parseInt %) a))]
    b))
(defn get-similarity-score [num lst]
  (let [a (count (filter #(= num %) lst))]
    (* num a)))

(time
 (let [a (get-sorted (slurp "p1l.txt"))
       b (get-sorted (slurp "p1r.txt"))
       c (reduce + (map abs (map - a b)))
       d (reduce + (map #(get-similarity-score % b) a))]
  (prn "1:" c)
  (prn "2:" d)))


