(require '[clojure.string :as str])

(time (let [a (slurp "1.txt")
      b (str/split a #"\n\n")
      c (map #(str/split % #"\n") b)
      d (for [x c] (reduce + (map #(Integer/parseInt %) x)))
      e (take-last 3 (sort d))]
  (println (last e)) (println (apply + e))))

